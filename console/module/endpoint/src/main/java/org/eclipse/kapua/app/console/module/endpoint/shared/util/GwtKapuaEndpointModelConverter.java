/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.shared.util;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoPredicates;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;

public class GwtKapuaEndpointModelConverter {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final EndpointInfoFactory ENDPOINT_FACTORY = LOCATOR.getFactory(EndpointInfoFactory.class);

    private GwtKapuaEndpointModelConverter() {
    }

    /**
     * Converts a {@link GwtEndpointQuery} into a {@link EndpointInfoQuery} object for backend usage
     *
     * @param loadConfig       the load configuration
     * @param gwtEndpointQuery the {@link GwtEndpointQuery} to convertKapuaId
     * @return the converted {@link EndpointInfoQuery}
     */
    public static EndpointInfoQuery convertEndpointQuery(PagingLoadConfig loadConfig, GwtEndpointQuery gwtEndpointQuery) {

        // Predicates conversion
        AndPredicateImpl andPredicate = new AndPredicateImpl();

        if (!Strings.isNullOrEmpty(gwtEndpointQuery.getSchema())) {
            andPredicate.and(new AttributePredicateImpl<String>(EndpointInfoPredicates.SCHEMA, gwtEndpointQuery.getSchema(), AttributePredicate.Operator.LIKE));
        }

        if (!Strings.isNullOrEmpty(gwtEndpointQuery.getDns())) {
            andPredicate.and(new AttributePredicateImpl<String>(EndpointInfoPredicates.DNS, gwtEndpointQuery.getDns(), AttributePredicate.Operator.LIKE));
        }

        if (gwtEndpointQuery.getPort() != null) {
            andPredicate.and(new AttributePredicateImpl<Integer>(EndpointInfoPredicates.SCHEMA, gwtEndpointQuery.getPort().intValue()));
        }

        // Sort order conversion
        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? EndpointInfoPredicates.DNS : loadConfig.getSortField();

        if (sortField.equals("schema")) {
            sortField = EndpointInfoPredicates.SCHEMA;
        }

        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);

        // Query conversion
        EndpointInfoQuery ednpointQuery = ENDPOINT_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtEndpointQuery.getScopeId()));
        ednpointQuery.setPredicate(andPredicate);
        ednpointQuery.setSortCriteria(sortCriteria);
        ednpointQuery.setOffset(loadConfig.getOffset());
        ednpointQuery.setLimit(loadConfig.getLimit());

        return ednpointQuery;
    }
}
