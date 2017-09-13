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
import java.util.Collections;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExporterServlet extends HttpServlet {

    private static final long serialVersionUID = 226461063207179649L;
    private static Logger logger = LoggerFactory.getLogger(DataExporterServlet.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqPathInfo = request.getPathInfo();
        if (reqPathInfo != null) {
            response.sendError(404);
            return;
        }

        internalDoGet(request, response);
    }

    private void internalDoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String format = request.getParameter("format");
            String scopeIdString = request.getParameter("scopeIdString");
            String topic = request.getParameter("topic");
            String device = request.getParameter("device");
            String asset = request.getParameter("asset");
            String[] headers = request.getParameterValues("headers");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String sortField = request.getParameter("sortField");
            String sortDir = request.getParameter("sortDir");
            String topicOrDevice;

            AndPredicate predicate = new AndPredicateImpl();
            if (topic != null) {
                topicOrDevice = topic;
                predicate.getPredicates().add(STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(topic.replaceFirst("/#$", "")));
            } else if (device != null) {
                topicOrDevice = device;
                predicate.getPredicates().add(STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CLIENT_ID, device));
            } else if (asset != null) {
                topicOrDevice = device;
                predicate.getPredicates().add(STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CHANNEL, asset));
            } else {
                throw new IllegalArgumentException("topic, device");
            }

            if (startDate == null) {
                throw new IllegalArgumentException("startDate");
            }

            if (endDate == null) {
                throw new IllegalArgumentException("endDate");
            }

            if (headers == null) {
                throw new IllegalArgumentException("headers");
            }

            DataExporter dataExporter;
            if ("xls".equals(format)) {
                dataExporter = new DataExporterExcel(response, topicOrDevice);
            } else if ("csv".equals(format)) {
                dataExporter = new DataExporterCsv(response, topicOrDevice);
            } else {
                throw new IllegalArgumentException("format");
            }
            dataExporter.init(headers);
            KapuaLocator locator = KapuaLocator.getInstance();
            MessageStoreService messageService = locator.getService(MessageStoreService.class);
            MessageQuery query = new MessageQueryImpl(GwtKapuaCommonsModelConverter.convertKapuaId(scopeIdString));
            Date start = new Date(Long.valueOf(startDate));
            Date end = new Date(Long.valueOf(endDate));
            predicate.getPredicates().add(new RangePredicateImpl(MessageField.TIMESTAMP, start, end));

            if (sortField != null && sortDir != null) {
                query.setSortFields(Collections.singletonList(SortField.of(SortDirection.valueOf(sortDir), sortField)));
            }

            query.setPredicate(predicate);
            MessageListResult result;
            int offset = 0;
            query.setLimit(250);
            do {
                query.setOffset(offset);
                result = messageService.query(query);
                dataExporter.append(result.getItems());
                offset += result.getSize();
            } while (!result.isEmpty());
            dataExporter.close();
        } catch (IllegalArgumentException iae) {
            response.sendError(400, "Illegal value for query parameter(s): " + iae.getMessage());
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
            logger.error("Error creating data export", e);
            throw new ServletException(e);
        }
    }

}
