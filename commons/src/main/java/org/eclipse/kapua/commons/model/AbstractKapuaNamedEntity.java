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
package org.eclipse.kapua.commons.model;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * {@link KapuaNamedEntity} {@code abstract} implementation.
 *
 * @since 1.0.0
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractKapuaNamedEntity extends AbstractKapuaUpdatableEntity implements KapuaNamedEntity {

    @Basic
    @Column(name = "name", nullable = false, updatable = true)
    protected String name;

    @Basic
    @Column(name = "description", nullable = true, updatable = true)
    protected String description;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected AbstractKapuaNamedEntity() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.0.0
     */
    public AbstractKapuaNamedEntity(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @param name    The name of this {@link org.eclipse.kapua.model.KapuaEntity}.
     * @since 1.0.0
     */
    public AbstractKapuaNamedEntity(KapuaId scopeId, String name) {
        super(scopeId);

        setName(name);
    }

    /**
     * Constructor.
     * <p>
     * It can be used to clone the {@link KapuaUpdatableEntity}.
     *
     * @since 1.0.0
     */
    protected AbstractKapuaNamedEntity(KapuaNamedEntity kapuaNamedEntity) {
        super(kapuaNamedEntity);

        setName(kapuaNamedEntity.getName());
        setDescription(kapuaNamedEntity.getDescription());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
