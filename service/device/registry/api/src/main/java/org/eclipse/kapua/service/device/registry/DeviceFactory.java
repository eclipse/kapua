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

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device service factory definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceFactory extends KapuaEntityFactory<Device, DeviceCreator, DeviceQuery, DeviceListResult>
{

    /**
     * Creates a new device creator
     * 
     * @param scopeId
     * @param clientId
     * @return
     */
    public DeviceCreator newCreator(KapuaId scopeId, String clientId);

}
