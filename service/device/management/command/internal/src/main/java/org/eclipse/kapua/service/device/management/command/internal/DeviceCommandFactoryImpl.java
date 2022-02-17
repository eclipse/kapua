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
package org.eclipse.kapua.service.device.management.command.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;

/**
 * {@link DeviceCommandFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceCommandFactoryImpl implements DeviceCommandFactory {

    @Override
    public DeviceCommandInput newCommandInput() {
        return new DeviceCommandInputImpl();
    }

    @Override
    public DeviceCommandOutput newCommandOutput() {
        return new DeviceCommandOutputImpl();
    }

}
