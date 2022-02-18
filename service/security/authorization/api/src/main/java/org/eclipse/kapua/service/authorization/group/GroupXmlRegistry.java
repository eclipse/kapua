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
