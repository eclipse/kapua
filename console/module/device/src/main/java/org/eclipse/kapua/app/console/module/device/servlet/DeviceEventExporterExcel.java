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
package org.eclipse.kapua.app.console.module.device.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceEventExporterExcel extends DeviceEventExporter {

    private static final Logger logger = LoggerFactory.getLogger(DeviceEventExporterExcel.class);

    private String scopeId;
    private String accountName;

    private Workbook workbook;
    private Sheet sheet;
    private CellStyle dateStyle;
    private short rowCount;

    private static final int MAX_ROWS = 65535;
    private static final int MAX_CHAR = 32767;

    public DeviceEventExporterExcel(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(String scopeId)
            throws ServletException, IOException {
        this.scopeId = scopeId;

        AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
        Account account = null;
        try {
            account = accountService.find(KapuaEid.parseCompactId(scopeId));
            accountName = account.getName();
        } catch (KapuaException e) {
            KapuaException.internalError(e);
        }

        // workbook
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy hh:mm:ss.0"));

        // headers
        rowCount = 0;
        Row row = sheet.createRow(rowCount++);

        int iColCount = 0;
        for (String property : DEVICE_PROPERTIES) {
            sheet.setColumnWidth(iColCount, 18 * 256);
            row.createCell(iColCount++).setCellValue(truncate(property));
        }
    }

    @Override
    public void append(KapuaListResult<DeviceEvent> deviceEvents)
            throws ServletException, IOException {

        Row row;
        Cell cell;
        for (DeviceEvent deviceEvent : deviceEvents.getItems()) {

            int iColCount = 0;
            row = sheet.createRow(rowCount++);

            // Account id
            row.createCell(iColCount++).setCellValue(scopeId != null ? truncate(scopeId) : BLANK);

            // Account name
            row.createCell(iColCount++).setCellValue(accountName != null ? truncate(accountName) : BLANK);

            // Event Id
            row.createCell(iColCount++).setCellValue(deviceEvent.getId() != null ? truncate(deviceEvent.getId().toCompactId()) : BLANK);

            // Created on
            if (deviceEvent.getCreatedOn() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(dateStyle);
                cell.setCellValue(deviceEvent.getCreatedOn());
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
            }

            // Created by
            row.createCell(iColCount++).setCellValue(deviceEvent.getCreatedBy() != null ? truncate(deviceEvent.getCreatedBy().toCompactId()) : BLANK);

            // Device Id
            row.createCell(iColCount++).setCellValue(deviceEvent.getDeviceId() != null ? truncate(deviceEvent.getDeviceId().toCompactId()) : BLANK);

            // Received On
            if (deviceEvent.getReceivedOn() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(dateStyle);
                cell.setCellValue(deviceEvent.getReceivedOn());
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
            }

            // Sent On
            if (deviceEvent.getSentOn() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(dateStyle);
                cell.setCellValue(deviceEvent.getSentOn());
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
            }

            if (deviceEvent.getPosition() != null) {
                KapuaPosition eventPosition = deviceEvent.getPosition();

                // Position Longitude
                row.createCell(iColCount++).setCellValue(eventPosition.getLongitude() != null ? truncate(eventPosition.getLongitude().toString()) : BLANK);

                // Position Latitude
                row.createCell(iColCount++).setCellValue(eventPosition.getLatitude() != null ? truncate(eventPosition.getLatitude().toString()) : BLANK);

                // Position Altitude
                row.createCell(iColCount++).setCellValue(eventPosition.getAltitude() != null ? truncate(eventPosition.getAltitude().toString()) : BLANK);

                // Position Precision
                row.createCell(iColCount++).setCellValue(eventPosition.getPrecision() != null ? truncate(eventPosition.getPrecision().toString()) : BLANK);

                // Position Heading
                row.createCell(iColCount++).setCellValue(eventPosition.getHeading() != null ? truncate(eventPosition.getHeading().toString()) : BLANK);

                // Position Speed
                row.createCell(iColCount++).setCellValue(eventPosition.getSpeed() != null ? truncate(eventPosition.getSpeed().toString()) : BLANK);

                // Position Timestamp
                row.createCell(iColCount++).setCellValue(eventPosition.getTimestamp() != null ? truncate(eventPosition.getTimestamp().toString()) : BLANK);

                // Position Satellites
                row.createCell(iColCount++).setCellValue(eventPosition.getSatellites() != null ? truncate(eventPosition.getSatellites().toString()) : BLANK);

                // Position Status
                row.createCell(iColCount++).setCellValue(eventPosition.getStatus() != null ? truncate(eventPosition.getStatus().toString()) : BLANK);
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
                row.createCell(iColCount++).setCellValue(BLANK);
            }

            // Resource
            row.createCell(iColCount++).setCellValue(deviceEvent.getResource() != null ? truncate(deviceEvent.getResource()) : BLANK);

            // Action
            row.createCell(iColCount++).setCellValue(deviceEvent.getAction() != null ? truncate(deviceEvent.getAction().toString()) : BLANK);

            // Response Code
            row.createCell(iColCount++).setCellValue(deviceEvent.getResponseCode() != null ? truncate(deviceEvent.getResponseCode().toString()) : BLANK);

            // Event Message
            row.createCell(iColCount++).setCellValue(deviceEvent.getEventMessage() != null ? truncate(deviceEvent.getEventMessage()) : BLANK);

            if (rowCount >= MAX_ROWS) {
                logger.warn("Truncated file at {} rows. Max rows limit reached.", MAX_ROWS - 1);
                return;
            }
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        // Write the output
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + accountName + "_devices.xls");
        response.setHeader("Cache-Control", "no-transform, max-age=0");

        workbook.write(response.getOutputStream());
    }

    private String truncate(String cellValue) {
        return cellValue.length() > MAX_CHAR ? cellValue.substring(0, MAX_CHAR) : cellValue;
    }
}
