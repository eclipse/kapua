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
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceExporterExcel extends DeviceExporter {

    private static final Logger s_logger = LoggerFactory.getLogger(DeviceExporterExcel.class);

    private String m_account;

    private Workbook m_workbook;
    private Sheet m_sheet;
    private CellStyle m_dateStyle;
    private short m_rowCount;

    private static final int MAX_ROWS = 65535;
    private static final int MAX_CHAR = 32767;

    public DeviceExporterExcel(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void init(String account)
            throws ServletException, IOException {
        m_account = account;

        // workbook
        m_workbook = new HSSFWorkbook();
        m_sheet = m_workbook.createSheet();
        CreationHelper createHelper = m_workbook.getCreationHelper();
        m_dateStyle = m_workbook.createCellStyle();
        m_dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy hh:mm:ss.0"));

        // headers
        m_rowCount = 0;
        Row row = m_sheet.createRow(m_rowCount++);

        int iColCount = 0;
        for (String property : s_deviceProperties) {
            m_sheet.setColumnWidth(iColCount, 18 * 256);
            row.createCell(iColCount++).setCellValue(truncate(property));
        }
    }

    @Override
    public void append(KapuaListResult<Device> devices)
            throws ServletException, IOException {
        Row row = null;
        Cell cell = null;
        for (Device device : devices.getItems()) {

            int iColCount = 0;
            row = m_sheet.createRow(m_rowCount++);

            row.createCell(iColCount++).setCellValue(truncate(m_account));

            if (device.getClientId() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getClientId()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getStatus() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getStatus().name()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            row.createCell(iColCount++).setCellValue("");

            if (device.getCreatedOn() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(m_dateStyle);
                cell.setCellValue(device.getCreatedOn());
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getLastEvent() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(m_dateStyle);
                cell.setCellValue(device.getLastEvent().getReceivedOn());
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getLastEvent() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getLastEvent().getType()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            row.createCell(iColCount++).setCellValue("");

            if (device.getDisplayName() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getDisplayName()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getSerialNumber() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getSerialNumber()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getImei() != null) {
                row.createCell(iColCount++).setCellValue(device.getImei());
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getImsi() != null) {
                row.createCell(iColCount++).setCellValue(device.getImsi());
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getIccid() != null) {
                row.createCell(iColCount++).setCellValue(device.getIccid());
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getModelId() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getModelId()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getBiosVersion() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getBiosVersion()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getFirmwareVersion() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getFirmwareVersion()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getOsVersion() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getOsVersion()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getJvmVersion() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getJvmVersion()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            row.createCell(iColCount++).setCellValue("");

            row.createCell(iColCount++).setCellValue("");

            if (device.getApplicationIdentifiers() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getApplicationIdentifiers()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getAcceptEncoding() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getAcceptEncoding()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getCustomAttribute1() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getCustomAttribute1()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getCustomAttribute2() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getCustomAttribute2()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getCustomAttribute3() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getCustomAttribute3()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getCustomAttribute4() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getCustomAttribute4()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            if (device.getCustomAttribute5() != null) {
                row.createCell(iColCount++).setCellValue(truncate(device.getCustomAttribute5()));
            } else {
                row.createCell(iColCount++).setCellValue("");
            }

            row.createCell(iColCount++).setCellValue("");

            if (m_rowCount >= MAX_ROWS) {
                s_logger.warn("Truncated file at {} rows. Max rows limit reached.", MAX_ROWS - 1);
                return;
            }
        }
    }

    @Override
    public void close()
            throws ServletException, IOException {
        // Write the output
        m_response.setContentType("application/vnd.ms-excel");
        m_response.setHeader("Content-Disposition", "attachment; filename=" + m_account + "_devices.xls");
        m_response.setHeader("Cache-Control", "no-transform, max-age=0");

        m_workbook.write(m_response.getOutputStream());
    }

    private String truncate(String cellValue) {
        if (cellValue.length() > MAX_CHAR)
            return cellValue.substring(0, MAX_CHAR);
        else
            return cellValue;
    }
}
