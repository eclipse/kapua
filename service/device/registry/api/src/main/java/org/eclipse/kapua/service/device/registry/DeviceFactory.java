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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device service factory definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceFactory extends KapuaObjectFactory
{

    /**
     * Creates a new device creator
     * 
     * @param scopeId
     * @param clientId
     * @return
     */
    public DeviceCreator newCreator(KapuaId scopeId, String clientId);

    /**
     * Creates a new {@link Device}
     * 
     * @return
     */
    public Device newDevice();
    
    /**
     * Creates a new device query base on provided scoper identifier
     * 
     * @param scopeId
     * @return
     */
    public DeviceQuery newQuery(KapuaId scopeId);
    
    /**
     * Creates a new device list result
     * 
     * @return
     */
    public DeviceListResult newDeviceListResult();
}
