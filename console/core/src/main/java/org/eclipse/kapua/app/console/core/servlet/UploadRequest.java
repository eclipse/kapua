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
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class UploadRequest extends ServletFileUpload {

    private static Logger logger = LoggerFactory.getLogger(UploadRequest.class);

    private final Map<String, String> formFields;
    private final List<FileItem> fileItems;

    public UploadRequest(DiskFileItemFactory diskFileItemFactory) {
        super(diskFileItemFactory);
        setSizeMax(ConsoleSetting.getInstance().getLong(ConsoleSettingKeys.FILE_UPLOAD_SIZE_MAX));
        formFields = new HashMap<String, String>();
        fileItems = new ArrayList<FileItem>();
    }

    public void parse(HttpServletRequest req) throws FileUploadException {
        logger.debug("upload.getFileSizeMax(): {}", getFileSizeMax());
        logger.debug("upload.getSizeMax(): {}", getSizeMax());

        // Parse the request
        List<FileItem> items = null;
        items = parseRequest(req);

        // Process the uploaded items
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString();

                logger.debug("Form field item name: {}, value: {}", name, value);

                formFields.put(name, value);
            } else {
                String fieldName = item.getFieldName();
                String fileName = item.getName();
                String contentType = item.getContentType();
                boolean isInMemory = item.isInMemory();
                long sizeInBytes = item.getSize();

                logger.debug("File upload item name: {}, fileName: {}, contentType: {}, isInMemory: {}, size: {}",
                        new Object[] { fieldName, fileName, contentType, isInMemory, sizeInBytes });

                fileItems.add(item);
            }
        }
    }

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }
}
