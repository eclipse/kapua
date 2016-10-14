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
package org.eclipse.kapua.service.device.management.commons.exception;

import org.eclipse.kapua.KapuaErrorCode;

public enum DeviceManagementErrorCodes implements KapuaErrorCode
{
    REQUEST_EXCEPTION,
    REQUEST_BAD_METHOD,

    RESPONSE_PARSE_EXCEPTION,

    RESPONSE_BAD_REQUEST,
    RESPONSE_NOT_FOUND,
    RESPONSE_INTERNAL_ERROR;
}
