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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceCommand} XmlFactory implementation.
 *
 * @since 1.0.0
 */
@XmlRegistry
public class DeviceCommandXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceCommandFactory factory = locator.getFactory(DeviceCommandFactory.class);

    /**
     * Instantiates a new {@link DeviceCommandInput}.
     *
     * @return The newly instantiated {@link DeviceCommandInput}.
     * @since 1.0.0
     */
    public DeviceCommandInput newCommandInput() {
        return factory.newCommandInput();
    }

    /**
     * Instantiates a new {@link DeviceCommandOutput}.
     *
     * @return The newly instantiated {@link DeviceCommandOutput}.
     * @since 1.0.0
     */
    public DeviceCommandOutput newCommandOutput() {
        return factory.newCommandOutput();
    }

}
