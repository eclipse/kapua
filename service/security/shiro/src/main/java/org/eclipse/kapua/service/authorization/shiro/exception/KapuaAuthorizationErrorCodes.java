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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Authorization error codes
 * <p>
 * since 1.0
 */
public enum KapuaAuthorizationErrorCodes implements KapuaErrorCode {
    /**
     * Invalid string permission representation
     */
    INVALID_STRING_PERMISSION,

    /**
     * Subject does not have the required {@link org.eclipse.kapua.service.authorization.permission.Permission}
     */
    SUBJECT_UNAUTHORIZED,

    /**
     * One or more embedded entities is not found or does not match the scopeId of the root entity
     */
    ENTITY_SCOPE_MISSMATCH
}
