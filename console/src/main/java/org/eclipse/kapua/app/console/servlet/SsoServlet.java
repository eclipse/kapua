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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SsoServlet extends HttpServlet {

    private static final long serialVersionUID = -4854037814597039013L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HTTP/1.1 302 Found
//        Location: https://server.example.com/authorize?
//          response_type=code
//          &scope=openid%20profile%20email
//          &client_id=s6BhdRkqt3
//          &state=af0ifjsldkj
//          &redirect_uri=https%3A%2F%2Fclient.example.org%2Fcb
//
//          http://localhost:9090/auth/realms/master/protocol/openid-connect/auth
//          response_type=code&client_id=console&state=af0ifjsldkj&redirect_uri=http://localhost:8889
        
        String uri = "http://localhost:9090/auth/realms/master/protocol/openid-connect/auth?response_type=code&client_id=console&state=af0ifjsldkj&redirect_uri=http://localhost:8889/sso/callback";
        resp.sendRedirect(uri);
    }    
    
    
}
