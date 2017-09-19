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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.util.GwtKapuaAuthorizationModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.util.KapuaGwtAuthorizationModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

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
            gwtGroup = KapuaGwtAuthorizationModelConverter.convertGroup(group);

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
                gwtGroupUpdated = KapuaGwtAuthorizationModelConverter.convertGroup(groupService.find(group.getScopeId(), group.getId()));
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtGroupUpdated;
    }

    @Override
    public GwtGroup find(String scopeShortId, String groupShortId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
        KapuaId groupId = KapuaEid.parseCompactId(groupShortId);
        GwtGroup gwtGroup = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            GroupService groupService = locator.getService(GroupService.class);
            Group group = groupService.find(scopeId, groupId);
            if (group != null) {
                gwtGroup = KapuaGwtAuthorizationModelConverter.convertGroup(group);
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
            UserService userService = locator.getService(UserService.class);
            GroupQuery groupQuery = GwtKapuaAuthorizationModelConverter.convertGroupQuery(loadConfig,
                    gwtGroupQuery);
            GroupListResult groups = groupService.query(groupQuery);
            if (!groups.isEmpty()) {
                if (groups.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(groupService.count(groupQuery)).intValue();

                } else {
                    totalLength = groups.getSize();
                }
                for (Group g : groups.getItems()) {
                    gwtGroupList.add(KapuaGwtAuthorizationModelConverter.convertGroup(g));
                    for (GwtGroup gwtGroup : gwtGroupList) {
                        User user = userService.find(g.getScopeId(), g.getCreatedBy());
                        if (user != null) {
                            gwtGroup.setUserName(user.getDisplayName());
                        }
                }
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
            KapuaId groupId2 = GwtKapuaCommonsModelConverter.convertKapuaId(groupId);
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
            UserService userService = locator.getService(UserService.class);
            KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId groupId = KapuaEid.parseCompactId(groupShortId);
            Group group = groupService.find(scopeId, groupId);
            User user = userService.find(scopeId, group.getCreatedBy());

            if (group != null) {
                // gwtGroupDescription.add(new GwtGroupedNVPair("Entity", "Scope
                // Id", KapuaGwtAuthenticationModelConverter.convertKapuaId(group.getScopeId())));
                gwtGroupDescription
                        .add(new GwtGroupedNVPair("accessGroupInfo", "accessGroupName", group.getName()));
                gwtGroupDescription.add(new GwtGroupedNVPair("entityInfo", "accessGroupModifiedOn",
                        group.getModifiedOn().toString()));
                gwtGroupDescription.add(new GwtGroupedNVPair("entityInfo", "accessGroupModifiedBy",
                        user.getDisplayName()));
                gwtGroupDescription.add(new GwtGroupedNVPair("entityInfo", "accessGroupCreatedOn",
                        group.getCreatedOn().toString()));
                gwtGroupDescription.add(new GwtGroupedNVPair("entityInfo", "accessGroupCreatedBy",
                        user.getDisplayName()));

            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtGroupedNVPair>(gwtGroupDescription);
    }

    @Override
    public List<GwtGroup> findAll(String scopeId) {
        List<GwtGroup> groupList = new ArrayList<GwtGroup>();
        KapuaLocator locator = KapuaLocator.getInstance();
        GroupService groupService = locator.getService(GroupService.class);
        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
        GroupQuery query = groupFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        try {
            GroupListResult result = groupService.query(query);
            for (Group group : result.getItems()) {
                groupList.add(KapuaGwtAuthorizationModelConverter.convertGroup(group));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return groupList;
    }

}
