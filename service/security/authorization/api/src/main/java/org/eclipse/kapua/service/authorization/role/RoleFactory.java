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
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Role factory service definition.
 * 
 * @since 1.0
 *
 */
public interface RoleFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link Role} for the specified scope identifier
     * 
     * @param scopeId
     * @return
     * 
     * @since 1.0
     */
    public Role newRole(KapuaId scopeId);

    /**
     * Creates a new {@link RoleCreator} for the specified scope identifier
     * 
     * @param scopeId
     * @return
     * 
     * @since 1.0
     */
    public RoleCreator newCreator(KapuaId scopeId);

    /**
     * Create a new role query for the specified scope identifier
     * 
     * @param scopeId
     * @return
     * 
     * @since 1.0
     */
    public RoleQuery newQuery(KapuaId scopeId);
}
