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

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.util.IOUtils;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.core.shared.model.KapuaFormFields;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.StringTokenizer;

public class FileServlet extends KapuaHttpServlet {

    private static final long serialVersionUID = -5016170117606322129L;
    private static final Logger logger = LoggerFactory.getLogger(FileServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String reqPathInfo = req.getPathInfo();
        if (reqPathInfo == null) {
            resp.sendError(404);
            return;
        }

        // BEGIN XSRF - Servlet dependent code
        KapuaFormFields kapuaFormFields = getFormFields(req);
        try {
            GwtXSRFToken token = new GwtXSRFToken(kapuaFormFields.get("xsrfToken"));
            KapuaRemoteServiceServlet.checkXSRFToken(req, token);
        } catch (Exception e) {
            throw new ServletException("Security error: please retry this operation correctly.", e);
        }
        // END XSRF security check

        if (reqPathInfo.equals("/command")) {
            doPostCommand(kapuaFormFields, resp);
        } else if (reqPathInfo.equals("/configuration/snapshot")) {
            doPostConfigurationSnapshot(kapuaFormFields, resp);
        } else {
            resp.sendError(404);
            return;
        }
    }

    private void doPostConfigurationSnapshot(KapuaFormFields kapuaFormFields, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<FileItem> fileItems = kapuaFormFields.getFileItems();
            String scopeIdString = kapuaFormFields.get("scopeIdString");
            String deviceIdString = kapuaFormFields.get("deviceIdString");

            if (scopeIdString == null || scopeIdString.isEmpty()) {
                throw new IllegalArgumentException("scopeIdString");
            }

            if (deviceIdString == null || deviceIdString.isEmpty()) {
                throw new IllegalArgumentException("deviceIdString");
            }

            if (fileItems == null || fileItems.size() != 1) {
                throw new IllegalArgumentException("configuration");
            }

            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceConfigurationManagementService deviceConfigurationManagementService = locator.getService(DeviceConfigurationManagementService.class);

            FileItem fileItem = fileItems.get(0);
            byte[] data = fileItem.get();
            String xmlConfigurationString = new String(data, "UTF-8");

            deviceConfigurationManagementService.put(KapuaEid.parseCompactId(scopeIdString),
                    KapuaEid.parseCompactId(deviceIdString),
                    xmlConfigurationString,
                    null);

        } catch (IllegalArgumentException iae) {
            resp.sendError(400, "Illegal value for query parameter: " + iae.getMessage());
            return;
        } catch (KapuaEntityNotFoundException eenfe) {
            resp.sendError(400, eenfe.getMessage());
            return;
        } catch (KapuaUnauthenticatedException eiae) {
            resp.sendError(401, eiae.getMessage());
            return;
        } catch (KapuaIllegalAccessException eiae) {
            resp.sendError(403, eiae.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Error posting configuration", e);
            throw new ServletException(e);
        }
    }

    private void doPostCommand(KapuaFormFields kapuaFormFields, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<FileItem> fileItems = kapuaFormFields.getFileItems();

            final String scopeIdString = kapuaFormFields.get("scopeIdString");
            final String deviceIdString = kapuaFormFields.get("deviceIdString");
            final String command = kapuaFormFields.get("command");
            final String password = kapuaFormFields.get("password");
            final String timeoutString = kapuaFormFields.get("timeout");

            if (scopeIdString == null || scopeIdString.isEmpty()) {
                throw new IllegalArgumentException("scopeId");
            }

            if (deviceIdString == null || deviceIdString.isEmpty()) {
                throw new IllegalArgumentException("deviceId");
            }

            if (command == null || command.isEmpty()) {
                throw new IllegalArgumentException("command");
            }

            if (fileItems.size() > 1) {
                throw new IllegalArgumentException("file");
            }

            Integer timeout = null;
            if (timeoutString != null && !timeoutString.isEmpty()) {
                try {
                    timeout = Integer.parseInt(timeoutString);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("timeout");
                }
            }

            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceCommandManagementService deviceService = locator.getService(DeviceCommandManagementService.class);

            // FIXME: set a max size on the MQtt payload
            byte[] data = fileItems.size() == 0 ? null : fileItems.get(0).get();

            DeviceCommandFactory deviceCommandFactory = locator.getFactory(DeviceCommandFactory.class);
            DeviceCommandInput commandInput = deviceCommandFactory.newCommandInput();

            StringTokenizer st = new StringTokenizer(command);
            int count = st.countTokens();

            String cmd = count > 0 ? st.nextToken() : null;
            String[] args = count > 1 ? new String[count - 1] : null;
            int i = 0;
            while (st.hasMoreTokens()) {
                args[i++] = st.nextToken();
            }

            commandInput.setCommand(cmd);
            commandInput.setPassword(password == null || password.isEmpty() ? null : password);
            commandInput.setArguments(args);
            commandInput.setTimeout(timeout);
            commandInput.setWorkingDir("/tmp/");
            commandInput.setBody(data);

            DeviceCommandOutput deviceCommandOutput = deviceService.exec(KapuaEid.parseCompactId(scopeIdString),
                    KapuaEid.parseCompactId(deviceIdString),
                    commandInput,
                    null);
            resp.setContentType("text/plain");
            if (deviceCommandOutput.getStderr() != null &&
                    deviceCommandOutput.getStderr().length() > 0) {
                resp.getWriter().println(deviceCommandOutput.getStderr());
            }
            if (deviceCommandOutput.getStdout() != null &&
                    deviceCommandOutput.getStdout().length() > 0) {
                resp.getWriter().println(deviceCommandOutput.getStdout());
            }
            if (deviceCommandOutput.getExceptionMessage() != null &&
                    deviceCommandOutput.getExceptionMessage().length() > 0) {
                resp.getWriter().println(deviceCommandOutput.getExceptionMessage());
            }

        } catch (IllegalArgumentException iae) {
            resp.sendError(400, "Illegal value for query parameter: " + iae.getMessage());
            return;
        } catch (KapuaEntityNotFoundException eenfe) {
            resp.sendError(400, eenfe.getMessage());
            return;
        } catch (KapuaUnauthenticatedException eiae) {
            resp.sendError(401, eiae.getMessage());
            return;
        } catch (KapuaIllegalAccessException eiae) {
            resp.sendError(403, eiae.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Error sending command", e);
            throw new ServletException(e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reqPathInfo = request.getPathInfo();

        if (reqPathInfo.startsWith("/icons")) {
            doGetIconResource(request, response);
        } else {
            response.sendError(404);
        }
    }

    private void doGetIconResource(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id");
        }

        if (!id.matches("[0-9A-Za-z]{1,}")) {
            throw new IllegalArgumentException("id");
        }

        String tmpPath = System.getProperty("java.io.tmpdir");

        StringBuilder filePathSb = new StringBuilder(tmpPath);

        if (!tmpPath.endsWith("/")) {
            filePathSb.append("/");
        }

        ConsoleSetting config = ConsoleSetting.getInstance();

        filePathSb.append(config.getString(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_FOLDER))
                .append("/")
                .append(id);

        File requestedFile = new File(filePathSb.toString());
        if (!requestedFile.exists()) {
            throw new IllegalArgumentException("id");
        }

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        OutputStream out = response.getOutputStream();
        IOUtils.copy(new FileInputStream(requestedFile), out);
    }
}

