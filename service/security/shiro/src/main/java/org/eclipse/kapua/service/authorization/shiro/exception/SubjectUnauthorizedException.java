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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro.exception;

import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * Authorization exception.
 *
 * @since 1.0
 */
public class SubjectUnauthorizedException extends KapuaAuthorizationException {

    private static final String KAPUA_ERROR_MESSAGES = "kapua-service-error-messages";

    private Permission permission;

    public SubjectUnauthorizedException(Permission permission) {
        super(KapuaAuthorizationErrorCodes.SUBJECT_UNAUTHORIZED, null, permission);

        setPermission(permission);
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
