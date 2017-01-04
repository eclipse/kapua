/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.test.device;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;

@KapuaProvider
public class DeviceRegistryFactoryMock implements DeviceFactory {

	@Override
	public DeviceCreator newCreator(KapuaId scopeId, String clientId) {
		return new DeviceCreatorMock(scopeId, clientId);
	}

	@Override
	public DeviceQuery newQuery(KapuaId scopeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device newDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceListResult newDeviceListResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
