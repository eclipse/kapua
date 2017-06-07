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
package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.client.tag.GwtTagCreator;
import org.eclipse.kapua.app.console.client.tag.GwtTagQuery;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtTag;
import org.eclipse.kapua.app.console.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtTagServiceImpl extends KapuaConfigurableRemoteServiceServlet<TagService> implements GwtTagService {

    private static final long serialVersionUID = 929002466564699535L;

    public GwtTagServiceImpl() {
        super(KapuaLocator.getInstance().getService(TagService.class));
    }

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
            gwtTag = KapuaGwtModelConverter.convert(tag);

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
                gwtTagUpdated = KapuaGwtModelConverter
                        .convert(tagService.find(tag.getScopeId(), tag.getId()));
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
                gwtTag = KapuaGwtModelConverter.convert(tag);
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtTag;
    }

    @Override
    public PagingLoadResult<GwtTag> query(PagingLoadConfig loadConfig,
            GwtTagQuery gwtTagQuery) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtTag> gwtTagList = new ArrayList<GwtTag>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TagService tagService = locator.getService(TagService.class);
            TagQuery tagQuery = GwtKapuaModelConverter.convertTagQuery(loadConfig, gwtTagQuery);
            TagListResult tags = tagService.query(tagQuery);
            if (!tags.isEmpty()) {
                if (tags.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(tagService.count(tagQuery)).intValue();

                } else {
                    totalLength = tags.getSize();
                }
                for (Tag g : tags.getItems()) {
                    gwtTagList.add(KapuaGwtModelConverter.convert(g));
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
            KapuaId tagId2 = GwtKapuaModelConverter.convert(tagId);
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
            KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId tagId = KapuaEid.parseCompactId(tagShortId);
            Tag tag = tagService.find(scopeId, tagId);

            if (tag != null) {
                // gwtTagDescription.add(new GwtGroupedNVPair("Entity", "Scope
                // Id", KapuaGwtModelConverter.convert(tag.getScopeId())));
                gwtTagDescription.add(new GwtGroupedNVPair("Tag", "Tag Name", tag.getName()));
                gwtTagDescription.add(new GwtGroupedNVPair("Entity", "Modified On", tag.getModifiedOn().toString()));
                gwtTagDescription.add(new GwtGroupedNVPair("Entity", "Modified By", tag.getModifiedBy().toCompactId()));
                gwtTagDescription.add(new GwtGroupedNVPair("Entity", "Created On", tag.getCreatedOn().toString()));
                gwtTagDescription.add(new GwtGroupedNVPair("Entity", "Created By", tag.getCreatedBy().toCompactId()));

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
        TagQuery query = tagFactory.newQuery(GwtKapuaModelConverter.convert(scopeId));
        try {
            TagListResult result = tagService.query(query);
            for (Tag tag : result.getItems()) {
                tagList.add(KapuaGwtModelConverter.convert(tag));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return tagList;
    }

}
