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
import java.net.URI;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SsoCallbackServlet extends HttpServlet {

    private static final long serialVersionUID = -4854037814597039013L;
    private static final String callbackUrl = "http://localhost:8889/sso/callback";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String authCode = req.getParameter("code");
        
        String uri = "http://localhost:9090/auth/realms/master/protocol/openid-connect/token";
        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");

        String urlParameters = "grant_type=authorization_code&code=" + authCode + "&client_id=console&redirect_uri=" + callbackUrl;
        
        // Send post request
        urlConnection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        outputStream.writeBytes(urlParameters);
        outputStream.flush();
        outputStream.close();

        int responseCode = urlConnection.getResponseCode();

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        // parse result
        JsonReader jsonReader = Json.createReader(inputReader);

        // Parse json response
        JsonObject jsonObject = jsonReader.readObject();
        inputReader.close();

        // Get and clean jwks_uri property
        String accessToken = jsonObject.getString("access_token");
        
        resp.sendRedirect("http://localhost:8889/console.jsp?gwt.codesvr=127.0.0.1:9997&access_token=" + accessToken);
    }    
}
