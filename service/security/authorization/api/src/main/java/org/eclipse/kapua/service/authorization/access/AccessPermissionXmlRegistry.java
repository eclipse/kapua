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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccessPermissionFactory ACCESS_PERMISSION_FACTORY = LOCATOR.getFactory(AccessPermissionFactory.class);

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermission newAccessPermission() {
        return ACCESS_PERMISSION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermissionCreator newCreator() {
        return ACCESS_PERMISSION_FACTORY.newCreator(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     *
     * @return
     */
    public AccessPermissionListResult newAccessPermissionListResult() {
        return ACCESS_PERMISSION_FACTORY.newListResult();
    }

    public AccessPermissionQuery newQuery() {
        return ACCESS_PERMISSION_FACTORY.newQuery(null);
    }
}
