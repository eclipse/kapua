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
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class PermissionXmlRegistry {

    private final PermissionFactory permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);

    /**
     * Creates a new {@link Permission} instance
     *
     * @return A new {@link Permission} instance
     * @since 1.0.0
     */
    public Permission newPermission() {
        return permissionFactory.newPermission(null, null, null, null);
    }
}
