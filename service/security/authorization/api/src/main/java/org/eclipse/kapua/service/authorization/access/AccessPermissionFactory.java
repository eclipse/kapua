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
 * {@link AccessPermission} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface AccessPermissionFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link AccessPermission} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessPermission}.
     * @since 1.0.0
     */
    public AccessPermission newAccessPermission(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessPermissionCreator} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessPermissionCreator}.
     * @since 1.0.0
     */
    public AccessPermissionCreator newCreator(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessPermissionQuery} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessPermissionQuery}.
     * @since 1.0.0
     */
    public AccessPermissionQuery newQuery(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessPermissionListResult} implementing object.
     * 
     * @return A instance of the implementing class of {@link AccessPermissionListResult}.
     * @since 1.0.0
     */
    public AccessPermissionListResult newAccessPermissionListResult();

    /**
     * Instantiate a new {@link AccessPermission} implementing object.
     * 
     * @return A instance of the implementing class of {@link AccessPermission}.
     * @since 1.0.0
     */
    public AccessPermission newAccessPermission();
}
