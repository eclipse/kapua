/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;

public class SsoCallbackServlet extends HttpServlet {

    private static final long serialVersionUID = -4854037814597039013L;

    private final ConsoleSetting settings = ConsoleSetting.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String authCode = req.getParameter("code");

        final URL url = new URL(settings.getString(ConsoleSettingKeys.SSO_OPENID_SERVER_ENDPOINT_TOKEN));
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        parameters.add(new BasicNameValuePair("code", authCode));
        parameters.add(new BasicNameValuePair("client_id", settings.getString(ConsoleSettingKeys.SSO_OPENID_CLIENT_ID)));

        final String clientSecret = settings.getString(ConsoleSettingKeys.SSO_OPENID_CLIENT_SECRET);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            parameters.add(new BasicNameValuePair("client_secret", clientSecret));
        }

        parameters.add(new BasicNameValuePair("redirect_uri", settings.getString(ConsoleSettingKeys.SSO_OPENID_REDIRECT_URI)));
        final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);

        // Send post request
        urlConnection.setDoOutput(true);

        try (final OutputStream outputStream = urlConnection.getOutputStream()) {
            entity.writeTo(outputStream);
        }

        final JsonObject jsonObject;
        try (final InputStream stream = urlConnection.getInputStream()) {
            // parse result
            jsonObject = Json.createReader(stream).readObject();
        }

        // Get and clean jwks_uri property
        final String accessToken = jsonObject.getString("access_token");
        final String homeUri = settings.getString(ConsoleSettingKeys.SITE_HOME_URI);

        try {
            final URIBuilder redirect = new URIBuilder(homeUri);
            redirect.addParameter("access_token", accessToken);
            resp.sendRedirect(redirect.toString());
        } catch (final URISyntaxException e) {
            throw new ServletException("Failed to parse redirect URL: " + homeUri, e);
        }
    }
}
