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

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.io.FileCleaningTracker;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.core.shared.model.KapuaFormFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 8120495078076069807L;
    private static final Logger logger = LoggerFactory.getLogger(KapuaHttpServlet.class);

    protected DiskFileItemFactory diskFileItemFactory;
    protected FileCleaningTracker fileCleaningTracker;

    @Override
    public void init() throws ServletException {
        super.init();

        logger.info("Servlet {} initialized", getServletName());

        ServletContext ctx = getServletContext();
        fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(ctx);

        int sizeThreshold = ConsoleSetting.getInstance().getInt(ConsoleSettingKeys.FILE_UPLOAD_INMEMORY_SIZE_THRESHOLD);
        File repository = new File(System.getProperty("java.io.tmpdir"));

        logger.info("DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD: {}", DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        logger.info("DiskFileItemFactory: using size threshold of: {}", sizeThreshold);

        diskFileItemFactory = new DiskFileItemFactory(sizeThreshold, repository);
        diskFileItemFactory.setFileCleaningTracker(fileCleaningTracker);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("Servlet {} destroyed", getServletName());

        if (fileCleaningTracker != null) {
            logger.info("Number of temporary files tracked: " + fileCleaningTracker.getTrackCount());
        }
    }

    public KapuaFormFields getFormFields(HttpServletRequest req)
            throws ServletException {
        UploadRequest upload = new UploadRequest(diskFileItemFactory);

        try {
            upload.parse(req);
        } catch (FileUploadException e) {
            logger.error("Error parsing the provision request", e);
            throw new ServletException("Error parsing the provision request", e);
        }

        return new KapuaFormFields(upload.getFormFields(), upload.getFileItems());
    }
}
