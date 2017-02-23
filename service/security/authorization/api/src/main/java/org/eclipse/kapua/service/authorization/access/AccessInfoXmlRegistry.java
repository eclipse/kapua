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
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;

@XmlRegistry
public class AccessInfoXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessInfoFactory factory = locator.getFactory(AccessInfoFactory.class);
    
    /**
     * Creates a new access info instance
     * 
     * @return
     */
    public AccessInfo newAccessInfo()
    {
        return factory.newAccessInfo();
    }
    
    /**
     * Creates a new access info creator instance
     * 
     * @return
     */
    public AccessInfoCreator newAccessInfoCreator()
    {
        return factory.newCreator(null);
    }
    
    /**
     * Creates a new {@link AccessInfoListResult} instance
     * 
     * @return
     */
    public AccessInfoListResult newAccessInfoListResult() {
        return factory.newAccessInfoListResult();
    }
    
    public AccessInfoQuery newQuery() {
        return factory.newQuery(null);
    }
}
