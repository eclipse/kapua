/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.command.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;

/**
 * Device command entity service factory implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class DeviceCommandFactoryImpl implements DeviceCommandFactory
{

    @Override
    public DeviceCommandInput newCommandInput()
    {
        return new DeviceCommandInputImpl();
    }

    @Override
    public DeviceCommandOutput newCommandOutput()
    {
        return new DeviceCommandOutputImpl();
    }

}
