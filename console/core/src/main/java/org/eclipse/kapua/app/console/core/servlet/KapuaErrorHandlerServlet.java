/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.servlet;

import com.google.common.base.Strings;
import com.google.common.html.HtmlEscapers;
import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.scada.utils.ExceptionHelper;
import org.eclipse.scada.utils.str.StringReplacer;
import org.eclipse.scada.utils.str.StringReplacer.ReplaceSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KapuaErrorHandlerServlet extends KapuaHttpServlet {

    private static final long serialVersionUID = 823090686760110256L;

    private static final Logger logger = LoggerFactory.getLogger(KapuaErrorHandlerServlet.class);

    private static final String HTTP_ERROR_PATH = "/httpError";
    private static final String THROWABLE_PATH = "/throwable";

    private static final String UNKNOWN = "Unknown";

    private static String httpErrorTemplate;
    private static String throwableErrorTemplate;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            processError(request, response);
        } catch (Exception e) {
            logger.warn("{} processing error! Request: {} - Error: {}", KapuaErrorHandlerServlet.class.getSimpleName(), request.getPathInfo(), e.getMessage(), e);
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doGet(request, response);
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
        processErrorWithTemplate(request, response, getHttpErrorTemplate());
    }

    private void processThrowable(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processErrorWithTemplate(request, response, getThrowableErrorTemplate());
    }

    private void processErrorWithTemplate(HttpServletRequest request, HttpServletResponse response, String template) throws IOException {

        // Status Code
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            statusCode = 0;
        }

        // Request URI
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (Strings.isNullOrEmpty(requestUri)) {
            requestUri = UNKNOWN;
        }

        // Error Message
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
        if (Strings.isNullOrEmpty(errorMessage)) {
            errorMessage = UNKNOWN;
        }

        // Exception Message
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        String exceptionMessage;
        if (throwable != null) {
            exceptionMessage = ExceptionHelper.getMessage(throwable);
        } else {
            exceptionMessage = UNKNOWN;
        }

        // Replace Parameters Map
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("statusCode", statusCode);
        data.put("requestUri", requestUri);
        data.put("errorMessage", errorMessage);
        data.put("exceptionMessage", exceptionMessage);

        try {
            response.getWriter().write(processTemplate(data, template));
        } catch (FileNotFoundException ex) {
            logger.error("Error while parsing error template: {}", ex.getMessage(), ex);
            response.sendError(500);
        }

        logger.warn("Processed HTTP error! Request: {} - Code: {} - Error: {}", requestUri, statusCode, errorMessage);
    }

    private static String getHttpErrorTemplate() throws FileNotFoundException {
        if (httpErrorTemplate == null) {
            httpErrorTemplate = getTemplate("org/eclipse/kapua/app/console/core/servlet/http_error_template.html");
        }
        return httpErrorTemplate;
    }

    private static String getThrowableErrorTemplate() throws FileNotFoundException {
        if (throwableErrorTemplate == null) {
            throwableErrorTemplate = getTemplate("org/eclipse/kapua/app/console/core/servlet/throwable_error_template.html");
        }
        return throwableErrorTemplate;
    }

    private static String getTemplate(String templateName) throws FileNotFoundException {
        try {
            return FileUtils.readFileToString(new File(ResourceUtils.getResource(templateName).getFile()));
        } catch (IOException ioex) {
            throw new FileNotFoundException(templateName);
        } catch (NullPointerException npex) {
            throw new FileNotFoundException(templateName);
        }
    }

    private static String processTemplate(Map<String, Object> properties, String template) {
        final ReplaceSource mapSource = StringReplacer.newSource(properties);
        return StringReplacer.replace(template, new ReplaceSource() {

            @Override
            public String replace(String context, String key) {
                return HtmlEscapers.htmlEscaper().escape(mapSource.replace(context, key));
            }
        }, StringReplacer.DEFAULT_PATTERN);
    }
}
