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
package org.eclipse.kapua.commons.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua named-updatable entity default abstract implementation.
 * 
 * @since 1.0
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractKapuaNamedEntity extends AbstractKapuaUpdatableEntity implements KapuaNamedEntity {

    @Basic
    @Column(name = "name")
    protected String name;

    /**
     * Constructor
     */
    protected AbstractKapuaNamedEntity() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AbstractKapuaNamedEntity(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor
     * 
     * @param scopeId
     * @param name
     */
    public AbstractKapuaNamedEntity(KapuaId scopeId,
            String name) {
        super(scopeId);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
