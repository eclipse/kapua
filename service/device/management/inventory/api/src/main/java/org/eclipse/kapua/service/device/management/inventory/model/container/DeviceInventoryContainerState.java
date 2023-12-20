/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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

/**
 * The {@link DeviceInventoryContainer} states.
 *
 * @since 2.0.0
 */
public enum DeviceInventoryContainerState {

    /**
     * The container is running.
     *
     * @since 2.0.0
     */
    ACTIVE,

    /**
     * The container is starting
     *
     * @since 2.0.0
     */
    INSTALLED,

    /**
     * Tontainer has failed, or is stopped
     *
     * @since 2.0.0
     */
    UNINSTALLED,

    /**
     * The container state can not be determined
     *
     * @since 2.0.0
     */
    UNKNOWN,
}
