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
package org.eclipse.kapua.app.console.module.account.shared.util;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountQuery;

public class GwtKapuaAccountModelConverter {

    private GwtKapuaAccountModelConverter() { }

    public static AccountQuery convertAccountQuery(PagingLoadConfig loadConfig, GwtAccountQuery gwtAccountQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountFactory factory = locator.getFactory(AccountFactory.class);
        AccountQuery query = factory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtAccountQuery.getScopeId()));
        AndPredicate predicate = new AndPredicate();

        if (gwtAccountQuery.getName() != null && !gwtAccountQuery.getName().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("name", gwtAccountQuery.getName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationName() != null && !gwtAccountQuery.getOrganizationName().isEmpty()) {
            predicate.and(new AttributePredicate<String>("organization.name", gwtAccountQuery.getOrganizationName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationEmail() != null && !gwtAccountQuery.getOrganizationEmail().isEmpty()) {
            predicate.and(new AttributePredicate<String>("organization.email", gwtAccountQuery.getOrganizationEmail(), Operator.LIKE));
        }

        query.setPredicate(predicate);

        return query;
    }
}
