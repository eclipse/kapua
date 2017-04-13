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
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link Role} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface RoleFactory extends KapuaEntityFactory<Role, RoleCreator, RoleQuery, RoleListResult> {
    
    /**
     * Instantiate a new {@link RolePermission} implementing object.
     * 
     * @return A instance of the implementing class of {@link RolePermission}.
     * @since 1.0.0
     */
    public RolePermission newRolePermission();
}
