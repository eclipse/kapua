/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Account error codes
 *
 * since 1.0
 *
 */
public enum KapuaAccountErrorCodes implements KapuaErrorCode {
    /**
     * Internal error
     */
    INTERNAL_ERROR,
    /**
     * Illegal argument
     */
    ILLEGAL_ARGUMENT,
    /**
     * Operation not allowed
     */
    OPERATION_NOT_ALLOWED;
}
