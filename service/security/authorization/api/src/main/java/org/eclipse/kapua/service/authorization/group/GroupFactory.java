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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link Group} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface GroupFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link GroupCreator} implementing object with the provided parameters.
     *
     * @param scopeId
     *            The scope id of the group.
     * @param name
     *            The {@link Group} name to set.
     * @return A instance of the implementing class of {@link Group}.
     * @since 1.0.0
     */
    public GroupCreator newCreator(KapuaId scopeId, String name);

    /**
     * Instantiate a new {@link Group} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link Group}.
     * @since 1.0.0
     */
    public Group newGroup(KapuaId scopeId);

    /**
     * Instantiate a new {@link GroupListResult} implementing object.
     * 
     * @return A instance of the implementing class of {@link GroupListResult}.
     * @since 1.0.0
     */
    public GroupListResult newGroupListResult();

    /**
     * Instantiate a new {@link GroupQuery} implementing object.
     * 
     * @param scopeId
     *            The scope id of the {@link GroupQuery}.
     * @return A instance of the implementing class of {@link GroupQuery}.
     * @since 1.0.0
     */
    public GroupQuery newQuery(KapuaId scopeId);
}
