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
package org.eclipse.kapua.service.authorization.domain.shiro;

import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.permission.Actions;

/**
 * Role creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class DomainCreatorImpl extends AbstractKapuaEntityCreator<Domain> implements DomainCreator {

    private static final long serialVersionUID = -4676187845961673421L;

    private String name;
    private String serviceName;
    private Set<Actions> actions;

    /**
     * Constructor
     * 
     * @param name
     *            The name to set for this {@link DomainCreator}.
     * @param serviceName
     *            The service name that creates this {@link DomainCreator}.
     * 
     * @since 1.0.0
     */
    public DomainCreatorImpl(String name, String serviceName) {
        super((KapuaId) null);

        setName(name);
        setServiceName(serviceName);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }
}
