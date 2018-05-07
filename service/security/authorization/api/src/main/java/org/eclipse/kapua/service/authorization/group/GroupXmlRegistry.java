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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class GroupXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final GroupFactory GROUP_FACTORY = LOCATOR.getFactory(GroupFactory.class);

    /**
     * Creates a new {@link Group} instance
     *
     * @return The newly created {@link Group} instance.
     * @since 1.0.0
     */
    public Group newGroup() {
        return GROUP_FACTORY.newEntity(null);
    }

    /**
     * Creates a new {@link GroupCreator} instance.
     *
     * @return The newly created {@link GroupCreator} instance.
     * @since 1.0.0
     */
    public GroupCreator newGroupCreator() {
        return GROUP_FACTORY.newCreator(null, null);
    }

    /**
     * Creates a new {@link GroupListResult} instance.
     *
     * @return The newly created {@link GroupListResult} instance.
     * @since 1.0.0
     */
    public GroupListResult newGroupListResult() {
        return GROUP_FACTORY.newListResult();
    }

    /**
     * Creates a new {@link GroupQuery} instance.
     *
     * @return The newly created {@link GroupQuery} instance.
     * @since 1.0.0
     */
    public GroupQuery newQuery() {
        return GROUP_FACTORY.newQuery(null);
    }
}
