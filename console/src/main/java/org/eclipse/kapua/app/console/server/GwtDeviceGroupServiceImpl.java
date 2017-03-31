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
package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.device.deviceGroup.GwtDeviceGroupCreator;
import org.eclipse.kapua.app.console.client.device.deviceGroup.GwtDeviceGroupQuery;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceGroup;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.devicegroup.DevGroupListResult;
import org.eclipse.kapua.service.devicegroup.DevGroupQuery;
import org.eclipse.kapua.service.devicegroup.DeviceGroup;
import org.eclipse.kapua.service.devicegroup.DeviceGroupCreator;
import org.eclipse.kapua.service.devicegroup.DeviceGroupFactory;
import org.eclipse.kapua.service.devicegroup.DeviceGroupService;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.IsSerializable;

import org.eclipse.kapua.service.authorization.group.Group;

public class GwtDeviceGroupServiceImpl extends KapuaRemoteServiceServlet
        implements GwtDeviceGroupService, IsSerializable {

    /**
     * 
     */
    private static final long serialVersionUID = -544858591748485947L;

    @Override
    public GwtDeviceGroup create(GwtDeviceGroupCreator gwtDeviceGroupCreator)
            throws GwtKapuaException {
        GwtDeviceGroup gwtDeviceGroup = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceGroupFactory factory = locator.getFactory(DeviceGroupFactory.class);
            KapuaId scopeId = KapuaEid.parseCompactId(gwtDeviceGroupCreator.getScopeId());
            DeviceGroupCreator creator = factory.newCreator(scopeId,
                    gwtDeviceGroupCreator.getDevId());

            creator.setDevId(GwtKapuaModelConverter.convert(gwtDeviceGroupCreator.getDevId()));
            creator.setGroupId(GwtKapuaModelConverter.convert(gwtDeviceGroupCreator.getGroupId()));
            DeviceGroupService service = locator.getService(DeviceGroupService.class);
            DeviceGroup deviceGroup = service.create(creator);
            gwtDeviceGroup = KapuaGwtModelConverter.convert(deviceGroup);

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtDeviceGroup;
    }

    @Override
    public void delete(String id, String deviceGroupId) throws GwtKapuaException {
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(id);
            KapuaId devGroupId2 = KapuaEid.parseCompactId(deviceGroupId);
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceGroupService service = locator.getService(DeviceGroupService.class);
            service.delete(scopeId, devGroupId2);
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

    }

    @Override
    public GwtDeviceGroup find(String id, String devGroupId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(id);
        KapuaId devGroupId1 = KapuaEid.parseCompactId(devGroupId);

        GwtDeviceGroup gwtDeviceGroup = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceGroupService service = locator.getService(DeviceGroupService.class);
            DeviceGroup deviceGroup = service.find(scopeId, devGroupId1);
            if (deviceGroup != null) {
                gwtDeviceGroup = KapuaGwtModelConverter.convert(deviceGroup);
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtDeviceGroup;
    }

    @Override
    public PagingLoadResult<GwtDeviceGroup> query(PagingLoadConfig loadConfig,
            GwtDeviceGroupQuery gwtDeviceGroupQuery) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtDeviceGroup> gwtDeviceGroupList = new ArrayList<GwtDeviceGroup>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceGroupService service = locator.getService(DeviceGroupService.class);
            DevGroupQuery devGroupQuery = GwtKapuaModelConverter.convertDeviceGroupQuery(loadConfig,
                    gwtDeviceGroupQuery);
            DevGroupListResult devs = service.query(devGroupQuery);
            if (!devs.isEmpty()) {
                if (devs.getSize() >= loadConfig.getLimit()) {
                    totalLength = devs.getSize();
                } else {
                    totalLength = devs.getSize();
                }
                for (DeviceGroup d : devs.getItems()) {
                    gwtDeviceGroupList.add(KapuaGwtModelConverter.convert(d));
                }
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<>(gwtDeviceGroupList, loadConfig.getOffset(), totalLength);
    }

    @Override
    public PagingLoadResult<GwtDeviceGroup> findByDeviceId(PagingLoadConfig loadConfig,
            String scopeShortId, String deviceShortId) throws GwtKapuaException {
        List<GwtDeviceGroup> gwtDeviceGroups = new ArrayList<GwtDeviceGroup>();
        if (deviceShortId != null) {
            try {
                KapuaLocator locator = KapuaLocator.getInstance();
                DeviceGroupService service = locator.getService(DeviceGroupService.class);
                GroupService groupService = locator.getService(GroupService.class);
                KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
                KapuaId devId = GwtKapuaModelConverter.convert(deviceShortId);

                DevGroupListResult deviceGroupList = service.findByDeviceId(scopeId, devId);
                for (DeviceGroup devGroup : deviceGroupList.getItems()) {
                    Group group = groupService.find(scopeId, devGroup.getGroupId());
                    GwtDeviceGroup gwtDeviceGroup = KapuaGwtModelConverter.convert(group, devGroup);
                    gwtDeviceGroups.add(gwtDeviceGroup);
                }
            } catch (Exception e) {
                KapuaExceptionHandler.handle(e);
            }
        }
        return new BasePagingLoadResult<GwtDeviceGroup>(gwtDeviceGroups, 0, gwtDeviceGroups.size());
    }

}
