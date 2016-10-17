/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaErrorHandlerServlet extends KapuaHttpServlet {

    private static final long serialVersionUID = 823090686760110256L;

    private static Logger s_logger = LoggerFactory.getLogger(KapuaErrorHandlerServlet.class);

    private static String HTTP_ERROR_PATH = "/httpError";
    private static String THROWABLE_PATH = "/throwable";

    private static String httpErrorTemplate = "<!doctype html>" +
            "<html>" +
            "   <head>" +
            "      <title>Kapua&copy; Console - ${statusCode}</title>" +
            "      <style>body {padding: 10px;}h1 {color: #1E80B0;}pre {display: none}.label {font-weight: bold;} .message {}</style>" +
            "   </head>" +
            "   <body>" +
            "      <div>" +
            "         <h1>Kapua&copy;</h1>" +
            "         <table>" +
            "            <tr>" +
            "               <td class='label'>HTTP Error Code:</td>" +
            "               <td class='message'>${statusCode}</td>" +
            "            </tr>" +
            "            <tr>" +
            "               <td class='label'>Requested Resource:</td>" +
            "               <td class='message'>${requestUri}</td>" +
            "            </tr>" +
            "            <tr>" +
            "               <td class='label'>Error Message:</td>" +
            "               <td class='message'>${errorMessage}</td>" +
            "            </tr>" +
            "         </table>" +
            "         <pre>${errorMessage}</pre>" +
            "      </div>" +
            "   </body>" +
            "</html>";
    private static String throwableErrorTemplate = "<!doctype html>" +
            "<html>" +
            "   <head>" +
            "      <title>Kapua&copy; Console - ${statusCode}</title>" +
            "      <style>body {padding: 10px;}h1 {color: #1E80B0;}pre {display: none}.label {font-weight: bold;} .message {}</style>" +
            "   </head>" +
            "   <body>" +
            "      <div>" +
            "         <h1>Kapua&copy;</h1>" +
            "         <table>" +
            "            <tr>" +
            "               <td class='label'>HTTP Error Code:</td>" +
            "               <td class='message'>${statusCode}</td>" +
            "            </tr>" +
            "            <tr>" +
            "               <td class='label'>Requested Resource:</td>" +
            "               <td class='message'>${requestUri}</td>" +
            "            </tr>" +
            "            <tr>" +
            "               <td class='label'>Error Message:</td>" +
            "               <td class='message'>${errorMessage}</td>" +
            "            </tr>" +
            "            <tr>" +
            "                <td class='label'>Exception Message:</td>" +
            "                <td class='message'>${exceptionMessage}</td>" +
            "            </tr>" +
            "         </table>" +
            "         <pre>${errorMessage}</pre>" +
            "      </div>" +
            "   </body>" +
            "</html>";

    public KapuaErrorHandlerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = (String) request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(404);
            return;
        }

        if (pathInfo.startsWith(HTTP_ERROR_PATH)) {
            processHttpError(request, response);
        } else if (pathInfo.startsWith(THROWABLE_PATH)) {
            processThrowable(request, response);
        } else {
            response.sendError(404);
            return;
        }
    }

    private void processHttpError(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Getting parameters from the request
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");

        // Defaulting them if null
        if (statusCode == null) {
            statusCode = 500;
        }
        if (requestUri == null) {
            requestUri = "Unknown";
        }
        if (errorMessage == null) {
            errorMessage = "Internal Server Error";
        }

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("statusCode", statusCode);
        velocityContext.put("requestUri", requestUri);
        velocityContext.put("errorMessage", errorMessage);

        PrintWriter out = response.getWriter();
        Velocity.evaluate(velocityContext, out, "", httpErrorTemplate);
        out.close();

        s_logger.error("Processed HTTP error! Code: {} - Request: {} - Error: {}",
                new Object[] { statusCode, requestUri, errorMessage });
    }

    private void processThrowable(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Getting parameters from the request
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");

        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        String exceptionMessage = null;
        if (throwable != null) {
            exceptionMessage = throwable.toString();
        }

        // Defaulting them if null
        if (statusCode == null) {
            statusCode = 500;
        }
        if (requestUri == null) {
            requestUri = "Unknown";
        }
        if (errorMessage == null) {
            errorMessage = "Internal Server Error";
        }
        if (exceptionMessage == null) {
            exceptionMessage = "Unknown";
        }

        requestUri = requestUri == null ? "Unknown" : requestUri;

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("statusCode", statusCode);
        velocityContext.put("requestUri", requestUri);
        velocityContext.put("errorMessage", errorMessage);
        velocityContext.put("exceptionMessage", exceptionMessage);

        PrintWriter out = response.getWriter();
        Velocity.evaluate(velocityContext, out, "", throwableErrorTemplate);
        out.close();

        s_logger.error("Processed HTTP error! Code: {} - Request: {} - Error: {}",
                new Object[] { statusCode, requestUri, errorMessage });
    }
}
