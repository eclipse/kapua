/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.permission.shiro;

import java.math.BigInteger;
import java.util.StringTokenizer;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizationErrorCodes;
import org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizationException;

/**
 * Permission factory service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class PermissionFactoryImpl implements PermissionFactory {

    @Override
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId) {
        return newPermission(domain, action, targetScopeId, null);
    }

    @Override
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId, KapuaId groupId) {
        return new PermissionImpl(domain != null ? domain.getName() : null, action, targetScopeId, groupId);
    }

    @Override
    public RolePermission newRolePermission(KapuaId scopeId, Permission permission) {
        return new RolePermissionImpl(scopeId, permission);
    }

    @Override
    public Permission parseString(String stringPermission)
            throws KapuaException {
        StringTokenizer st = new StringTokenizer(stringPermission, Permission.SEPARATOR);
        int iTokensCount = st.countTokens();
        if (iTokensCount < 1 || iTokensCount > 4) {
            throw new KapuaAuthorizationException(KapuaAuthorizationErrorCodes.INVALID_STRING_PERMISSION, null, stringPermission);
        }

        //
        // Build the new Permission
        String domain = st.nextToken();

        Actions action = null;
        if (iTokensCount > 1) {
            String permissionToken = st.nextToken();
            if (!Permission.WILDCARD.equals(permissionToken)) {
                action = Actions.valueOf(permissionToken);
            }
        }

        KapuaId scopeTargetId = null;
        if (iTokensCount > 2) {
            String permissionToken = st.nextToken();
            if (!Permission.WILDCARD.equals(permissionToken)) {
                try {
                    BigInteger kapuaId = new BigInteger(permissionToken);
                    scopeTargetId = new KapuaEid(kapuaId);
                } catch (IllegalArgumentException iae) {
                    throw new KapuaAuthorizationException(KapuaAuthorizationErrorCodes.INVALID_STRING_PERMISSION, iae, stringPermission);
                }
            }
        }

        KapuaId groupId = null;
        if (iTokensCount > 3) {
            String permissionToken = st.nextToken();
            if (!Permission.WILDCARD.equals(permissionToken)) {
                try {
                    BigInteger kapuaId = new BigInteger(permissionToken);
                    groupId = new KapuaEid(kapuaId);
                } catch (IllegalArgumentException iae) {
                    throw new KapuaAuthorizationException(KapuaAuthorizationErrorCodes.INVALID_STRING_PERMISSION, iae, stringPermission);
                }
            }
        }

        return new PermissionImpl(domain, action, scopeTargetId, groupId);
    }

}
