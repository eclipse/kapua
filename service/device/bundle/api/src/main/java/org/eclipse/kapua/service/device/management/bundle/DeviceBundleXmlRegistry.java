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
package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.locator.KapuaLocator;

public class DeviceBundleXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceBundleFactory factory = locator.getFactory(DeviceBundleFactory.class);

    public DeviceBundles newBundleListResult() { return factory.newBundleListResult(); }

    public DeviceBundle newDeviceBundle() { return factory.newDeviceBundle(); }
}
