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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.service.datastore.model.DatastoreMessage;

import com.opencsv.CSVWriter;

public class DataExporterCsv extends DataExporter {

    private CSVWriter writer;
    private String topicOrDevice;
    private String[] headers;

    protected DataExporterCsv(HttpServletResponse response, String topicOrDevice) {
        super(response);
        this.topicOrDevice = topicOrDevice;
    }

    @Override
    public void init(String[] headers) throws ServletException, IOException {
        this.headers = headers;

        // set headers before calling getWriter()

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(topicOrDevice, "UTF-8") + "_data.csv");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        writer = new CSVWriter(response.getWriter());

        final String[] columns = new String[MANDATORY_COLUMNS.length + headers.length];
        int i = 0;

        for (final String column : MANDATORY_COLUMNS) {
            columns[i++] = column;
        }
        for (final String header : headers) {
            columns[i++] = header;
        }
        writer.writeNext(columns);
    }

    @Override
    public void append(List<DatastoreMessage> messages) throws ServletException, IOException {
        for (DatastoreMessage message : messages) {
            List<String> columns = new ArrayList<String>();
            columns.add(valueOf(message.getTimestamp()));
            columns.add(valueOf(message.getClientId()));

            if (message.getChannel() != null) {
                columns.add(message.getChannel().toPathString());
            } else {
                columns.add(BLANK);
            }

            if (message.getPayload() != null && message.getPayload().getMetrics() != null) {
                for (String header : headers) {
                    columns.add(valueOf(message.getPayload().getMetrics().get(header)));
                }
            }

            writer.writeNext(columns.toArray(new String[] {}));
        }
    }

    @Override
    public void append(String message) {
        writer.writeNext(new String[]{ message });
    }

    @Override
    public void close() throws ServletException, IOException {
        writer.flush();
        writer.close();
    }

}
