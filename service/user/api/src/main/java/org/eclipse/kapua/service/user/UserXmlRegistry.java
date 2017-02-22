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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * User xml factory class
 * 
 * @since 1.0
 * 
 */
@XmlRegistry
public class UserXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UserFactory factory = locator.getFactory(UserFactory.class);
    
    /**
     * Creates a new user instance
     * 
     * @return
     */
    public User newUser()
    {
        return factory.newUser();
    }

    /**
     * Creates a new user creator instance
     * 
     * @return
     */
    public UserCreator newUserCreator()
    {
        return factory.newCreator(null, null);
    }

    /**
     * Creates new user list result
     * 
     * @return
     */
    public UserListResult newUserListResult()
    {
        return factory.newUserListResult();
    }
    
    public UserQuery newQuery() {
        return factory.newQuery(null);
    }
}
