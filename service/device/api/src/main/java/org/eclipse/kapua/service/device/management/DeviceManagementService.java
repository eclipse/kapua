/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management;

import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaService;

public interface DeviceManagementService extends KapuaService, KapuaDomainService<DeviceManagementDomain> {

    public static final DeviceManagementDomain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();

    @Override
    public default DeviceManagementDomain getServiceDomain() {
        return DEVICE_MANAGEMENT_DOMAIN;
    }

}
