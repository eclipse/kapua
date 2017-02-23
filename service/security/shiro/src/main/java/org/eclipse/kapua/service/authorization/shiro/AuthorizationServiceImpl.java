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
package org.eclipse.kapua.service.authorization.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalStateException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * {@link AuthorizationService} implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public boolean isPermitted(Permission permission)
            throws KapuaException {
        boolean isPermitted = true;

        try {
            checkPermission(permission);
        } catch (AuthorizationException e) {
            isPermitted = false;
        }

        return isPermitted;
    }

    @Override
    public void checkPermission(Permission permission)
            throws KapuaException {
        KapuaSession session = KapuaSecurityUtils.getSession();

        // FIXME: this should throw something like unauthenticated exception
        if (session == null) {
            throw new KapuaIllegalStateException("null KapuaSession");
        }

        if (!session.isTrustedMode()) {
            Subject subject = SecurityUtils.getSubject();
            subject.checkPermission((org.apache.shiro.authz.Permission) permission);
        }
    }
}
