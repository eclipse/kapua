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
package org.eclipse.kapua.service.device.connection.listener;

import org.eclipse.kapua.event.ServiceEventBusListener;
import org.eclipse.kapua.service.KapuaService;

public interface DeviceConnectionEventListenerService extends KapuaService, ServiceEventBusListener {

    public void addReceiver(DeviceConnectionEventReceiver receiver);

    public void removeReceiver(DeviceConnectionEventReceiver receiver);
}
