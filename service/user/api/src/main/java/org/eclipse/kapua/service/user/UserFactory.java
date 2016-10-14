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
package org.eclipse.kapua.service.user;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * User factory service definition.
 * 
 * @since 1.0
 * 
 */
public interface UserFactory extends KapuaObjectFactory
{

    /**
     * Creates a new {@link UserCreator} for the specified name
     * 
     * @param scopedId
     * @param name
     * @return
     */
    public UserCreator newCreator(KapuaId scopedId, String name);
    
    /**
     * Creates a new user entity
     * 
     * @return
     */
    public User newUser();

    /**
     * Creates a new user query for the specified scope identifier
     * 
     * @param scopedId
     * @return
     */
    public UserQuery newQuery(KapuaId scopedId);
    
    /**
     * Creates a new user result list
     * 
     * @return
     */
    public UserListResult newUserListResult();
}
