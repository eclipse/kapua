/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaSerializable;

import javax.validation.constraints.NotNull;

public class DeviceManagementRequestException extends DeviceManagementException {

    private static final long serialVersionUID = 6369408505280888324L;

    private final KapuaSerializable kapuaSerializable;
    private final Object object;

    /**
     * Constructor.
     *
     * @param cause the root cause of the {@link Exception}.
     * @since 1.1.0
     */
    public DeviceManagementRequestException(@NotNull Throwable cause, @NotNull KapuaSerializable kapuaSerializable) {
        super(DeviceManagementRequestErrorCodes.REQUEST_EXCEPTION, cause, kapuaSerializable);
        this.kapuaSerializable = kapuaSerializable;
        this.object = null;
    }

    /**
     * Constructor.
     *
     * @param cause the root cause of the {@link Exception}.
     * @since 1.1.0
     */
    public DeviceManagementRequestException(@NotNull Throwable cause, @NotNull Object object) {
        super(DeviceManagementRequestErrorCodes.REQUEST_EXCEPTION, cause, object);
        this.kapuaSerializable = null;
        this.object = object;
    }

    /**
     * Gets the {@link KapuaSerializable}
     *
     * @return The {@link KapuaSerializable}
     * @since 1.1.0
     */
    public KapuaSerializable getKapuaSerializable() {
        return kapuaSerializable;
    }

    /**
     * Gets the {@link Object}
     *
     * @return The {@link Object}
     * @since 1.1.0
     */
    public Object getObject() {
        return object;
    }

}
