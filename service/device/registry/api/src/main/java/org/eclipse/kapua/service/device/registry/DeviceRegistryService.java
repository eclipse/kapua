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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

public interface DeviceRegistryService extends KapuaEntityService<Device, DeviceCreator>,
                                       KapuaUpdatableEntityService<Device>
{
    /**
     * Finds a device by its unique clientId and loads it with all its properties.
     */
    public Device findByClientId(KapuaId scopeId, String clientId)
        throws KapuaException;
}
