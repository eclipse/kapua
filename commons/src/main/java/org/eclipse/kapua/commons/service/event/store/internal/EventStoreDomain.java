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
package org.eclipse.kapua.commons.service.event.store.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;

import com.google.common.collect.Lists;

/**
 * KapuaEvent permission domain.<br>
 * KapuaEvent to describe the kapuaEvent domain in the {@link Permission}
 * 
 * @since 1.0
 *
 */
public class EventStoreDomain extends AbstractKapuaEntity implements Domain {

    private static final long serialVersionUID = 2624648055164592528L;

    private String name = "eventStoreRecird";
    private String serviceName = "eventStoreService";
    private Set<Actions> actions = new HashSet<>(Lists.newArrayList(Actions.read, Actions.delete, Actions.write));

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public void setGroupable(boolean groupable) {
        //this domain is not groupable so the set method doesn't set any field
    }

    @Override
    public boolean getGroupable() {
        return false;
    }
}