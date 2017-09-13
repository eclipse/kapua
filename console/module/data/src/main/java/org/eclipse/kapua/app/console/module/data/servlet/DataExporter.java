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
package org.eclipse.kapua.app.console.module.data.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.service.datastore.model.DatastoreMessage;

public abstract class DataExporter {

    protected static final String BLANK = "";
    protected static final String[] MANDATORY_COLUMNS = { "Timestamp (UTC)", "Device", "Topic" };

    /*
     * As this is not multi-thread safe it must not be a static instance
     */
    protected final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    protected HttpServletResponse response;

    protected DataExporter(HttpServletResponse response) {
        this.response = response;
    }

    public abstract void init(String[] headers)
            throws ServletException, IOException;

    public abstract void append(List<DatastoreMessage> messages)
            throws ServletException, IOException;

    public abstract void close()
            throws ServletException, IOException;

    protected String valueOf(Object field) {
        if (field instanceof Date) {
            return field == null ? BLANK : dateFormat.format((Date) field);
        }
        return field == null ? BLANK : field.toString();
    }
}
