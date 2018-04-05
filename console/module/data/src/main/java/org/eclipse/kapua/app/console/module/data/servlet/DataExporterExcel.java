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
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExporterExcel extends DataExporter {

    private static final Logger logger = LoggerFactory.getLogger(DataExporterExcel.class);

    private String[] headers;
    private Workbook workbook;
    private Sheet sheet;
    private CellStyle dateStyle;
    private int rowCount;
    private String topicOrDevice;

    private static final int MAX_ROWS = 65535;
    private static final int MAX_CHAR = 32767;

    protected DataExporterExcel(HttpServletResponse response, String topicOrDevice) {
        super(response);
        this.topicOrDevice = topicOrDevice;
    }

    @Override
    public void init(String[] headers) throws ServletException, IOException {
        this.headers = headers;

        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy hh:mm:ss.0"));

        rowCount = 0;
        Row row = sheet.createRow(rowCount++);
        int iColCount = 0;
        for (String column : MANDATORY_COLUMNS) {
            sheet.setColumnWidth(iColCount, 18 * 256);
            row.createCell(iColCount++).setCellValue(truncate(column));
        }
        for (String header : headers) {
            sheet.setColumnWidth(iColCount, 18 * 256);
            row.createCell(iColCount++).setCellValue(truncate(header));
        }
    }

    @Override
    public void append(List<DatastoreMessage> messages) throws ServletException, IOException {
        Row row;
        Cell cell;
        for (DatastoreMessage message : messages) {
            int iColCount = 0;
            row = sheet.createRow(rowCount++);

            Date timestamp = message.getTimestamp();
            if (timestamp != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(dateStyle);
                cell.setCellValue(dateFormat.format(timestamp));
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
            }
            row.createCell(iColCount++).setCellValue(valueOf(message.getClientId()));

            String topic = BLANK;
            if (message.getChannel() != null) {
                List<String> semanticParts = message.getChannel().getSemanticParts();
                StringBuilder semanticTopic = new StringBuilder();
                for (int i = 0; i < semanticParts.size() - 1; i++) {
                    semanticTopic.append(semanticParts.get(i));
                    semanticTopic.append("/");
                }
                semanticTopic.append(semanticParts.get(semanticParts.size() - 1));
                topic = semanticTopic.toString();
            }
            row.createCell(iColCount++).setCellValue(valueOf(topic));
            if (message.getPayload() != null && message.getPayload().getMetrics() != null) {
                for (String header : headers) {
                    row.createCell(iColCount++).setCellValue(valueOf(message.getPayload().getMetrics().get(header)));
                }
            }
            if (rowCount >= MAX_ROWS) {
                logger.warn("Truncated file at {} rows. Max rows limit reached.", MAX_ROWS - 1);
                return;
            }
        }
    }

    @Override
    public void append(String message) {
        Row row = sheet.createRow(rowCount++);
        Cell cell = row.createCell(0);
        cell.setCellValue(message);
    }

    @Override
    public void close() throws ServletException, IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(topicOrDevice, "UTF-8") + "_data.xls");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        workbook.write(response.getOutputStream());
    }

    private String truncate(String cellValue) {
        return cellValue.length() > MAX_CHAR ? cellValue.substring(0, MAX_CHAR) : cellValue;
    }

}
