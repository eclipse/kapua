/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.client.group.GwtGroupCreator;
import org.eclipse.kapua.app.console.client.group.GwtGroupQuery;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.*;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.*;

public class GwtGroupServiceImpl extends KapuaRemoteServiceServlet implements GwtGroupService {

    private static final long serialVersionUID = 929002466564699535L;

    @Override
    public GwtGroup create(GwtGroupCreator gwtGroupCreator) throws GwtKapuaException {
        GwtGroup gwtGroup = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
            KapuaId scopeId = KapuaEid.parseCompactId(gwtGroupCreator.getScopeId());
            GroupCreator groupCreator = groupFactory.newCreator(scopeId, gwtGroupCreator.getName());
            GroupService groupService = locator.getService(GroupService.class);
            Group group = groupService.create(groupCreator);
            gwtGroup = KapuaGwtModelConverter.convert(group);

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtGroup;
    }

    @Override
    public GwtGroup update(GwtGroup gwtGroup) throws GwtKapuaException {
        GwtGroup gwtGroupUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupService groupService = locator.getService(GroupService.class);
            KapuaId scopeId = KapuaEid.parseCompactId(gwtGroup.getScopeId());
            KapuaId groupId = KapuaEid.parseCompactId(gwtGroup.getId());
            Group group = groupService.find(scopeId, groupId);

            if (group != null) {
                group.setName(gwtGroup.getGroupName());
                groupService.update(group);
                gwtGroupUpdated = KapuaGwtModelConverter
                        .convert(groupService.find(group.getScopeId(), group.getId()));
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtGroupUpdated;
    }

    @Override
    public GwtGroup find(String scopeShortId, String roleShortId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
        KapuaId groupId = KapuaEid.parseCompactId(roleShortId);
        GwtGroup gwtGroup = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupService groupService = locator.getService(GroupService.class);
            Group group = groupService.find(scopeId, groupId);
            if (group != null) {
                gwtGroup = KapuaGwtModelConverter.convert(group);
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtGroup;
    }

    @Override
    public PagingLoadResult<GwtGroup> query(PagingLoadConfig loadConfig,
            GwtGroupQuery gwtGroupQuery) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtGroup> gwtGroupList = new ArrayList<GwtGroup>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupService groupService = locator.getService(GroupService.class);
            GroupQuery groupQuery = GwtKapuaModelConverter.convertGroupQuery(loadConfig,
                    gwtGroupQuery);
            GroupListResult groups = groupService.query(groupQuery);
            if (!groups.isEmpty()) {
                if (groups.getSize() >= loadConfig.getLimit()) {
                    totalLength = new Long(groupService.count(groupQuery)).intValue();

                } else {
                    totalLength = groups.getSize();
                }
                for (Group g : groups.getItems()) {
                    gwtGroupList.add(KapuaGwtModelConverter.convert(g));
                }
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtGroup>(gwtGroupList, loadConfig.getOffset(),
                totalLength);
    }

    @Override
    public void delete(String scopeId, String groupId) throws GwtKapuaException {

        try {
            KapuaId scopeId2 = KapuaEid.parseCompactId(scopeId);
            KapuaId groupId2 = GwtKapuaModelConverter.convert(groupId);
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupService groupService = locator.getService(GroupService.class);
            groupService.delete(scopeId2, groupId2);
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getGroupDescription(String scopeShortId,
            String groupShortId) throws GwtKapuaException {
        List<GwtGroupedNVPair> gwtGroupDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupService groupService = locator.getService(GroupService.class);
            KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId groupId = KapuaEid.parseCompactId(groupShortId);
            Group group = groupService.find(scopeId, groupId);

            if (group != null) {
                // gwtGroupDescription.add(new GwtGroupedNVPair("Entity", "Scope
                // Id", KapuaGwtModelConverter.convert(group.getScopeId())));
                gwtGroupDescription
                        .add(new GwtGroupedNVPair("Group", "Group Name", group.getName()));
                gwtGroupDescription.add(new GwtGroupedNVPair("Entity", "Modified On",
                        group.getModifiedOn().toString()));
                gwtGroupDescription.add(new GwtGroupedNVPair("Entity", "Modified By",
                        group.getModifiedBy().toCompactId()));
                gwtGroupDescription.add(new GwtGroupedNVPair("Entity", "Created On",
                        group.getCreatedOn().toString()));
                gwtGroupDescription.add(new GwtGroupedNVPair("Entity", "Created By",
                        group.getCreatedBy().toCompactId()));

            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtGroupedNVPair>(gwtGroupDescription);
    }

    @Override
    public List<GwtGroup> findAll(String scopeId) {
        List<GwtGroup> groupList = new ArrayList<>();
        KapuaLocator locator = KapuaLocator.getInstance();
        GroupService groupService = locator.getService(GroupService.class);
        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
        GroupQuery query = groupFactory.newQuery(GwtKapuaModelConverter.convert(scopeId));
        try {
            GroupListResult result = groupService.query(query);
            for (Group group : result.getItems()) {
                groupList.add(KapuaGwtModelConverter.convert(group));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return groupList;
    }
}
