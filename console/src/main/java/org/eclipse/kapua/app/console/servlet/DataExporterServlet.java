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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExporterServlet extends HttpServlet {

    private static final long serialVersionUID = 226461063207179649L;
    private static Logger logger = LoggerFactory.getLogger(DataExporterServlet.class);

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
            String asset = request.getParameter("asset");
            String[] headers = request.getParameterValues("headers");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String topicOrAsset;

            AndPredicate predicate = new AndPredicateImpl();
            if (topic != null) {
                topicOrAsset = topic;
                predicate.getPredicates().add(new TermPredicateImpl(MessageField.CHANNEL, topic));
            } else if (asset != null) {
                topicOrAsset = asset;
                predicate.getPredicates().add(new TermPredicateImpl(MessageField.CLIENT_ID, asset));
            } else {
                throw new IllegalArgumentException("topic, asset");
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
                dataExporter = new DataExporterExcel(response, topicOrAsset);
            } else if ("csv".equals(format)) {
                dataExporter = new DataExporterCsv(response, topicOrAsset);
            } else {
                throw new IllegalArgumentException("format");
            }
            dataExporter.init(headers);
            KapuaLocator locator = KapuaLocator.getInstance();
            MessageStoreService messageService = locator.getService(MessageStoreService.class);
            MessageQuery query = new MessageQueryImpl(GwtKapuaModelConverter.convert(scopeIdString));
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            predicate.getPredicates().add(new RangePredicateImpl(MessageField.TIMESTAMP, start, end));
            query.setPredicate(predicate);
            MessageListResult result; 
            int offset = 0;
            query.setLimit(250);
            do{
                query.setOffset(offset);
                result = messageService.query(query);
                dataExporter.append(result.getItems());
                offset += result.getSize();
            }while (result.getSize() > 0);
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
