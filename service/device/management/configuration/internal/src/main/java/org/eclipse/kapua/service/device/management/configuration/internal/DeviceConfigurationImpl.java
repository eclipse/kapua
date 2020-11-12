/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;

/**
 * Device configuration entity implementation.
 *
 * @since 1.0
 */
public class DeviceConfigurationImpl implements DeviceConfiguration {

    private static final long serialVersionUID = -2167999497954676423L;

    private List<DeviceComponentConfiguration> configurations;

    public DeviceConfigurationImpl() {
        configurations = new ArrayList<>();
    }

    @Override
    public List<DeviceComponentConfiguration> getComponentConfigurations() {
        return configurations;
    }

}
