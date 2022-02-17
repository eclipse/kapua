/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.model;

import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;

/**
 * {@link DevicePackageOptions} definition.
 *
 * @since 1.1.0
 */
public interface DevicePackageOptions {

    /**
     * The {@link DeviceManagementOperation} timeout.
     *
     * @return The {@link DeviceManagementOperation} timeout.
     * @since 1.1.0
     */
    Long getTimeout();

    /**
     * Sets the {@link DeviceManagementOperation} timeout.
     *
     * @param timeout The {@link DeviceManagementOperation} timeout.
     * @since 1.1.0
     */
    void setTimeout(Long timeout);
}
