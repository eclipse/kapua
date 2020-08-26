/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.event.DeviceEventDomain;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifecycleDomain;

public class DeviceDomains {

    private DeviceDomains() { }

    public static final DeviceDomain DEVICE_DOMAIN = new DeviceDomain();

    public static final DeviceConnectionDomain DEVICE_CONNECTION_DOMAIN = new DeviceConnectionDomain();

    public static final DeviceEventDomain DEVICE_EVENT_DOMAIN = new DeviceEventDomain();

    public static final DeviceLifecycleDomain DEVICE_LIFECYCLE_DOMAIN = new DeviceLifecycleDomain();
}
