/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.shiro;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaUnauthenticatedException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;

/**
 * {@link AuthorizationService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public boolean[] isPermitted(List<Permission> permissions) throws KapuaException {
        KapuaSession session = KapuaSecurityUtils.getSession();

        if (session == null) {
            throw new KapuaUnauthenticatedException();
        }
        if (session.isTrustedMode()) {
            boolean[] returnedPermissions = new boolean[permissions.size()];
            Arrays.fill(returnedPermissions, true);
            return returnedPermissions;
        } else {
            List<org.apache.shiro.authz.Permission> permissionsShiro = permissions.stream()
                    .map(permission -> (org.apache.shiro.authz.Permission) permission)
                    .collect(Collectors.toList());
            return SecurityUtils.getSubject().isPermitted(permissionsShiro);
        }
    }

    @Override
    public boolean isPermitted(Permission permission)
            throws KapuaException {
        KapuaSession session = KapuaSecurityUtils.getSession();

        if (session == null) {
            throw new KapuaUnauthenticatedException();
        }

        return session.isTrustedMode() ||
                SecurityUtils.getSubject().isPermitted((org.apache.shiro.authz.Permission) permission);
    }

    @Override
    public void checkPermission(Permission permission)
            throws KapuaException {
        if (!isPermitted(permission)) {
            throw new SubjectUnauthorizedException(permission);
        }
    }

}
