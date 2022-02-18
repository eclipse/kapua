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
package org.eclipse.kapua.service.device.management.inventory.model.bundle;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;

/**
 * {@link DeviceInventoryManagementService#execBundle(KapuaId, KapuaId, DeviceInventoryBundle, DeviceInventoryBundleAction, Long)} action.
 *
 * @since 1.5.0
 */
public enum DeviceInventoryBundleAction {

    /**
     * Action to start a {@link DeviceInventoryBundle}.
     *
     * @since 1.5.0
     */
    START,

    /**
     * Action to stop a {@link DeviceInventoryBundle}.
     *
     * @since 1.5.0
     */
    STOP
}
