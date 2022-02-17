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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;

/**
 * {@link DeviceCallFactory} {@link Kura} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class KuraDeviceCallFactoryImpl implements DeviceCallFactory {

    @Override
    public KuraDeviceCallImpl newDeviceCall() {
        return new KuraDeviceCallImpl();
    }

}
