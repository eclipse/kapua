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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;

public class SsoCallbackServlet extends HttpServlet {

    private static final long serialVersionUID = -4854037814597039013L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConsoleSetting settings = ConsoleSetting.getInstance();
        
        String authCode = req.getParameter("code");
        
        String uri = settings.getString(ConsoleSettingKeys.SSO_OPENID_SERVER_ENDPOINT_TOKEN);
        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");

        String urlParameters = "grant_type=authorization_code&code=" + authCode + "&client_id=" + settings.getString(ConsoleSettingKeys.SSO_OPENID_CLIENT_ID) + "&redirect_uri=" + settings.getString(ConsoleSettingKeys.SSO_OPENID_REDIRECT_URI);
        
        // Send post request
        urlConnection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        outputStream.writeBytes(urlParameters);
        outputStream.flush();
        outputStream.close();

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        // parse result
        JsonReader jsonReader = Json.createReader(inputReader);

        // Parse json response
        JsonObject jsonObject = jsonReader.readObject();
        inputReader.close();

        // Get and clean jwks_uri property
        String accessToken = jsonObject.getString("access_token");
        String baseUri = settings.getString(ConsoleSettingKeys.SITE_HOME_URI);
        String separator = baseUri.contains("?") ? "&" : "?";
        resp.sendRedirect(baseUri + separator + "access_token=" + accessToken);
    }    
}
