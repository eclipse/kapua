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
package org.eclipse.kapua.service.authorization.group;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class GroupXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final GroupFactory factory = locator.getFactory(GroupFactory.class);

    /**
     * Creates a new role instance
     * 
     * @return
     */
    public Group newGroup() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new role creator instance
     * 
     * @return
     */
    public GroupCreator newGroupCreator() {
        return factory.newCreator(null, null);
    }

    /**
     * Creates a new role creator instance
     * 
     * @return
     */
    public GroupListResult newGroupListResult() {
        return factory.newListResult();
    }

    public GroupQuery newQuery() {
        return factory.newQuery(null);
    }
}
