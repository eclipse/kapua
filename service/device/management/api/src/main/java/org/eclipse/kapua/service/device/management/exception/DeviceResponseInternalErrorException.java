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
package org.eclipse.kapua.service.device.management.exception;

public class DeviceResponseInternalErrorException extends DeviceManagementException {

    private static final long serialVersionUID = -6932782062611595148L;

    public DeviceResponseInternalErrorException() {
        super(DeviceManagementResponseErrorCodes.RESPONSE_INTERNAL_ERROR);
    }
}
