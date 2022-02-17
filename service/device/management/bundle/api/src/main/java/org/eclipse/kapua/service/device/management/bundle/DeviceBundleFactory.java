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

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Device bundle entity service factory definition.
 *
 * @since 1.0
 */
public interface DeviceBundleFactory extends KapuaObjectFactory {

    /**
     * Creates a new device bundle list
     *
     * @return
     */
    DeviceBundles newBundleListResult();

    /**
     * Create a new {@link DeviceBundle}
     *
     * @return
     */
    DeviceBundle newDeviceBundle();
}
