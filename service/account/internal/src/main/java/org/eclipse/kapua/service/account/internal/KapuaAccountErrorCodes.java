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
