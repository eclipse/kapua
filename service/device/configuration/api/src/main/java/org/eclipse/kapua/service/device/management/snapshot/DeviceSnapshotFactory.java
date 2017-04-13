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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Device snapshot entity service factory definition.
 *
 * @since 1.0
 * 
 */
public interface DeviceSnapshotFactory extends KapuaObjectFactory
{

    /**
     * Creates a new {@link DeviceSnapshot}
     * 
     * @return
     */
    public DeviceSnapshot newDeviceSnapshot();

    /**
     * Creates a new {@link DeviceSnapshots}
     * 
     * @return
     */
    public DeviceSnapshots newDeviceSnapshots();
}
