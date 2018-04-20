/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.server;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class KapuaRemoteServiceServlet extends RemoteServiceServlet {

    private static final long serialVersionUID = 8615748799422377390L;

    private static final Logger logger = LoggerFactory.getLogger(KapuaRemoteServiceServlet.class);

    /**
     *
     * If the given xsrfToken is not valid a GwtKapuaException will throw.
     * Check if the given xsrfToken is valid, otherwise traces the user network info and invalidates the user session.
     *
     * @param xsrfToken
     * @throws GwtKapuaException
     */
    public void checkXSRFToken(GwtXSRFToken xsrfToken)
            throws GwtKapuaException {
        HttpServletRequest req = perThreadRequest.get();
        performXSRFTokenValidation(req, xsrfToken);
    }

    /**
     *
     * This method perform a XSRF validation on the given request and for the specific userToken.
     * This is a private method to support both, standard class validation or multipart Servlet validation.
     *
     * @param req
     * @param userToken
     */
    private static void performXSRFTokenValidation(HttpServletRequest req, GwtXSRFToken userToken)
            throws GwtKapuaException {
        HttpSession session = req.getSession();

        if (!isValidXSRFToken(session, userToken)) {
            if (session != null) {
                logger.info("XSRF token is NOT VALID - Token={}", userToken.getToken());
                logger.debug("\tSender IP: {}", req.getRemoteAddr());
                logger.debug("\tSender Host: {}", req.getRemoteHost());
                logger.debug("\tSender Port: {}", req.getRemotePort());
                logger.debug("\tFull Request URL\n {}?{}\n\n",
                        req.getRequestURL().toString(),
                        req.getQueryString());

                // forcing the console log out
                session.invalidate();
            }

            logger.debug("Session invalidated.");

            throw new GwtKapuaException(GwtKapuaErrorCode.XSRF_INVALID_TOKEN, null, "Invalid XSRF token");
        }
    }

    /**
     *
     * Verify if the given userToken is valid on the given session.
     * This method tests if the server xsrf token is equals on the user token.
     * If yes, the method returns true, otherwise returns false.
     * This method controls the xsrf token date validity based on the expire date field.
     *
     * @param session
     * @param userToken
     * @return boolean
     */
    public static boolean isValidXSRFToken(HttpSession session, GwtXSRFToken userToken) {
        logger.debug("Starting XSRF Token validation...'");

        if (userToken == null) {
            logger.debug("XSRF Token is NOT VALID -> NULL TOKEN");
            return false;
        }

        // Retrieve the server side token
        GwtXSRFToken serverXSRFToken = (GwtXSRFToken) session.getAttribute(GwtSecurityTokenServiceImpl.XSRF_TOKEN_KEY);
        if (serverXSRFToken != null) {
            String serverToken = serverXSRFToken.getToken();

            // Checking the XSRF validity on the serverToken
            if (isValidStringToken(serverToken)) {
                if (isValidStringToken(userToken.getToken())) {
                    if (serverToken.equals(userToken.getToken())) {
                        // Checking expire date
                        if (new Date().before(userToken.getExpiresOn())) {
                            logger.debug("XSRF Token is VALID - {}", userToken.getToken());

                            // Reset used token
                            session.setAttribute(GwtSecurityTokenServiceImpl.XSRF_TOKEN_KEY, null);
                            return true;
                        } else {
                            session.setAttribute(GwtSecurityTokenServiceImpl.XSRF_TOKEN_KEY, null);
                            logger.error("XSRF Token is EXPIRED - {}", userToken.getToken());
                        }
                    }
                }
            }
        }

        logger.debug("XSRF Token is NOT VALID - {}", userToken.getToken());
        return false;
    }

    /**
     *
     * Performs some basic string validation on the given String token.
     *
     * @param token
     * @return boolean
     */
    private static boolean isValidStringToken(String token) {
        if (token != null) {
            if (!token.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Get the given value field from the Servlet request if exist.
     * Returns null if the passed fieldName isn't present in the request.
     *
     * @param req
     * @param fieldName
     * @return String
     * @throws Exception
     */
    public static String getFieldFromMultiPartForm(HttpServletRequest req, String fieldName)
            throws Exception {
        String fieldValue = null;

        ServletFileUpload upload = new ServletFileUpload();
        List<FileItem> items = upload.parseRequest(req);

        // Process the uploaded items
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();
            if (item.isFormField()) {
                String name = item.getFieldName();

                if (name.equals(fieldName)) {
                    fieldValue = item.getString();
                    logger.debug("Found field name '{}' with value: {}", name, fieldValue);
                }
            }
        }

        return fieldValue;
    }

    static public void checkXSRFToken(HttpServletRequest req, GwtXSRFToken token)
            throws Exception {
        performXSRFTokenValidation(req, token);
    }

    /**
     *
     * Check if the given xsrfToken is valid, otherwise traces the user network info and invalidates the user session.
     * This is the checkXSRFToken for the MultiPart Servlet support.
     *
     * @param req
     * @throws Exception
     */
    public static void checkXSRFTokenMultiPart(HttpServletRequest req, GwtXSRFToken token)
            throws Exception {
        performXSRFTokenValidation(req, token);
    }

}
