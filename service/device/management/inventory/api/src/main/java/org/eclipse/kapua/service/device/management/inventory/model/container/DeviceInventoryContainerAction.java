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
package org.eclipse.kapua.service.device.management.inventory.model.container;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;

/**
 * {@link DeviceInventoryManagementService#execContainer(KapuaId, KapuaId, DeviceInventoryContainer, DeviceInventoryContainerAction, Long)} action.
 *
 * @since 2.0.0
 */
public enum DeviceInventoryContainerAction {

    /**
     * Action to start a {@link DeviceInventoryContainer}.
     *
     * @since 2.0.0
     */
    START,

    /**
     * Action to stop a {@link DeviceInventoryContainer}.
     *
     * @since 2.0.0
     */
    STOP
}
