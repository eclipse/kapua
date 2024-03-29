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
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class RoleXmlRegistry {

    private final RoleFactory roleFactory = KapuaLocator.getInstance().getFactory(RoleFactory.class);

    /**
     * Creates a new {@link Role} instance.
     *
     * @return The newly created {@link Role} instance.
     * @since 1.0.0
     */
    public Role newRole() {
        return roleFactory.newEntity(null);
    }

    /**
     * Creates a new {@link RoleCreator} instance.
     *
     * @return The newly created {@link RoleCreator} instance.
     * @since 1.0.0
     */
    public RoleCreator newRoleCreator() {
        return roleFactory.newCreator(null);
    }

    /**
     * Creates a new {@link RoleListResult} instance.
     *
     * @return The newly created {@link RoleListResult} instance.
     * @since 1.0.0
     */
    public RoleListResult newRoleListResult() {
        return roleFactory.newListResult();
    }

    /**
     * Creates a new {@link RoleQuery} instance.
     *
     * @return The newly created {@link RoleQuery} instance.
     * @since 1.0.0
     */
    public RoleQuery newQuery() {
        return roleFactory.newQuery(null);
    }
}
