/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.user;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link User} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class UserXmlRegistry {

    private final UserFactory userFactory = KapuaLocator.getInstance().getFactory(UserFactory.class);

    /**
     * Creates a new user instance
     *
     * @return
     */
    public User newUser() {
        return userFactory.newEntity(null);
    }

    /**
     * Creates a new user creator instance
     *
     * @return
     */
    public UserCreator newUserCreator() {
        return userFactory.newCreator(null, null);
    }

    /**
     * Creates new user list result
     *
     * @return
     */
    public UserListResult newUserListResult() {
        return userFactory.newListResult();
    }

    public UserQuery newQuery() {
        return userFactory.newQuery(null);
    }
}
