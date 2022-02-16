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
package org.eclipse.kapua.service.device.management.inventory.internal.message;

import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainers;

/**
 * {@link DeviceInventoryContainers} {@link InventoryRequestMessage} implementation.
 *
 * @since 2.0.0
 */
public abstract class InventoryContainerExecRequestMessage extends InventoryRequestMessage<InventoryContainerExecRequestMessage> {

    private static final long serialVersionUID = 3593350285989405174L;

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public InventoryContainerExecRequestMessage() {
        super(InventoryContainerExecRequestMessage.class);
    }
}
