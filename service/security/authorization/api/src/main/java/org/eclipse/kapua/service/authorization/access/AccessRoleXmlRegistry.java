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
public class AccessRoleXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessRoleFactory factory = locator.getFactory(AccessRoleFactory.class);

    /**
     * Creates a new {@link AccessRole} instance
     * 
     * @return
     */
    public AccessRole newAccessRole() {
        return factory.newAccessRole(null);
    }

    /**
     * Creates a new {@link AccessRole} instance
     * 
     * @return
     */
    public AccessRoleCreator newCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new {@link AccessRole} instance
     * 
     * @return
     */
    public AccessRoleListResult newAccessRoleListResult() {
        return factory.newAccessRoleListResult();
    }
}
