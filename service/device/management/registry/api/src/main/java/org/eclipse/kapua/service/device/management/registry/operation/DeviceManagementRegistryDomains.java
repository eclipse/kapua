/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
