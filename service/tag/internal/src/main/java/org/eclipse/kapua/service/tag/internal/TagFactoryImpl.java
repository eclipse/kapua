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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;

/**
 * {@link TagFactory} implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class TagFactoryImpl implements TagFactory {

    @Override
    public TagCreator newCreator(KapuaId scopeId, String name) {
        TagCreator creator = newCreator(scopeId);
        creator.setName(name);
        return creator;
    }

    @Override
    public Tag newEntity(KapuaId scopeId) {
        return new TagImpl(scopeId);
    }

    @Override
    public TagListResult newListResult() {
        return new TagListResultImpl();
    }

    @Override
    public TagQuery newQuery(KapuaId scopeId) {
        return new TagQueryImpl(scopeId);
    }

    @Override
    public TagCreator newCreator(KapuaId scopeId) {
        return new TagCreatorImpl(scopeId);
    }
}
