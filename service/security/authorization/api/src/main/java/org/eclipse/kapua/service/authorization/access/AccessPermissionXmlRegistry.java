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
public class AccessPermissionXmlRegistry {

    private final AccessPermissionFactory accessPermissionFactory = KapuaLocator.getInstance().getFactory(AccessPermissionFactory.class);

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermission newAccessPermission() {
        return accessPermissionFactory.newEntity(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermissionCreator newCreator() {
        return accessPermissionFactory.newCreator(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermissionListResult newAccessPermissionListResult() {
        return accessPermissionFactory.newListResult();
    }

    public AccessPermissionQuery newQuery() {
        return accessPermissionFactory.newQuery(null);
    }
}
