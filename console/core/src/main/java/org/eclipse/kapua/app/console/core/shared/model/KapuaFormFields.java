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
package org.eclipse.kapua.app.console.core.shared.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

public class KapuaFormFields {

    Map<String, String> formFields;
    List<FileItem> fileItems;

    public KapuaFormFields() {
    }

    public KapuaFormFields(Map<String, String> formFields, List<FileItem> fileItems) {
        this.formFields = formFields;
        this.fileItems = fileItems;
    }

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public void setFormFields(Map<String, String> formFields) {
        this.formFields = formFields;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<FileItem> fileItems) {
        this.fileItems = fileItems;
    }

    public String get(Object key)
            throws IOException {
        String value = formFields.get(key);

        if (value != null) {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        }

        return value;
    }

}
