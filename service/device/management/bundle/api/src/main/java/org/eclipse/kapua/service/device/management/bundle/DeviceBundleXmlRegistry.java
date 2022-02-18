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
package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device bundle xml factory class
 *
 * @since 1.0
 */
public class DeviceBundleXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceBundleFactory factory = locator.getFactory(DeviceBundleFactory.class);

    /**
     * Creates a new device bundles list
     *
     * @return
     */
    public DeviceBundles newBundleListResult() {
        return factory.newBundleListResult();
    }

    /**
     * Creates a new device bundle
     *
     * @return
     */
    public DeviceBundle newDeviceBundle() {
        return factory.newDeviceBundle();
    }
}
