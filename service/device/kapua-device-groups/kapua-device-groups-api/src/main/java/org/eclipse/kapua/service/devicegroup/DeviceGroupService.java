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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.devicegroup;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

public interface DeviceGroupService extends KapuaEntityService<DeviceGroup, DeviceGroupCreator>,
KapuaUpdatableEntityService<DeviceGroup>,
KapuaNamedEntityService<DeviceGroup>,
KapuaConfigurableService
{
    public DeviceGroup create(DeviceGroupCreator deviceGroupCreator) throws KapuaException;
    
    public void delete(DeviceGroup deviceGroup) throws KapuaException;
     
    public DevGroupListResult query(KapuaQuery<DeviceGroup> query) throws KapuaException;
    
    public DevGroupListResult findByDeviceId(KapuaId scopeId, KapuaId devId) throws KapuaException;
}
