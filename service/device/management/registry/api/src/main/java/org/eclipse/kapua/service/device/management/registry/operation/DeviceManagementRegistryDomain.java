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

import org.eclipse.kapua.model.domain.AbstractDomain;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.service.device.management.DeviceManagementService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link DeviceManagementService} domain.<br>
 * Used to describe the {@link DeviceManagementService} {@link Domain} in the {@link DeviceManagementService}.
 *
 * @since 1.0.0
 */
public class DeviceManagementRegistryDomain extends AbstractDomain implements Domain {

    private String name = "device_management_registry";
    private String serviceName = DeviceManagementService.class.getName();
    private Set<Actions> actions = new HashSet<>(Arrays.asList(Actions.execute, Actions.read, Actions.write));

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public boolean getGroupable() {
        return false;
    }
}
