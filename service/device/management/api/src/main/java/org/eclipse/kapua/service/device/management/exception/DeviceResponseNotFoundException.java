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

public class DeviceResponseNotFoundException extends DeviceManagementException {

    private static final long serialVersionUID = 979552606398189720L;

    public DeviceResponseNotFoundException() {
        super(DeviceManagementResponseErrorCodes.RESPONSE_NOT_FOUND);
    }
}
