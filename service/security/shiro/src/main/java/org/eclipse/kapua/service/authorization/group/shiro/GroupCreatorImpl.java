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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;

/**
 * {@link GroupCreator} implementation.
 * 
 * @since 1.0
 * 
 */
public class GroupCreatorImpl extends AbstractKapuaEntityCreator<Group> implements GroupCreator {

    private static final long serialVersionUID = -4676187845961673421L;

    private String name;

    /**
     * Constructor
     * 
     * @param scopeId
     *            The scope id to set.
     * @param name
     *            The name to set for this {@link DomainCreator}.
     * @since 1.0.0
     */
    public GroupCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId);
        setName(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
