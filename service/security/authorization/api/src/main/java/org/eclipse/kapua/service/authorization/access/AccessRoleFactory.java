/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link AccessRole} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface AccessRoleFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link AccessRole} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessRole}.
     * @since 1.0.0
     */
    public AccessRole newAccessRole(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessRoleCreator} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessRoleCreator}.
     * @since 1.0.0
     */
    public AccessRoleCreator newCreator(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessRoleQuery} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessRoleQuery}.
     * @since 1.0.0
     */
    public AccessRoleQuery newQuery(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessRoleListResult} implementing object.
     * 
     * @return A instance of the implementing class of {@link AccessRoleListResult}.
     * @since 1.0.0
     */
    public AccessRoleListResult newAccessRoleListResult();
}
