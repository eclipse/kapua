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
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.devicegroup.DevGroupListResult;
import org.eclipse.kapua.service.devicegroup.DevGroupQuery;
import org.eclipse.kapua.service.devicegroup.DeviceGroup;
import org.eclipse.kapua.service.devicegroup.DeviceGroupCreator;
import org.eclipse.kapua.service.devicegroup.DeviceGroupPredicates;
import org.eclipse.kapua.service.devicegroup.DeviceGroupService;

@KapuaProvider
public class DeviceGroupServiceImpl extends AbstractKapuaConfigurableService
        implements DeviceGroupService {

    private static final Domain deviceGroupDomain = new DeviceGroupDomain();

    public DeviceGroupServiceImpl() {
        super(DeviceGroupService.class.getName(), deviceGroupDomain,
                DevGroupEntityManagerFactory.getInstance());
    }

    @Override
    public DeviceGroup find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        ArgumentValidator.notNull(scopeId.getId(), "devGroupId");
        ArgumentValidator.notNull(entityId.getId(), "id");
        return entityManagerSession.onResult(em -> DeviceGroupDAO.find(em, entityId));
    }

    @Override
    public long count(KapuaQuery<DeviceGroup> query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");
        return entityManagerSession.onResult(em -> DeviceGroupDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        ArgumentValidator.notNull(entityId.getId(), "id");
        ArgumentValidator.notNull(scopeId.getId(), "devGroupId");
        entityManagerSession.onTransactedAction(em -> {
            DeviceGroup dg = DeviceGroupDAO.find(em, entityId);
            if (dg == null) {
                throw new KapuaEntityNotFoundException("id", entityId);
            }

            DeviceGroupDAO.delete(em, entityId);
        });
    }

    @Override
    public DeviceGroup update(DeviceGroup entity) throws KapuaException {
        return null;
    }

    @Override
    public DeviceGroup findByName(String name) throws KapuaException {
        ArgumentValidator.notEmptyOrNull(name, "name");
        return entityManagerSession.onResult(em -> {
            DeviceGroup deviceGroup = DeviceGroupDAO.findByName(em, name);
            return deviceGroup;
        });
    }

    @Override
    public DeviceGroup create(DeviceGroupCreator deviceGroupCreator) throws KapuaException {
        ArgumentValidator.notNull(deviceGroupCreator.getScopeId().getId(), "scopeId");
        ArgumentValidator.notNull(deviceGroupCreator.getDevId(), "devId");
        ArgumentValidator.notNull(deviceGroupCreator.getGroupId(), "groupId");

        return entityManagerSession
                .onTransactedInsert(em -> DeviceGroupDAO.create(em, deviceGroupCreator));
    }

    @Override
    public void delete(DeviceGroup deviceGroup) throws KapuaException {
        ArgumentValidator.notNull(deviceGroup.getDevId(), "devId");
        ArgumentValidator.notNull(deviceGroup.getGroupId(), "groupId");
        delete(deviceGroup.getScopeId(), deviceGroup.getId());
    }

    @Override
    public DevGroupListResult query(KapuaQuery<DeviceGroup> query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        return entityManagerSession.onResult(em -> DeviceGroupDAO.query(em, query));
    }

    @Override
    public DevGroupListResult findByDeviceId(KapuaId scopeId, KapuaId devId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(devId, "devId");

        DevGroupQuery query = new DevGroupQueryImpl(scopeId);
        query.setPredicate(new AttributePredicate<KapuaId>(DeviceGroupPredicates.DEVICE_ID, devId));

        return query(query);
    }

}
