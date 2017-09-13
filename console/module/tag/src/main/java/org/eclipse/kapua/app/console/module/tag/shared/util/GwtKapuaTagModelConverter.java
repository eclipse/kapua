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

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.internal.TagPredicates;

public class GwtKapuaTagModelConverter {

    private GwtKapuaTagModelConverter() { }

    /**
     * Converts a {@link GwtTagQuery} into a {@link TagQuery} object for backend usage
     *
     * @param loadConfig  the load configuration
     * @param gwtTagQuery the {@link GwtTagQuery} to convertKapuaId
     * @return the converted {@link TagQuery}
     */
    public static TagQuery convertTagQuery(PagingLoadConfig loadConfig, GwtTagQuery gwtTagQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        TagFactory tagFactory = locator.getFactory(TagFactory.class);
        TagQuery tagQuery = tagFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtTagQuery.getScopeId()));
        if (gwtTagQuery.getName() != null && !gwtTagQuery.getName().isEmpty()) {
            tagQuery.setPredicate(new AttributePredicate<String>(TagPredicates.NAME, gwtTagQuery.getName(), Operator.LIKE));
        }
        tagQuery.setOffset(loadConfig.getOffset());
        tagQuery.setLimit(loadConfig.getLimit());

        return tagQuery;
    }
}
