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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceExporterServlet extends HttpServlet {

    private static final long serialVersionUID = -2533869595709953567L;
    private static final Logger logger = LoggerFactory.getLogger(DeviceExporterServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reqPathInfo = request.getPathInfo();
        if (reqPathInfo != null) {
            response.sendError(404);
            return;
        }

        internalDoGet(request, response);
    }

    private void internalDoGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // parameter extraction
            String format = request.getParameter("format");
            String scopeIdString = request.getParameter("scopeIdString");

            // data exporter
            DeviceExporter deviceExporter;
            if ("xls".equals(format)) {
                deviceExporter = new DeviceExporterExcel(response);
            } else if ("csv".equals(format)) {
                deviceExporter = new DeviceExporterCsv(response);
            } else {
                throw new IllegalArgumentException("format");
            }

            if (scopeIdString == null || scopeIdString.isEmpty()) {
                throw new IllegalArgumentException("scopeIdString");
            }

            deviceExporter.init(scopeIdString);

            //
            // get the devices and append them to the exporter
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceRegistryService drs = locator.getService(DeviceRegistryService.class);
            DeviceFactory drf = locator.getFactory(DeviceFactory.class);

            int offset = 0;

            // paginate through the matching message
            DeviceQuery dq = drf.newQuery(KapuaEid.parseCompactId(scopeIdString));
            dq.setLimit(250);

            // Inserting filter parameter if specified
            AndPredicateImpl andPred = new AndPredicateImpl();

            String clientId = request.getParameter("clientId");
            if (clientId != null && !clientId.isEmpty()) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.CLIENT_ID, clientId, Operator.STARTS_WITH));
            }

            String displayName = request.getParameter("displayName");
            if (displayName != null && !displayName.isEmpty()) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.DISPLAY_NAME, displayName, Operator.STARTS_WITH));
            }

            String serialNumber = request.getParameter("serialNumber");
            if (serialNumber != null && !serialNumber.isEmpty()) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.SERIAL_NUMBER, serialNumber));
            }

            String deviceStatus = request.getParameter("deviceStatus");
            if (deviceStatus != null && !deviceStatus.isEmpty()) {
                andPred = andPred.and(new AttributePredicateImpl<DeviceStatus>(DevicePredicates.STATUS, DeviceStatus.valueOf(deviceStatus)));
            }

            String iotFrameworkVersion = request.getParameter("esfVersion");
            if (iotFrameworkVersion != null) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.APPLICATION_FRAMEWORK_VERSION, iotFrameworkVersion));
            }

            String applicationIdentifiers = request.getParameter("applicationIdentifiers");
            if (applicationIdentifiers != null) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.APPLICATION_IDENTIFIERS, applicationIdentifiers, Operator.LIKE));
            }

            String customAttribute1 = request.getParameter("customAttribute1");
            if (customAttribute1 != null) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.CUSTOM_ATTRIBUTE_1, customAttribute1));
            }

            String customAttribute2 = request.getParameter("customAttribute2");
            if (customAttribute2 != null) {
                andPred = andPred.and(new AttributePredicateImpl<String>(DevicePredicates.CUSTOM_ATTRIBUTE_2, customAttribute2));
            }

            String deviceConnectionStatus = request.getParameter("deviceConnectionStatus");
            if (deviceConnectionStatus != null) {
                andPred = andPred.and(new AttributePredicateImpl<DeviceConnectionStatus>(DevicePredicates.CONNECTION_STATUS, DeviceConnectionStatus.valueOf(deviceConnectionStatus)));
            }

            String sortAttribute = request.getParameter("sortAttribute");
            if (sortAttribute != null && !sortAttribute.isEmpty()) {

                String sortOrderString = request.getParameter("sortOrder");
                SortOrder sortOrder;
                if (sortOrderString != null && !sortOrderString.isEmpty()) {
                    sortOrder = SortOrder.valueOf(sortOrderString);
                } else {
                    sortOrder = SortOrder.ASCENDING;
                }

                if (sortAttribute.compareTo("CLIENT_ID") == 0) {
                    dq.setSortCriteria(new FieldSortCriteria(DevicePredicates.CLIENT_ID, sortOrder));
                } else if (sortAttribute.compareTo("DISPLAY_NAME") == 0) {
                    dq.setSortCriteria(new FieldSortCriteria(DevicePredicates.DISPLAY_NAME, sortOrder));
                } else if (sortAttribute.compareTo("LAST_EVENT_ON") == 0) {
                    dq.setSortCriteria(new FieldSortCriteria(DevicePredicates.LAST_EVENT_ON, sortOrder));
                }
            }

            dq.setPredicate(andPred);

            KapuaListResult<Device> results;
            do {
                dq.setOffset(offset);
                results = drs.query(dq);

                deviceExporter.append(results);

                offset += results.getSize();
                results.getSize();
            } while (results.getSize() > 0);

            // Close things up
            deviceExporter.close();
        } catch (IllegalArgumentException iae) {
            logger.info("Failed to export", iae);
            response.sendError(400, "Illegal value for query parameter: " + iae.getMessage());
            return;
        } catch (KapuaEntityNotFoundException eenfe) {
            response.sendError(400, eenfe.getMessage());
            return;
        } catch (KapuaUnauthenticatedException eiae) {
            response.sendError(401, eiae.getMessage());
            return;
        } catch (KapuaIllegalAccessException eiae) {
            response.sendError(403, eiae.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Error creating device export", e);
            throw new ServletException(e);
        }
    }
}
