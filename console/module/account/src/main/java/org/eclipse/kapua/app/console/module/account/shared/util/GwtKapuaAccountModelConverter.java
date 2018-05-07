/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.account.shared.util;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountPredicates;
import org.eclipse.kapua.service.account.AccountQuery;

public class GwtKapuaAccountModelConverter {

    private GwtKapuaAccountModelConverter() { }

    public static AccountQuery convertAccountQuery(PagingLoadConfig loadConfig, GwtAccountQuery gwtAccountQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountFactory factory = locator.getFactory(AccountFactory.class);
        AccountQuery query = factory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccountQuery.getScopeId()));
        AndPredicateImpl predicate = new AndPredicateImpl();

        if (gwtAccountQuery.getName() != null && !gwtAccountQuery.getName().trim().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(AccountPredicates.NAME, gwtAccountQuery.getName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationName() != null && !gwtAccountQuery.getOrganizationName().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(AccountPredicates.ORGANIZATION_NAME, gwtAccountQuery.getOrganizationName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationEmail() != null && !gwtAccountQuery.getOrganizationEmail().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(AccountPredicates.ORGANIZATION_EMAIL, gwtAccountQuery.getOrganizationEmail(), Operator.LIKE));
        }

        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? AccountPredicates.NAME : loadConfig.getSortField();
        if (sortField.equals("modifiedOnFormatted")) {
            sortField = AccountPredicates.MODIFIED_ON;
        } else if (sortField.equals("modifiedByName")) {
            sortField = AccountPredicates.MODIFIED_BY;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        query.setSortCriteria(sortCriteria);
        query.setOffset(loadConfig.getOffset());
        query.setLimit(loadConfig.getLimit());
        query.setPredicate(predicate);

        return query;
    }
}
