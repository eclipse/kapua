/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

public enum DeviceMenagementErrorCodes implements KapuaErrorCode {

    /**
     * The device was never connected
     */
    DEVICE_NEVER_CONNECTED,

    /**
     * The device is not connected
     */
    DEVICE_NOT_CONNECTED,

    /**
     * Bad request method
     */
    REQUEST_BAD_METHOD
}
