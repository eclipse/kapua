/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.security;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom {@link Authorizer} to reduce the query amount using the isPermitted method with the Permission list or String array.
 * To use this Authorizer a deeply modified shiro.ini is needed.
 * Without these changes this Authorizer will not have any realm configured. (see shiro.ini for explanation)
 * This authorizer takes the first valid configured realm and return the isPermitted evaluation skipping any aggregation strategy if more than one valid aggregator is defined.
 *
 */
public class EnhModularRealmAuthorizer extends ModularRealmAuthorizer {

    protected static final Logger logger = LoggerFactory.getLogger(EnhModularRealmAuthorizer.class);

    public EnhModularRealmAuthorizer() {
    }

    public EnhModularRealmAuthorizer(Collection<Realm> realms) {
        super(realms);
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection principals, List<Permission> permissions) {
        assertRealmsConfigured();
        if (CollectionUtils.isEmpty(permissions)) {
            //return the first realm result
            //the multiple realms case with aggregator should be handled or do we still have just one realm?
            for (Realm realm : getRealms()) {
                return ((Authorizer) realm).isPermitted(principals, permissions);
            }
        }
        return new boolean[0];
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection principals, String... permissions) {
        assertRealmsConfigured();
        if (permissions != null && permissions.length>0) {
            //return the first realm result
            //the multiple realms case with aggregator should be handled or do we still have just one realm?
            for (Realm realm : getRealms()) {
                return ((Authorizer) realm).isPermitted(principals, permissions);
            }
        }
        return new boolean[0];
    }

}