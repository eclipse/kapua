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
package org.eclipse.kapua.service.devicegroup.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.devicegroup.DevGroupListResult;
import org.eclipse.kapua.service.devicegroup.DeviceGroup;
import org.eclipse.kapua.service.devicegroup.DeviceGroupCreator;

public class DeviceGroupDAO extends ServiceDAO {

    public static DeviceGroup create(EntityManager em, DeviceGroupCreator deviceGroupCreator)
            throws KapuaException {
        DeviceGroupImpl deviceGroupImpl = new DeviceGroupImpl(deviceGroupCreator.getScopeId(),
                deviceGroupCreator.getName());

        deviceGroupImpl.setDevId(deviceGroupCreator.getDevId());
        deviceGroupImpl.setGroupId(deviceGroupCreator.getGroupId());

        return ServiceDAO.create(em, deviceGroupImpl);
    }

    public static void delete(EntityManager em, KapuaId devGroupId)
            throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, DeviceGroupImpl.class, devGroupId);
    }

    public static DeviceGroup find(EntityManager em, KapuaId devGroupId) {
        return em.find(DeviceGroupImpl.class, devGroupId);
    }

    public static DeviceGroup findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, DeviceGroupImpl.class, "name", name);
    }

    public static DevGroupListResult query(EntityManager em, KapuaQuery<DeviceGroup> query)
            throws KapuaException {
        return ServiceDAO.query(em, DeviceGroup.class, DeviceGroupImpl.class,
                new DevGroupResultImpl(), query);
    }

    public static long count(EntityManager em, KapuaQuery<DeviceGroup> query)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceGroup.class, DeviceGroupImpl.class, query);
    }
}
