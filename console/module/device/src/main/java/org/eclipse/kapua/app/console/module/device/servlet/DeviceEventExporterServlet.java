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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceEventExporterServlet extends HttpServlet {

    private static final long serialVersionUID = -2533869595709953567L;
    private static final Logger logger = LoggerFactory.getLogger(DeviceEventExporterServlet.class);

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
            String scopeId = request.getParameter("scopeId");
            String deviceId = request.getParameter("deviceId");
            Date startDate = new Date(Long.parseLong(request.getParameter("startDate")));
            Date endDate = new Date(Long.parseLong(request.getParameter("endDate")));

            // data exporter
            DeviceEventExporter deviceEventExporter;
            if ("xls".equals(format)) {
                deviceEventExporter = new DeviceEventExporterExcel(response);
            } else if ("csv".equals(format)) {
                deviceEventExporter = new DeviceEventExporterCsv(response);
            } else {
                throw new IllegalArgumentException("format");
            }

            if (scopeId == null || scopeId.isEmpty()) {
                throw new IllegalArgumentException("scopeId");
            }

            if (deviceId == null || deviceId.isEmpty()) {
                throw new IllegalArgumentException("deviceId");
            }

            deviceEventExporter.init(scopeId);

            //
            // get the devices and append them to the exporter
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceEventService des = locator.getService(DeviceEventService.class);
            DeviceEventFactory def = locator.getFactory(DeviceEventFactory.class);

            int offset = 0;

            // paginate through the matching message
            DeviceEventQuery deq = def.newQuery(KapuaEid.parseCompactId(scopeId));
            deq.setLimit(250);

            // Inserting filter parameter if specified
            AndPredicateImpl andPred = new AndPredicateImpl();

            andPred = andPred.and(new AttributePredicateImpl<KapuaId>(DeviceEventPredicates.DEVICE_ID, KapuaEid.parseCompactId(deviceId), Operator.EQUAL))
                    .and(new AttributePredicateImpl<Date>(DeviceEventPredicates.RECEIVED_ON, startDate, Operator.GREATER_THAN))
                    .and(new AttributePredicateImpl<Date>(DeviceEventPredicates.RECEIVED_ON, endDate, Operator.LESS_THAN));

            deq.setPredicate(andPred);

            KapuaListResult<DeviceEvent> results;
            do {
                deq.setOffset(offset);
                results = des.query(deq);

                deviceEventExporter.append(results);

                offset += results.getSize();
                results.getSize();
            } while (results.getSize() > 0);

            // Close things up
            deviceEventExporter.close();
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
