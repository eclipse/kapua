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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;

import java.util.Set;

/**
 * {@link DomainCreator} implementation
 *
 * @since 1.0.0
 */
public class DomainCreatorImpl extends AbstractKapuaEntityCreator<Domain> implements DomainCreator {

    private static final long serialVersionUID = -4676187845961673421L;

    private String name;
    private Set<Actions> actions;
    private boolean groupable;

    /**
     * Constructor
     *
     * @param name The name to set for this {@link DomainCreator}.
     * @since 1.0.0
     */
    public DomainCreatorImpl(String name) {
        super((KapuaId) null);

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

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public boolean getGroupable() {
        return groupable;
    }

    @Override
    public void setGroupable(boolean groupable) {
        this.groupable = groupable;
    }

}
