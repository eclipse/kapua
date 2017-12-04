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
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;

import com.google.common.collect.Lists;

/**
 * User domain.<br>
 * Used to describe the user domain in the {@link Permission}
 *
 * @since 1.0
 *
 */
public class CertificateDomain extends AbstractKapuaEntity implements Domain {

    private static final long serialVersionUID = 3782336558657796495L;

    private String name = "certificate";
    private String serviceName = "certificateService";
    private Set<Actions> actions = new HashSet<>(Lists.newArrayList(Actions.read, Actions.delete, Actions.write));
    private boolean groupable;

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

    public boolean getGroupable() {
        return groupable;
    }

    @Override
    public void setGroupable(boolean groupable) {
        this.groupable = groupable;
    }
}
