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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.commons.util.ResourceUtils;

import com.google.common.html.HtmlEscapers;
import org.apache.commons.io.FileUtils;
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

    private static final String UNKNOWN = "Unknown";

    private static String httpErrorTemplate;
    private static String throwableErrorTemplate;

    private static String getHttpErrorTemplate() throws FileNotFoundException {
        if (httpErrorTemplate == null) {
            httpErrorTemplate = getTemplate("org/eclipse/kapua/app/console/core/servlet/http_error_template.html");
        }
        return httpErrorTemplate;
    }

    private static String getThrowableErrorTemplate() throws FileNotFoundException{
        if (throwableErrorTemplate == null) {
            throwableErrorTemplate = getTemplate("org/eclipse/kapua/app/console/core/servlet/http_error_template.html");
        }
        return throwableErrorTemplate;
    }

    private static String getTemplate(String s) throws FileNotFoundException{
        try {
            return FileUtils.readFileToString(new File(ResourceUtils.getResource(s).getFile()));
        } catch (IOException ioex) {
            throw new FileNotFoundException(s);
        } catch (NullPointerException npex) {
            throw new FileNotFoundException(s);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
            requestUri = UNKNOWN;
        }
        if (errorMessage == null) {
            errorMessage = "Internal Server Error";
        }

        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("statusCode", statusCode);
        data.put("requestUri", requestUri);
        data.put("errorMessage", errorMessage);

        try {
            response.getWriter().write(processTemplate(data, getHttpErrorTemplate()));
        } catch (FileNotFoundException ex) {
            logger.error("Error while parsing error template");
            response.sendError(500);
        }

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
            exceptionMessage = UNKNOWN;
        }

        // Defaulting them if null
        if (statusCode == null) {
            statusCode = 500;
        }
        if (requestUri == null) {
            requestUri = UNKNOWN;
        }
        if (errorMessage == null) {
            errorMessage = "Internal Server Error";
        }

        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("statusCode", statusCode);
        data.put("requestUri", requestUri);
        data.put("errorMessage", errorMessage);
        data.put("exceptionMessage", exceptionMessage);

        try {
            response.getWriter().write(processTemplate(data, getThrowableErrorTemplate()));
        } catch (FileNotFoundException ex) {
            logger.error("Error while parsing error template");
            response.sendError(500);
        }

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
