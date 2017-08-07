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
package org.eclipse.kapua.app.console.core.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.html.HtmlEscapers;
import org.eclipse.scada.utils.ExceptionHelper;
import org.eclipse.scada.utils.str.StringReplacer;
import org.eclipse.scada.utils.str.StringReplacer.ReplaceSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaErrorHandlerServlet extends KapuaHttpServlet {

    private static final long serialVersionUID = 823090686760110256L;

    private static final Logger logger = LoggerFactory.getLogger(KapuaErrorHandlerServlet.class);

    private static final String HTTP_ERROR_PATH = "/httpError";
    private static final String THROWABLE_PATH = "/throwable";

    private static final String HTTP_ERROR_TEMPLATE = "<!doctype html>" +
            "<html>" +
            "   <head>" +
            "      <title>Eclipse Kapua&trade; Console - ${statusCode}</title>" +
            "      <style>body {padding: 10px;}h1 {color: #1E80B0;}pre {display: none}.label {font-weight: bold;} .message {}</style>" +
            "   </head>" +
            "   <body>" +
            "      <div>" +
            "         <h1>Eclipse Kapua&trade;</h1>" +
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
    private static final String THROWABLE_ERROR_TEMPLATE = "<!doctype html>" +
            "<html>" +
            "   <head>" +
            "      <title>Eclipse Kapua&trade; Console - ${statusCode}</title>" +
            "      <style>body {padding: 10px;}h1 {color: #1E80B0;}pre {display: none}.label {font-weight: bold;} .message {}</style>" +
            "   </head>" +
            "   <body>" +
            "      <div>" +
            "         <h1>Eclipse Kapua&trade;</h1>" +
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        final String pathInfo = request.getPathInfo();
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

        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("statusCode", statusCode);
        data.put("requestUri", requestUri);
        data.put("errorMessage", errorMessage);

        response.getWriter().write(processTemplate(data, HTTP_ERROR_TEMPLATE));

        logger.error("Processed HTTP error! Code: {} - Request: {} - Error: {}", statusCode, requestUri, errorMessage);
    }

    private void processThrowable(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Getting parameters from the request
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");

        final Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        final String exceptionMessage;
        if (throwable != null) {
            exceptionMessage = ExceptionHelper.getMessage(throwable);
        } else {
            exceptionMessage = "Unknown";
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

        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("statusCode", statusCode);
        data.put("requestUri", requestUri);
        data.put("errorMessage", errorMessage);
        data.put("exceptionMessage", exceptionMessage);

        response.getWriter().write(processTemplate(data, THROWABLE_ERROR_TEMPLATE));

        logger.error("Processed HTTP error! Code: {} - Request: {} - Error: {}", statusCode, requestUri, errorMessage);
    }

    private static String processTemplate(Map<String, ?> properties, String template) {
        final ReplaceSource mapSource = StringReplacer.newSource(properties);
        return StringReplacer.replace(template, new ReplaceSource() {

            @Override
            public String replace(String context, String key) {
                return HtmlEscapers.htmlEscaper().escape(mapSource.replace(context, key));
            }
        }, StringReplacer.DEFAULT_PATTERN);
    }
}
