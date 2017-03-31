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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.devicegroup.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;

import com.google.common.collect.Lists;

public class DeviceGroupDomain extends AbstractKapuaEntity implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = 6853259728564558637L;
    private String name = "deviceGroup";
    private String serviceName = "deviceGroupService";
    private Set<Actions> actions = new HashSet<>(
            Lists.newArrayList(Actions.read, Actions.write, Actions.delete));

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Set<Actions> getActions() {
        return actions;
    }

    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }

}
