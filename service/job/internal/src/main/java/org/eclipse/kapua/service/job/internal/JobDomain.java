/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;

import com.google.common.collect.Lists;

public class JobDomain extends AbstractKapuaEntity implements Domain {

    private static final long serialVersionUID = 3782336558657796495L;

    private String name = "job";
    private String serviceName = "jobService";
    private Set<Actions> actions = new HashSet<>(Lists.newArrayList(Actions.read, Actions.delete, Actions.write, Actions.execute));

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
}
