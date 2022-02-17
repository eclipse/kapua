/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        if (!CollectionUtils.isEmpty(permissions)) {
            if (getRealms()!=null && getRealms().size()==1) {
                return checkSingleRealm(principals, permissions);
            }
            else {
                return checkMultipleRealms(principals, permissions);
            }
        }
        return new boolean[permissions.size()];
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection principals, String... permissions) {
        return isPermitted(principals, Arrays.asList(permissions).stream()
                .map(permission -> getPermissionResolver().resolvePermission(permission))
                .collect(Collectors.toList()));
    }

    private boolean[] checkSingleRealm(PrincipalCollection principals, List<Permission> permissions) {
        Realm realm = getRealms().iterator().next();
        if (realm instanceof Authorizer) {
            return ((Authorizer) getRealms().iterator().next()).isPermitted(principals, permissions);
        }
        else {
            return new boolean[permissions.size()];
        }
    }

    private boolean[] checkMultipleRealms(PrincipalCollection principals, List<Permission> permissions) {
        boolean[] results = new boolean[permissions.size()];
        for (Realm realm : getRealms()) {
            if (realm instanceof Authorizer) {
                boolean allTrue = true;
                boolean[] resultTmp = ((Authorizer) realm).isPermitted(principals, permissions);
                for (int j=0; j<permissions.size(); j++) {
                    results[j] = results[j] || resultTmp[j];
                    allTrue = allTrue && results[j];
                }
                if (allTrue) {
                    break;
                }
            }
        }
        return results;
    }
}
