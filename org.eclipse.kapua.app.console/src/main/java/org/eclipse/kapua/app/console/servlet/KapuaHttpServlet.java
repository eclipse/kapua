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

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.io.FileCleaningTracker;
import org.eclipse.kapua.app.console.ConsoleJAXBContextProvider;
import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.shared.model.KapuaFormFields;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaHttpServlet extends HttpServlet
{
    private static final long     serialVersionUID = 8120495078076069807L;
    private static Logger         s_logger         = LoggerFactory.getLogger(KapuaHttpServlet.class);

    protected DiskFileItemFactory m_diskFileItemFactory;
    protected FileCleaningTracker m_fileCleaningTracker;

    @Override
    public void init()
        throws ServletException
    {
        super.init();

        s_logger.info("Servlet {} initialized", getServletName());

        ServletContext ctx = getServletContext();
        m_fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(ctx);

        int sizeThreshold = ConsoleSetting.getInstance().getInt(ConsoleSettingKeys.FILE_UPLOAD_INMEMORY_SIZE_THRESHOLD);
        File repository = new File(System.getProperty("java.io.tmpdir"));

        s_logger.info("DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD: {}", DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        s_logger.info("DiskFileItemFactory: using size threshold of: {}", sizeThreshold);

        m_diskFileItemFactory = new DiskFileItemFactory(sizeThreshold, repository);
        m_diskFileItemFactory.setFileCleaningTracker(m_fileCleaningTracker);

        JAXBContextProvider consoleProvider = new ConsoleJAXBContextProvider();
        XmlUtil.setContextProvider(consoleProvider);
    }

    @Override
    public void destroy()
    {
        super.destroy();
        s_logger.info("Servlet {} destroyed", getServletName());

        if (m_fileCleaningTracker != null) {
            s_logger.info("Number of temporary files tracked: " + m_fileCleaningTracker.getTrackCount());
        }
    }

    public KapuaFormFields getFormFields(HttpServletRequest req)
        throws ServletException
    {
        UploadRequest upload = new UploadRequest(m_diskFileItemFactory);

        try {
            upload.parse(req);
        }
        catch (FileUploadException e) {
            s_logger.error("Error parsing the provision request", e);
            throw new ServletException("Error parsing the provision request", e);
        }

        return new KapuaFormFields(upload.getFormFields(), upload.getFileItems());
    }
}
