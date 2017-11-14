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
package org.eclipse.kapua.app.console.module.tag.shared.util;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.internal.TagPredicates;

public class GwtKapuaTagModelConverter {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final TagFactory TAG_FACTORY = LOCATOR.getFactory(TagFactory.class);

    private GwtKapuaTagModelConverter() {
    }

    /**
     * Converts a {@link GwtTagQuery} into a {@link TagQuery} object for backend usage
     *
     * @param loadConfig  the load configuration
     * @param gwtTagQuery the {@link GwtTagQuery} to convertKapuaId
     * @return the converted {@link TagQuery}
     */
    public static TagQuery convertTagQuery(PagingLoadConfig loadConfig, GwtTagQuery gwtTagQuery) {

        // Predicates conversion
        AndPredicate andPredicate = new AndPredicate();
        if (gwtTagQuery.getName() != null && !gwtTagQuery.getName().isEmpty()) {
            andPredicate.and(new AttributePredicate<String>(TagPredicates.NAME, gwtTagQuery.getName(), Operator.LIKE));
        }
        if (!gwtTagQuery.getIds().isEmpty()) {
            int i = 0;
            KapuaId[] tagIds = new KapuaId[gwtTagQuery.getIds().size()];
            for (String gwtTagId : gwtTagQuery.getIds()) {
                tagIds[i++] = GwtKapuaCommonsModelConverter.convertKapuaId(gwtTagId);
            }

            andPredicate.and(new AttributePredicate<KapuaId[]>(TagPredicates.ENTITY_ID, tagIds));
        }

        // Sort order conversion
        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? TagPredicates.NAME : loadConfig.getSortField();
        if (sortField.equals("tagName")) {
            sortField = TagPredicates.NAME;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);

        // Query conversion
        TagQuery tagQuery = TAG_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtTagQuery.getScopeId()));
        tagQuery.setPredicate(andPredicate);
        tagQuery.setSortCriteria(sortCriteria);
        tagQuery.setOffset(loadConfig.getOffset());
        tagQuery.setLimit(loadConfig.getLimit());

        return tagQuery;
    }
}
