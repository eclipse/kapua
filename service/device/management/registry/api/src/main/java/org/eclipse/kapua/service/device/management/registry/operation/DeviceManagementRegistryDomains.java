/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation;

import org.eclipse.kapua.model.domain.Domain;

/**
 * DeviceManagementRegistryService domain.<br>
 * Used to describe the DeviceManagementRegistryService {@link Domain} in the DeviceManagementRegistryService.
 *
 * @since 1.0.0
 */
public class DeviceManagementRegistryDomains {

    private DeviceManagementRegistryDomains() {
    }

    public static final DeviceManagementRegistryDomain DEVICE_MANAGEMENT_REGISTRY_DOMAIN = new DeviceManagementRegistryDomain();
}
