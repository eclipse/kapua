/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device connection service factory definition.
 *
 * @since 1.0
 */
public interface DeviceConnectionFactory extends KapuaObjectFactory {

    /**
     * Creates a new device connection creator
     *
     * @param scopeId
     * @return
     */
    public DeviceConnectionCreator newCreator(KapuaId scopeId);

    /**
     * Creates a new {@link DeviceConnection}
     * 
     * @return
     */
    public DeviceConnection newDeviceConnection();

    /**
     * Creates a new device connection query for the specified scope identifier
     *
     * @param scopeId
     * @return
     */
    public DeviceConnectionQuery newQuery(KapuaId scopeId);

    /**
     * Creates a new device connection list result
     * 
     * @return
     */
    public DeviceConnectionListResult newDeviceConnectionListResult();

    /**
     * Creates a new device connection summary
     *
     * @return
     */
    public DeviceConnectionSummary newConnectionSummary();

}
