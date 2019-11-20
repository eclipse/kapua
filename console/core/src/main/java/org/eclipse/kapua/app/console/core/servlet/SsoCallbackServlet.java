/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.kapua.app.console.core.server.util.SsoHelper;
import org.eclipse.kapua.app.console.core.server.util.SsoLocator;
import org.eclipse.kapua.sso.SingleSignOnLocator;
import org.eclipse.kapua.sso.exception.SsoException;
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

    private SingleSignOnLocator locator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.locator = SsoLocator.getLocator(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String authCode = req.getParameter("code");

        final URI redirectUri = SsoHelper.getRedirectUri();
        if (authCode != null) {

            final JsonObject jsonObject;
            try {
                jsonObject = locator.getService().getAccessToken(authCode, redirectUri);
            } catch (SsoException sje) {
                throw new ServletException("Failed to get access token: " + sje.getMessage(), sje);
            }

            // Get and clean jwks_uri property
            final String accessToken = jsonObject.getString("access_token");
            final String homeUri;
            try {
                homeUri = SsoHelper.getHomeUri();
            } catch (SsoIllegalUriException e) {
                throw new ServletException("Failed to get Home URI (null or empty).");
            }

            try {
                final URIBuilder redirect = new URIBuilder(homeUri);
                redirect.addParameter("access_token", accessToken);
                resp.sendRedirect(redirect.toString());
            } catch (final URISyntaxException e) {
                throw new ServletException("Failed to parse redirect URL: " + homeUri, e);
            }
        } else {

            // access_token is null, collect possible error
            final String error = req.getParameter("error");
            String errorDescription = "";
            if (error != null) {
                errorDescription = req.getParameter("error_description");
                logger.warn("Failed to log in: {}, error_description: {}", error, errorDescription);
            } else {
                throw new ServletException("Invalid HttpServletRequest");
            }
            final String homeUri;
            try {
                homeUri = SsoHelper.getHomeUri();
            } catch (SsoIllegalUriException e) {
                throw new ServletException("Failed to get Home URI (null or empty).");
            }
            try {
                final URIBuilder redirect = new URIBuilder(homeUri);
                redirect.addParameter("error", error);
                redirect.addParameter("error_description", errorDescription);
                resp.sendRedirect(redirect.toString());
            } catch (final URISyntaxException e) {
                throw new ServletException("Failed to parse redirect URL: " + homeUri, e);
            }
        }
    }
}
