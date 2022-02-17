/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;

/**
 * {@link DeviceManagementRequestException} to {@code throw} when the {@link KapuaRequestChannel#getMethod()} is not mappable to any of the supported {@link KapuaMethod}s.
 *
 * @since 1.0.0
 */
public class DeviceManagementRequestBadMethodException extends DeviceManagementRequestException {

    private static final long serialVersionUID = 2544641159548768773L;

    private final KapuaMethod unmappedKapuaMethod;

    /**
     * Constructor
     *
     * @param unmappedKapuaMethod The unmapped {@link KapuaMethod}
     * @since 1.0.0
     */
    public DeviceManagementRequestBadMethodException(KapuaMethod unmappedKapuaMethod) {
        super(DeviceManagementErrorCodes.REQUEST_BAD_METHOD, unmappedKapuaMethod);

        this.unmappedKapuaMethod = unmappedKapuaMethod;
    }

    /**
     * Gets the unmapped {@link KapuaMethod}
     *
     * @return The unmapped {@link KapuaMethod}
     * @since 1.5.0
     */
    public KapuaMethod getUnmappedKapuaMethod() {
        return unmappedKapuaMethod;
    }
}
