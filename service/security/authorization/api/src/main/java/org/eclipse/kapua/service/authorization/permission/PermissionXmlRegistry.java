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
package org.eclipse.kapua.service.authorization.permission;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class PermissionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final PermissionFactory factory = locator.getFactory(PermissionFactory.class);

    /**
     * Creates a new {@link Permission} instance
     * 
     * @return A new {@link Permission} instance
     * 
     * @since 1.0.0
     */
    public Permission newPermission() {
        return factory.newPermission(null, null, null, null);
    }
}
