/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang.time.DateUtils;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.credential.mfa.KapuaExistingMfaOptionException;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionAttributes;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticator;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.mfa.MfaAuthenticatorServiceLocator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * {@link MfaOptionService} implementation.
 */
@KapuaProvider
public class MfaOptionServiceImpl extends AbstractKapuaService implements MfaOptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MfaOptionServiceImpl.class);
    private static final MfaAuthenticatorServiceLocator MFA_AUTH_SERVICE_LOCATOR = MfaAuthenticatorServiceLocator.getInstance();
    private static final MfaAuthenticator MFA_AUTHENTICATOR = MFA_AUTH_SERVICE_LOCATOR.getMfaAuthenticator();
    private static final int QR_CODE_SIZE = 134;  // TODO: make this configurable?
    private static final String IMAGE_FORMAT = "png";
    private static final int TRUST_KEY_DURATION = 30; // duration of the trust key in days
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final UserService userService = locator.getService(UserService.class);

    public MfaOptionServiceImpl() {
        super(MfaOptionEntityManagerFactory.getInstance());
    }

    @Override
    public MfaOption create(MfaOptionCreator mfaOptionCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(mfaOptionCreator, "mfaOptionCreator");
        ArgumentValidator.notNull(mfaOptionCreator.getScopeId(), "mfaOptionCreator.scopeId");
        ArgumentValidator.notNull(mfaOptionCreator.getUserId(), "mfaOptionCreator.userId");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write,
                mfaOptionCreator.getScopeId()));

        //
        // Check existing MfaOption
        MfaOption existingMfaOption = findByUserId(mfaOptionCreator.getScopeId(), mfaOptionCreator.getUserId());
        if (existingMfaOption != null) {
            throw new KapuaExistingMfaOptionException();
        }

        //
        // Do create
        MfaOption mfaOption;
        EntityManager em = AuthenticationEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            String fullKey = MFA_AUTHENTICATOR.generateKey();
            mfaOptionCreator = new MfaOptionCreatorImpl(mfaOptionCreator.getScopeId(), mfaOptionCreator.getUserId(), fullKey);
            mfaOption = MfaOptionDAO.create(em, mfaOptionCreator);
            mfaOption = MfaOptionDAO.find(em, mfaOption.getScopeId(), mfaOption.getId());

            // generating base64 QR code image
            final MfaOptionCreator mfaOptionCreatorCopy = mfaOptionCreator;
            Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(mfaOptionCreatorCopy.getScopeId()));
            User user = KapuaSecurityUtils.doPrivileged(() -> userService.find(mfaOptionCreatorCopy.getScopeId(), mfaOptionCreatorCopy.getUserId()));
            mfaOption.setQRCodeImage(generateQRCode(account.getName(), user.getName(), fullKey));

            em.commit();
        } catch (Exception pe) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(pe);
        } finally {
            em.close();
        }

        return mfaOption;
    }

    @Override
    public MfaOption update(MfaOption mfaOption) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(mfaOption, "mfaOption");
        ArgumentValidator.notNull(mfaOption.getId(), "mfaOption.id");
        ArgumentValidator.notNull(mfaOption.getScopeId(), "mfaOption.scopeId");
        ArgumentValidator.notNull(mfaOption.getUserId(), "mfaOption.userId");
        ArgumentValidator.notEmptyOrNull(mfaOption.getMfaSecretKey(), "mfaOption.mfaSecretKey");

        //
        // Check access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.write, mfaOption.getScopeId()));

        return entityManagerSession.doTransactedAction(em -> {
            MfaOption currentMfaOption = MfaOptionDAO.find(em, mfaOption.getScopeId(), mfaOption.getId());

            if (currentMfaOption == null) {
                throw new KapuaEntityNotFoundException(MfaOption.TYPE, mfaOption.getId());
            }

            // Passing attributes??
            return MfaOptionDAO.update(em, mfaOption);
        });
    }

    @Override
    public MfaOption find(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(mfaOptionId, "mfaOptionId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.doAction(em -> MfaOptionDAO.find(em, scopeId, mfaOptionId));
    }

    @Override
    public MfaOptionListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> MfaOptionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> MfaOptionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(mfaOptionId, "mfaOptionId");
        ArgumentValidator.notNull(scopeId, "scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.doTransactedAction(em -> {
            if (MfaOptionDAO.find(em, scopeId, mfaOptionId) == null) {
                throw new KapuaEntityNotFoundException(MfaOption.TYPE, mfaOptionId);
            }
            return MfaOptionDAO.delete(em, scopeId, mfaOptionId);
        });
    }

    @Override
    public MfaOption findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(userId, MfaOptionAttributes.USER_ID);

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(AuthenticationDomains.CREDENTIAL_DOMAIN, Actions.read, scopeId));

        //
        // Build query
        MfaOptionQuery query = new MfaOptionQueryImpl(scopeId);
        QueryPredicate predicate = query.attributePredicate(MfaOptionAttributes.USER_ID, userId);
        query.setPredicate(predicate);

        //
        // Query and return result
        MfaOptionListResult result = query(query);
        if (!result.isEmpty()) {
            return result.getFirstItem();
        } else {
            return null;
        }
    }

    @Override
    public void enableTrust(MfaOption mfaOption) throws KapuaException {

        // Argument Validation (fields validation is performed inside the 'update' method)
        ArgumentValidator.notNull(mfaOption, "mfaOption");

        // If MfaOption has a code set, use that one, otherwise other trusted machines won't be able to login and a new trusted machine will be
        // requested
        String trustKey = mfaOption.getTrustKey();
        if (trustKey == null || trustKey.isEmpty()) {
            trustKey = generateTrustKey();
        }

        Date expirationDate = new Date(System.currentTimeMillis());
        expirationDate = DateUtils.addDays(expirationDate, TRUST_KEY_DURATION);

        mfaOption.setTrustKey(trustKey);
        mfaOption.setTrustExpirationDate(expirationDate);
        update(mfaOption);
    }

    @Override
    public void enableTrust(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {

        // Argument Validation
        ArgumentValidator.notNull(mfaOptionId, "mfaOptionId");
        ArgumentValidator.notNull(scopeId, "scopeId");

        // extracting the MfaOption
        MfaOption mfaOption = find(scopeId, mfaOptionId);
        enableTrust(mfaOption);
    }

    @Override
    public void disableTrust(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {

        // Argument Validation
        ArgumentValidator.notNull(mfaOptionId, "mfaOptionId");
        ArgumentValidator.notNull(scopeId, "scopeId");

        // extracting the MfaOption
        MfaOption mfaOption = find(scopeId, mfaOptionId);

        // Reset the trust machine fields
        mfaOption.setTrustKey(null);
        mfaOption.setTrustExpirationDate(null);

        update(mfaOption);
    }

    /**
     * Generate the trust key string.
     *
     * @return String
     */
    private String generateTrustKey() {
        return UUID.randomUUID().toString();
    }

    private void deleteMfaOptionByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        MfaOptionFactory mfaOptionFactory = locator.getFactory(MfaOptionFactory.class);

        MfaOptionQuery query = mfaOptionFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(MfaOptionAttributes.USER_ID, userId));

        MfaOptionListResult mfaOptionsToDelete = query(query);

        for (MfaOption c : mfaOptionsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

    private void deleteMfaOptionByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        MfaOptionFactory mfaOptionFactory = locator.getFactory(MfaOptionFactory.class);

        MfaOptionQuery query = mfaOptionFactory.newQuery(accountId);

        MfaOptionListResult mfaOptionsToDelete = query(query);

        for (MfaOption c : mfaOptionsToDelete.getItems()) {
            delete(c.getScopeId(), c.getId());
        }
    }

    /**
     * Produce a QR code in base64 format for the authenticator app
     *
     * @param accountName the account name of the account to which the user belongs
     * @param username    the username
     * @param key         the Mfa secret key in plain text
     * @return the QR code image in base64 format
     */
    private String generateQRCode(String accountName, String username, String key) throws IOException, WriterException {
        // url to qr_barcode encoding
        StringBuilder sb = new StringBuilder();
        sb.append("otpauth://totp/")
                .append(username)
                .append("@")
                .append(accountName) // TODO: not sure that we also need the account name
                .append("?secret=")
                .append(key);

        BitMatrix bitMatrix = new QRCodeWriter().encode(sb.toString(), BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);
        BufferedImage image = buildImage(bitMatrix);
        return imgToBase64(image);
    }

    /**
     * Converts a {@link BufferedImage} to base64 string format
     *
     * @param img        the {@link BufferedImage} to convert
     * @return the base64 string representation of the input image
     */
    private static String imgToBase64(BufferedImage img) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img, IMAGE_FORMAT, outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * Converts a {@link BitMatrix} to a {@link BufferedImage}
     *
     * @param bitMatrix the {@link BitMatrix} to be converted into ad image
     * @return the {@link BufferedImage} obtained from the conversion
     */
    private static BufferedImage buildImage(BitMatrix bitMatrix) {
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage resultImage = new BufferedImage(QR_CODE_SIZE, QR_CODE_SIZE, BufferedImage.TYPE_INT_RGB);

        Graphics g = resultImage.getGraphics();
        g.drawImage(qrCodeImage, 0, 0, new Color(232, 232, 232, 255), null);

        return resultImage;
    }

}
