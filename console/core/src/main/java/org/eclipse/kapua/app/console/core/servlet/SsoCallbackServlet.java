/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.servlet;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.kapua.app.console.core.server.util.SsoHelper;
import org.eclipse.kapua.app.console.core.server.util.SsoLocator;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.sso.SingleSignOnLocator;
import org.eclipse.kapua.sso.exception.SsoAccessTokenException;
import org.eclipse.kapua.sso.exception.uri.SsoIllegalUriException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SsoCallbackServlet extends HttpServlet {

    private static final long serialVersionUID = -4854037814597039013L;

    private static final Logger logger = LoggerFactory.getLogger(SsoCallbackServlet.class);

    private static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String ID_TOKEN_PARAM = "id_token";
    private static final String ERROR_PARAM = "error";
    private static final String ERROR_DESCRIPTION_PARAM = "error_description";
    private static final String HIDDEN_SECRET = "****";

    private SingleSignOnLocator locator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.locator = SsoLocator.getLocator(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String authCode = req.getParameter("code");

        String homeUri = "";
        ConfigurationPrinter httpReqLogger =
                ConfigurationPrinter
                        .create()
                        .withLogger(logger)
                        .withLogLevel(ConfigurationPrinter.LogLevel.DEBUG)
                        .withTitle("SSO Servlet Log")
                        .addHeader("SSO servlet request:")
                        .increaseIndentation()
                        .addParameter("Request URL", req.getRequestURL())
                        .addParameter("AuthCode", HIDDEN_SECRET)
                        .decreaseIndentation();
        try {
            homeUri = SsoHelper.getHomeUri();
            httpReqLogger.addHeader("SSO servlet response:");
            httpReqLogger.increaseIndentation().addParameter("Response URI", homeUri);
            final URIBuilder redirect = new URIBuilder(homeUri);

            if (authCode != null) {
                final URI redirectUri = SsoHelper.getRedirectUri();
                final JsonObject jsonObject = locator.getService().getAccessToken(authCode, redirectUri);

                // Get and clean jwks_uri property
                final String accessToken = jsonObject.getString(ACCESS_TOKEN_PARAM);
                final String idToken = jsonObject.getString(ID_TOKEN_PARAM);
                redirect.addParameter(ACCESS_TOKEN_PARAM, accessToken);
                redirect.addParameter(ID_TOKEN_PARAM, idToken);
                resp.sendRedirect(redirect.toString());

                httpReqLogger.addParameter(ACCESS_TOKEN_PARAM, HIDDEN_SECRET);
                httpReqLogger.addParameter(ID_TOKEN_PARAM, HIDDEN_SECRET);
                logger.debug("Successfully sent the redirect response to {}", homeUri);
            } else {

                // access_token is null, collect possible error
                final String error = req.getParameter(ERROR_PARAM);
                if (error != null) {
                    String errorDescription = req.getParameter(ERROR_DESCRIPTION_PARAM);
                    redirect.addParameter(ERROR_PARAM, error);
                    redirect.addParameter(ERROR_DESCRIPTION_PARAM, errorDescription);
                    resp.sendRedirect(redirect.toString());

                    httpReqLogger.addParameter(ERROR_PARAM, error);
                    httpReqLogger.addParameter(ERROR_DESCRIPTION_PARAM, errorDescription);
                    logger.warn("Failed to log in: {}, error_description: {}", error, errorDescription);
                } else {
                    resp.sendError(400);
                    httpReqLogger.addParameter("Error", "400 Bad Request");
                    logger.error("Invalid HttpServletRequest, both 'access_token' and 'error' parameters are 'null'");
                }
            }
        } catch (SsoIllegalUriException siue) {
            throw new ServletException("Failed to get Home URI (null or empty): " + siue.getMessage(), siue);
        } catch (SsoAccessTokenException sate) {
            throw new ServletException("Failed to get access token: " + sate.getMessage(), sate);
        } catch (URISyntaxException use) {
            throw new ServletException("Failed to parse redirect URL " + homeUri + " : " + use.getMessage(), use);
        } finally {
            httpReqLogger.printLog();
        }
    }
}
