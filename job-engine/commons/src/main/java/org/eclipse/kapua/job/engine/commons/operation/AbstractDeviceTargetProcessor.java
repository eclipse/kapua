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
package org.eclipse.kapua.job.engine.commons.operation;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.job.targets.JobTarget;

public abstract class AbstractDeviceTargetProcessor extends AbstractTargetProcessor {
    protected static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    protected static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
    @Override
    protected String getTargetDisplayName(JobTarget jobTarget) throws KapuaException {
        Device device = KapuaSecurityUtils.doPrivileged(() -> DEVICE_REGISTRY_SERVICE.find(jobTarget.getScopeId(), jobTarget.getJobTargetId()));;
        if (device == null) {
            return "N/A";
        }
        return device.getClientId();
    }
}
