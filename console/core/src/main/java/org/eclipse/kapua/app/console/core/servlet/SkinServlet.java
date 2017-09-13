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
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.servlet;

import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class SkinServlet extends HttpServlet {

    private static final long serialVersionUID = -5374075152873372059L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SkinServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FileReader fr = null;
        PrintWriter w = response.getWriter();
        String resourceName = request.getPathInfo();
        try {

            // check to see if we have an external resource directory configured
            ConsoleSetting consoleSetting = ConsoleSetting.getInstance();
            String resourceDir = consoleSetting.getString(ConsoleSettingKeys.SKIN_RESOURCE_DIR);
            if (resourceDir != null && resourceDir.trim().length() != 0) {

                File fResourceDir = new File(resourceDir);
                if (!fResourceDir.exists()) {
                    LOGGER.warn("Resource Directory {} does not exist", fResourceDir.getAbsolutePath());
                    return;
                }

                List<String> resourceFragments = Arrays.asList(resourceName.split("\\\\|/"));
                if (resourceFragments.contains("..")) {
                    LOGGER.warn("No directory traversing allowed; requested path is {}", resourceFragments);
                    return;
                }
                File fResourceFile = new File(fResourceDir, resourceName);
                if (!fResourceFile.exists()) {
                    LOGGER.warn("Resource File {} does not exist", fResourceFile.getAbsolutePath());
                    return;
                }

                // write the requested resource
                fr = new FileReader(fResourceFile);
                char[] buffer = new char[1024];
                int iRead = fr.read(buffer);
                while (iRead != -1) {
                    w.write(buffer, 0, iRead);
                    iRead = fr.read(buffer);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error loading skin resource", e);
        } finally {
            if (fr != null) {
                fr.close();
            }
            if (w != null) {
                w.close();
            }
        }
    }
}
