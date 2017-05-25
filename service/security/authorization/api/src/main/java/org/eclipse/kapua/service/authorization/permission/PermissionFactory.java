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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.group.Group;

/**
 * {@link Permission} object factory.
 * 
 * @since 1.0.0
 */
public interface PermissionFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link Permission} implementing object with the provided parameters.
     * 
     * @param domain
     *            The {@link Domain} of the new {@link Permission}.
     * @param action
     *            The {@link Action} of the new {@link Permission}.
     * @param targetScopeId
     *            The target scope id of the new {@link Permission}.
     * @return A instance of the implementing class of {@link Permission}.
     * @since 1.0.0
     */
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId);

    /**
     * Instantiate a new {@link Permission} implementing object with the provided parameters.
     * 
     * @param domain
     *            The {@link Domain} of the new {@link Permission}.
     * @param action
     *            The {@link Action} of the new {@link Permission}.
     * @param targetScopeId
     *            The target scope id of the new {@link Permission}.
     * @param groupId
     *            The {@link Group} id that this {@link Permission} gives access.
     * @return A instance of the implementing class of {@link Permission}.
     * @since 1.0.0
     */
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId, KapuaId groupId);

    /**
     * Instantiate a new {@link Permission} implementing object with the provided parameters.
     * 
     * @param domain
     *            The {@link Domain} of the new {@link Permission}.
     * @param action
     *            The {@link Action} of the new {@link Permission}.
     * @param targetScopeId
     *            The target scope id of the new {@link Permission}.
     * @param groupId
     *            The {@link Group} id that this {@link Permission} gives access.
     * @param forwardable
     *            If the {@link Permission} is forward-able to children scopeIds
     * @return A instance of the implementing class of {@link Permission}.
     * @since 1.0.0
     */
    public Permission newPermission(Domain domain, Actions action, KapuaId targetScopeId, KapuaId groupId, boolean forwardable);
}
