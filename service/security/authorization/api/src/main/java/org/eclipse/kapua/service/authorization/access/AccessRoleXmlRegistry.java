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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class AccessRoleXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccessRoleFactory ACCESS_ROLE_FACTORY = LOCATOR.getFactory(AccessRoleFactory.class);

    /**
     * Creates a new {@link AccessRole} instance
     *
     * @return
     */
    public AccessRole newAccessRole() {
        return ACCESS_ROLE_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link AccessRole} instance
     *
     * @return
     */
    public AccessRoleCreator newCreator() {
        return ACCESS_ROLE_FACTORY.newCreator(null);
    }

    /**
     * Creates a new {@link AccessRole} instance
     *
     * @return
     */
    public AccessRoleListResult newAccessRoleListResult() {
        return ACCESS_ROLE_FACTORY.newListResult();
    }

    public AccessRoleQuery newQuery() {
        return ACCESS_ROLE_FACTORY.newQuery(null);
    }
}
