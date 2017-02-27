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
