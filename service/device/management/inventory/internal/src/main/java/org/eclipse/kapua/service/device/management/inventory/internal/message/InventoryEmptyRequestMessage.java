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
package org.eclipse.kapua.service.device.management.inventory.internal.message;

/**
 * Emptry {@link InventoryRequestMessage} implementation.
 *
 * @since 1.5.0
 */
public abstract class InventoryEmptyRequestMessage extends InventoryRequestMessage<InventoryEmptyRequestMessage> {

    private static final long serialVersionUID = 3593350285989405174L;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public InventoryEmptyRequestMessage() {
        super(InventoryEmptyRequestMessage.class);
    }
}
