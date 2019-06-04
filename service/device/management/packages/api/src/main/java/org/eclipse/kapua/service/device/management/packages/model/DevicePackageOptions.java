/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
