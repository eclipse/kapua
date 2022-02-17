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
package org.eclipse.kapua.service.device.management.bundle.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleFactory;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;

/**
 * Device bundle entity service factory implementation.
 *
 * @since 1.0
 *
 */
@KapuaProvider
public class DeviceBundleFactoryImpl implements DeviceBundleFactory {

    @Override
    public DeviceBundles newBundleListResult() {
        return new DeviceBundlesImpl();
    }

    @Override
    public DeviceBundle newDeviceBundle() {
        return new DeviceBundleImpl();
    }

}
