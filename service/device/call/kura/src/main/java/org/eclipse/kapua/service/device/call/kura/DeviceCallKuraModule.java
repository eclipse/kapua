/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;
import org.eclipse.kapua.service.device.call.DeviceMessageFactory;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettings;

import javax.inject.Singleton;

public class DeviceCallKuraModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceCallFactory.class).to(KuraDeviceCallFactoryImpl.class).in(Singleton.class);
        bind(DeviceMessageFactory.class).to(KuraMessageFactoryImpl.class).in(Singleton.class);
        bind(DeviceCallSettings.class).in(Singleton.class);
    }
}
