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
package org.eclipse.kapua.service.authorization.access;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class AccessPermissionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessPermissionFactory factory = locator.getFactory(AccessPermissionFactory.class);

    /**
     * Creates a new {@link AccessPermission} instance
     * 
     * @return
     */
    public AccessPermission newAccessPermission() {
        return factory.newAccessPermission();
    }

    /**
     * Creates a new {@link AccessPermission} instance
     * 
     * @return
     */
    public AccessPermissionCreator newCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new {@link AccessPermission} instance
     * 
     * @return
     */
    public AccessPermissionListResult newAccessPermissionListResult() {
        return factory.newAccessPermissionListResult();
    }
}
