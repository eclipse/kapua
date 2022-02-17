/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

/**
 * {@link DeviceManagementRequestException} to {@code throw} when the content of the request has any problem.
 *
 * @since 1.5.0
 */
public class DeviceManagementRequestContentException extends DeviceManagementRequestException {

    private static final long serialVersionUID = 6369408505280888324L;

    private final Object requestContent;

    /**
     * Constructor.
     *
     * @param cause          The root cause of the {@link Exception}.
     * @param requestContent The content that cannot be serialized.
     * @since 1.1.0
     */
    public DeviceManagementRequestContentException(@NotNull Throwable cause, @NotNull Object requestContent) {
        super(DeviceManagementErrorCodes.REQUEST_CONTENT, cause, requestContent);

        this.requestContent = requestContent;
    }

    /**
     * Gets the {@link KapuaSerializable}
     *
     * @return The {@link KapuaSerializable}
     * @since 1.1.0
     */
    public Object getRequestContent() {
        return requestContent;
    }

}
