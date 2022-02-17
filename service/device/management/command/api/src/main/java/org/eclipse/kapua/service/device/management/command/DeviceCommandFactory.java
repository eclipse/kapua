/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.command;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link DeviceCommand} {@link KapuaObjectFactory} definition.
 *
 * @since 1.0.0
 */
public interface DeviceCommandFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link DeviceCommandInput}.
     *
     * @return The newly instantiated {@link DeviceCommandInput}.
     * @since 1.0.0
     */
    DeviceCommandInput newCommandInput();

    /**
     * Instantiates a new {@link DeviceCommandOutput}.
     *
     * @return The newly instantiated {@link DeviceCommandOutput}.
     * @since 1.0.0
     */
    DeviceCommandOutput newCommandOutput();
}
