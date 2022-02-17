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
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link DeviceSnapshot} {@link KapuaObjectFactory} definition.
 *
 * @see KapuaObjectFactory
 * @since 1.0.0
 */
public interface DeviceSnapshotFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link DeviceSnapshot}
     *
     * @return The newly instantiated {@link DeviceSnapshot}.
     * @since 1.0.0
     */
    DeviceSnapshot newDeviceSnapshot();

    /**
     * Instantiates a new {@link DeviceSnapshots}
     *
     * @return The newly instantiated {@link DeviceSnapshots}
     * @since 1.0.0
     */
    DeviceSnapshots newDeviceSnapshots();
}
