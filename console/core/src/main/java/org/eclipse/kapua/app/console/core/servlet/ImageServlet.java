/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.servlet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.io.FileCleaningTracker;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementException;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = -5016170117606322129L;
    private static final Logger logger = LoggerFactory.getLogger(ImageServlet.class);

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final UserService userService = locator.getService(UserService.class);

    DiskFileItemFactory diskFileItemFactory;
    FileCleaningTracker fileCleaningTracker;

    private static final int QR_CODE_SIZE = 134;  // TODO: make this configurable?

    @Override
    public void destroy() {
        super.destroy();
        logger.info("Servlet {} destroyed", getServletName());
        if (fileCleaningTracker != null) {
            logger.info("Number of temporary files tracked: " + fileCleaningTracker.getTrackCount());
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Servlet {} initialized", getServletName());

        ServletContext ctx = getServletContext();
        fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(ctx);

        int sizeThreshold = ConsoleSetting.getInstance().getInt(ConsoleSettingKeys.FILE_UPLOAD_INMEMORY_SIZE_THRESHOLD);
        File repository = new File(System.getProperty("java.io.tmpdir"));

        logger.info("DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD: {}", DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        logger.info("DiskFileItemFactory: using size threshold of: {}", sizeThreshold);

        diskFileItemFactory = new DiskFileItemFactory(sizeThreshold, repository);
        diskFileItemFactory.setFileCleaningTracker(fileCleaningTracker);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("image/png");

        String reqPathInfo = req.getPathInfo();
        if (reqPathInfo == null) {
            resp.sendError(404);
            return;
        }

        logger.debug("req.getRequestURI(): {}", req.getRequestURI());
        logger.debug("req.getRequestURL(): {}", req.getRequestURL());
        logger.debug("req.getPathInfo(): {}", req.getPathInfo());

        if (reqPathInfo.equals("/2FAQRcode")) {
            doGetQRCode(req, resp);
        } else {
            resp.sendError(404);
        }
    }

    private void doGetQRCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final String username = req.getParameter("username");
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("username");
            }

            final String accountName = req.getParameter("accountName");
            if (accountName == null || accountName.isEmpty()) {
                throw new IllegalArgumentException("accountName");
            }

            String key = req.getParameter("key");
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("key");
            }

            // this is only used to avoid that images are taken by the browser cache instead of being generated again
            String timestamp = req.getParameter("timestamp");
            if (timestamp == null || timestamp.isEmpty()) {
                throw new IllegalArgumentException("timestamp");
            }

            // check session
            final KapuaSession session = KapuaSecurityUtils.getSession();
            if (session == null) {
                throw new KapuaUnauthenticatedException();
            }

            // check account existence, using do privileged since the user might not have the required permissions
            Account account = KapuaSecurityUtils.doPrivileged(new Callable<Account>() {

                @Override
                public Account call() throws Exception {
                    return accountService.findByName(accountName);
                }
            });
            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, accountName);
            }

            // check user existence, using do privileged since the user might not have the required permissions
            User user = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.findByName(username);
                }
            });
            if (user == null) {
                throw new KapuaEntityNotFoundException(User.TYPE, accountName);
            }

            // decrypt the secret
            String decryptedKey = AuthenticationUtils.decryptAes(key);

            /*
            Note that there is no need to verify the secret, since this servlet is only called when the secret has just been generated.
            Moreover, it would not make any sense to perform it with a fake secret, since it wouldn't be usable for logging in.
             */

            // url to qr_barcode encoding
            StringBuilder sb = new StringBuilder();
            sb.append("otpauth://totp/")
                    .append(username)
                    .append("@")
                    .append(accountName) // TODO: not sure that we also need the account name
                    .append("?secret=")
                    .append(decryptedKey);

            BitMatrix bitMatrix = new QRCodeWriter().encode(sb.toString(),
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE);

            BufferedImage resultImage = buildImage(bitMatrix);
            ImageIO.write(resultImage, "png", resp.getOutputStream());

        } catch (IllegalArgumentException iae) {
            resp.sendError(400, "Illegal value for query parameter: " + iae.getMessage());
        } catch (KapuaEntityNotFoundException eenfe) {
            resp.sendError(400, eenfe.getMessage());
        } catch (KapuaUnauthenticatedException eiae) {
            resp.sendError(401, eiae.getMessage());
        } catch (KapuaIllegalAccessException eiae) {
            resp.sendError(403, eiae.getMessage());
        } catch (DeviceManagementException edme) {
            logger.error("Device menagement exception", edme);
            resp.sendError(404, edme.getMessage());
        } catch (Exception e) {
            logger.error("Generic error", e);
            resp.sendError(500, e.getMessage());
        }
    }

    private BufferedImage buildImage(BitMatrix bitMatrix) {
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage resultImage = new BufferedImage(QR_CODE_SIZE,
                QR_CODE_SIZE,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = resultImage.getGraphics();
        g.drawImage(qrCodeImage, 0, 0, new Color(232, 232, 232, 255), null);

        return resultImage;
    }
}
