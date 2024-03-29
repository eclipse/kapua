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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class AccessRoleXmlRegistry {

    private final AccessRoleFactory accessRoleFactory = KapuaLocator.getInstance().getFactory(AccessRoleFactory.class);

    /**
     * Creates a new {@link AccessRole} instance
     *
     * @return
     */
    public AccessRole newAccessRole() {
        return accessRoleFactory.newEntity(null);
    }

    /**
     * Creates a new {@link AccessRole} instance
     *
     * @return
     */
    public AccessRoleCreator newCreator() {
        return accessRoleFactory.newCreator(null);
    }

    /**
     * Creates a new {@link AccessRole} instance
     *
     * @return
     */
    public AccessRoleListResult newAccessRoleListResult() {
        return accessRoleFactory.newListResult();
    }

    public AccessRoleQuery newQuery() {
        return accessRoleFactory.newQuery(null);
    }
}
