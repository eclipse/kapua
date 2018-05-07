/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.tag.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagCreator;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.util.GwtKapuaTagModelConverter;
import org.eclipse.kapua.app.console.module.tag.shared.util.KapuaGwtTagModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtTagServiceImpl extends KapuaRemoteServiceServlet implements GwtTagService {

    private static final long serialVersionUID = 929002466564699535L;

    @Override
    public GwtTag create(GwtTagCreator gwtTagCreator) throws GwtKapuaException {
        GwtTag gwtTag = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TagFactory tagFactory = locator.getFactory(TagFactory.class);
            KapuaId scopeId = KapuaEid.parseCompactId(gwtTagCreator.getScopeId());
            TagCreator tagCreator = tagFactory.newCreator(scopeId, gwtTagCreator.getName());
            TagService tagService = locator.getService(TagService.class);
            Tag tag = tagService.create(tagCreator);
            gwtTag = KapuaGwtTagModelConverter.convertTag(tag);

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtTag;
    }

    @Override
    public GwtTag update(GwtTag gwtTag) throws GwtKapuaException {
        GwtTag gwtTagUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TagService tagService = locator.getService(TagService.class);
            KapuaId scopeId = KapuaEid.parseCompactId(gwtTag.getScopeId());
            KapuaId tagId = KapuaEid.parseCompactId(gwtTag.getId());
            Tag tag = tagService.find(scopeId, tagId);

            if (tag != null) {
                tag.setName(gwtTag.getTagName());
                tagService.update(tag);
                gwtTagUpdated = KapuaGwtTagModelConverter.convertTag(tagService.find(tag.getScopeId(), tag.getId()));
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtTagUpdated;
    }

    @Override
    public GwtTag find(String scopeShortId, String tagShortId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
        KapuaId tagId = KapuaEid.parseCompactId(tagShortId);
        GwtTag gwtTag = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TagService tagService = locator.getService(TagService.class);
            Tag tag = tagService.find(scopeId, tagId);
            if (tag != null) {
                gwtTag = KapuaGwtTagModelConverter.convertTag(tag);
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtTag;
    }

    @Override
    public PagingLoadResult<GwtTag> query(PagingLoadConfig loadConfig,
            final GwtTagQuery gwtTagQuery) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtTag> gwtTagList = new ArrayList<GwtTag>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TagService tagService = locator.getService(TagService.class);
            TagQuery tagQuery = GwtKapuaTagModelConverter.convertTagQuery(loadConfig, gwtTagQuery);
            final UserService userService = locator.getService(UserService.class);
            final UserFactory userFactory = locator.getFactory(UserFactory.class);
            Map<String, String> usernameMap = new HashMap<String, String>();
            UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                @Override
                public UserListResult call() throws Exception {
                    return userService.query(userFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtTagQuery.getScopeId())));
                }
            });
            for (User user : userListResult.getItems()) {
                usernameMap.put(user.getId().toCompactId(), user.getName());
            }
            TagListResult tags = tagService.query(tagQuery);
            if (!tags.isEmpty()) {
                totalLength = Long.valueOf(tagService.count(tagQuery)).intValue();
                for (Tag g : tags.getItems()) {
                    GwtTag gwtTag = KapuaGwtTagModelConverter.convertTag(g);
                    gwtTag.setUserName(usernameMap.get(g.getCreatedBy().toCompactId()));
                    gwtTagList.add(gwtTag);

                }
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtTag>(gwtTagList, loadConfig.getOffset(),
                totalLength);
    }

    @Override
    public void delete(String scopeId, String tagId) throws GwtKapuaException {

        try {
            KapuaId scopeId2 = KapuaEid.parseCompactId(scopeId);
            KapuaId tagId2 = GwtKapuaCommonsModelConverter.convertKapuaId(tagId);
            KapuaLocator locator = KapuaLocator.getInstance();
            TagService tagService = locator.getService(TagService.class);
            tagService.delete(scopeId2, tagId2);
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getTagDescription(String scopeShortId, String tagShortId) throws GwtKapuaException {
        List<GwtGroupedNVPair> gwtTagDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TagService tagService = locator.getService(TagService.class);
            final UserService userService = locator.getService(UserService.class);
            final KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId tagId = KapuaEid.parseCompactId(tagShortId);
            final Tag tag = tagService.find(scopeId, tagId);
            User createdUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, tag.getCreatedBy());
                }
            });
            User modifiedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, tag.getModifiedBy());
                }
            });

            if (tag != null) {
                // gwtTagDescription.add(new GwtGroupedNVPair("Entity", "Scope
                // Id", KapuaGwtCommonsModelConverter.convertKapuaId(tag.getScopeId())));
                gwtTagDescription.add(new GwtGroupedNVPair("tagInfo", "tagName", tag.getName()));
                gwtTagDescription.add(new GwtGroupedNVPair("entityInfo", "tagModifiedOn", tag.getModifiedOn()));
                gwtTagDescription.add(new GwtGroupedNVPair("entityInfo", "tagModifiedBy", modifiedUser != null ? modifiedUser.getName() : null));
                gwtTagDescription.add(new GwtGroupedNVPair("entityInfo", "tagCreatedOn", tag.getCreatedOn()));
                gwtTagDescription.add(new GwtGroupedNVPair("entityInfo", "tagCreatedBy", createdUser != null ? createdUser.getName() : null));

            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtGroupedNVPair>(gwtTagDescription);
    }

    @Override
    public List<GwtTag> findAll(String scopeId) {
        List<GwtTag> tagList = new ArrayList<GwtTag>();
        KapuaLocator locator = KapuaLocator.getInstance();
        TagService tagService = locator.getService(TagService.class);
        TagFactory tagFactory = locator.getFactory(TagFactory.class);
        TagQuery query = tagFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        try {
            TagListResult result = tagService.query(query);
            for (Tag tag : result.getItems()) {
                tagList.add(KapuaGwtTagModelConverter.convertTag(tag));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return tagList;
    }

    @Override
    public PagingLoadResult<GwtTag> findByDeviceId(PagingLoadConfig loadConfig, String gwtScopeId, String gwtDeviceId) throws GwtKapuaException {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId deviceId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceId);
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        try {
            Device device = deviceRegistryService.find(scopeId, deviceId);
            if (device.getTagIds().size() == 0) {
                return new BasePagingLoadResult<GwtTag>(new ArrayList<GwtTag>(), 0, 0);
            }
            List<String> gwtTagIds = new ArrayList<String>();
            GwtTagQuery gwtTagQuery = new GwtTagQuery();
            gwtTagQuery.setScopeId(gwtScopeId);
            for (KapuaId tagId : device.getTagIds()) {
                gwtTagIds.add(KapuaGwtCommonsModelConverter.convertKapuaId(tagId));
            }
            gwtTagQuery.setIds(gwtTagIds);
            return query(loadConfig, gwtTagQuery);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
            return new BasePagingLoadResult<GwtTag>(new ArrayList<GwtTag>(), 0, 0);
        }
    }
}
