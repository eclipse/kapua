/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.device.registry.Device;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceManagementException} to {@code throw} when a Device Management Application is disabled for a {@link Device}
 *
 * @since 2.0.0
 */
public class DeviceManagementApplicationDisabledException extends DeviceManagementException {

    private static final long serialVersionUID = 7480204887766596656L;

    private final String applicationName;

    /**
     * Constructor.
     *
     * @param applicationName The disabled Device Management Application.
     * @since 2.0.0
     */
    public DeviceManagementApplicationDisabledException(@NotNull String applicationName) {
        super(DeviceManagementErrorCodes.APPLICATION_DISABLED);

        this.applicationName = applicationName;
    }

    /**
     * Gets the disabled Device Management Application.
     *
     * @return The disabled Device Management Application.
     * @since 2.0.0
     */
    public String getApplicationName() {
        return applicationName;
    }
}
