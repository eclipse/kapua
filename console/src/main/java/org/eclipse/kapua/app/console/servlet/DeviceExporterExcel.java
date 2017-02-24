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
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
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

        AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
        Account account = null;
        try {
            account = accountService.find(KapuaEid.parseCompactId(m_account));
        } catch (KapuaException e) {
            KapuaException.internalError(e);
        }

        Row row;
        Cell cell;
        for (Device device : devices.getItems()) {

            int iColCount = 0;
            row = m_sheet.createRow(m_rowCount++);

            // Account id
            row.createCell(iColCount++).setCellValue(truncate(m_account));

            // Account name
            row.createCell(iColCount++).setCellValue(truncate(account.getName()));

            // Client Id
            row.createCell(iColCount++).setCellValue(device.getClientId() != null ? truncate(device.getClientId()) : BLANK);

            // Device status
            row.createCell(iColCount++).setCellValue(device.getStatus() != null ? truncate(device.getStatus().name()) : BLANK);

            // Device connection status
            row.createCell(iColCount++).setCellValue(device.getConnection() != null ? device.getConnection().getStatus().name() : BLANK);

            // Created on
            if (device.getCreatedOn() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(m_dateStyle);
                cell.setCellValue(device.getCreatedOn());
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
            }

            // Last event on
            if (device.getLastEvent() != null) {
                cell = row.createCell(iColCount++);
                cell.setCellStyle(m_dateStyle);
                cell.setCellValue(device.getLastEvent().getReceivedOn());
            } else {
                row.createCell(iColCount++).setCellValue(BLANK);
            }

            // Last event type
            row.createCell(iColCount++).setCellValue(device.getLastEvent() != null ? truncate(device.getLastEvent().getResource()) : BLANK);

            // Client ip
            row.createCell(iColCount++).setCellValue(device.getConnection() != null ? device.getConnection().getClientIp() : BLANK);

            // Display name
            row.createCell(iColCount++).setCellValue(device.getDisplayName() != null ? truncate(device.getDisplayName()) : BLANK);

            // Serial number
            row.createCell(iColCount++).setCellValue(device.getSerialNumber() != null ? truncate(device.getSerialNumber()) : BLANK);

            // Imei
            row.createCell(iColCount++).setCellValue(device.getImei() != null ? device.getImei() : BLANK);

            // Imsi
            row.createCell(iColCount++).setCellValue(device.getImsi() != null ? device.getImsi() : BLANK);

            // Iccid
            row.createCell(iColCount++).setCellValue(device.getIccid() != null ? device.getIccid() : BLANK);

            // Model id
            row.createCell(iColCount++).setCellValue(device.getModelId() != null ? truncate(device.getModelId()) : BLANK);

            // Bios version
            row.createCell(iColCount++).setCellValue(device.getBiosVersion() != null ? truncate(device.getBiosVersion()) : BLANK);

            // Firmware version
            row.createCell(iColCount++).setCellValue(device.getFirmwareVersion() != null ? truncate(device.getFirmwareVersion()) : BLANK);

            // Os version
            row.createCell(iColCount++).setCellValue(device.getOsVersion() != null ? truncate(device.getOsVersion()) : BLANK);

            // JVM version
            row.createCell(iColCount++).setCellValue(device.getJvmVersion() != null ? truncate(device.getJvmVersion()) : BLANK);

            // OSGi version
            row.createCell(iColCount++).setCellValue(device.getOsgiFrameworkVersion() != null ? truncate(device.getOsgiFrameworkVersion()) : BLANK);

            // Application framework version
            row.createCell(iColCount++).setCellValue(device.getApplicationFrameworkVersion() != null ? truncate(device.getApplicationFrameworkVersion()) : BLANK);

            // Application identifiers
            row.createCell(iColCount++).setCellValue(device.getApplicationIdentifiers() != null ? truncate(device.getApplicationIdentifiers()) : BLANK);

            // Accept encoding
            row.createCell(iColCount++).setCellValue(device.getAcceptEncoding() != null ? truncate(device.getAcceptEncoding()) : BLANK);

            // Custom attribute 1
            row.createCell(iColCount++).setCellValue(device.getCustomAttribute1() != null ? truncate(device.getCustomAttribute1()) : BLANK);

            // Custom attribute 2
            row.createCell(iColCount++).setCellValue(device.getCustomAttribute2() != null ? truncate(device.getCustomAttribute2()) : BLANK);

            // Custom attribute 3
            row.createCell(iColCount++).setCellValue(device.getCustomAttribute3() != null ? truncate(device.getCustomAttribute3()) : BLANK);

            // Custom attribute 4
            row.createCell(iColCount++).setCellValue(device.getCustomAttribute4() != null ? truncate(device.getCustomAttribute4()) : BLANK);

            // Custom attribute 5
            row.createCell(iColCount).setCellValue(device.getCustomAttribute5() != null ? truncate(device.getCustomAttribute5()) : BLANK);

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
        return cellValue.length() > MAX_CHAR ? cellValue.substring(0, MAX_CHAR) : cellValue;
    }
}
