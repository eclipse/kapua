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
package org.eclipse.kapua.service.tag;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link Tag} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface TagFactory extends KapuaEntityFactory<Tag, TagCreator, TagQuery, TagListResult> {

    /**
     * Instantiate a new {@link TagCreator} implementing object with the provided parameters.
     *
     * @param scopeId
     *            The scope id of the tag.
     * @param name
     *            The {@link Tag} name to set.
     * @return A instance of the implementing class of {@link Tag}.
     * @since 1.0.0
     */
    public TagCreator newCreator(KapuaId scopeId, String name);

}
