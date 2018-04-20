/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;

/**
 * {@link DeviceCallFactory} {@link Kura} implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class KuraDeviceCallFactoryImpl implements DeviceCallFactory {

    @Override
    public KuraDeviceCallImpl newDeviceCall() {
        return new KuraDeviceCallImpl();
    }

}
