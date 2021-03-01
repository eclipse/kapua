/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventory;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;

/**
 * {@link DeviceInventory} {@link KapuaRequestChannel} implementation.
 *
 * @since 1.5.0
 */
public class InventoryRequestChannel extends KapuaRequestChannelImpl implements KapuaRequestChannel {

    private static final long serialVersionUID = 9127157971609776985L;
}
