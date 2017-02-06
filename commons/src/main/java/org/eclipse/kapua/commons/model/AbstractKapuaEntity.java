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
package org.eclipse.kapua.commons.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaEntity} reference abstract implementation.
 * 
 * @see KapuaEntity
 * 
 * @since 1.0.0
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractKapuaEntity implements KapuaEntity, Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "id", nullable = false, updatable = false))
    })
    protected KapuaEid id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "scope_id", nullable = false, updatable = false))
    })
    protected KapuaEid scopeId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false, updatable = false)
    protected Date createdOn;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "created_by", nullable = false, updatable = false))
    })
    protected KapuaEid createdBy;

    /**
     * Protected default constructor.<br>
     * Required by JPA.
     * 
     * @since 1.0.0
     */
    protected AbstractKapuaEntity() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param scopeId
     *            The scope {@link KapuaId} to set for this {@link KapuaEntity}.
     * @since 1.0.0
     */
    public AbstractKapuaEntity(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    /**
     * Constructor.
     */
    protected AbstractKapuaEntity(KapuaEntity entity) {
        this();

        setId(entity.getId());
        setScopeId(entity.getScopeId());
        setCreatedBy(entity.getCreatedBy());
        setCreatedOn(entity.getCreatedOn());
    }

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = id != null ? (id instanceof KapuaEid ? (KapuaEid) id : new KapuaEid(id)) : null;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    /**
     * Sets scope identifier
     * 
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId != null ? (scopeId instanceof KapuaEid ? (KapuaEid) scopeId : new KapuaEid(scopeId)) : null;
    }

    @Override
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on date
     * 
     * @param createdOn
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public KapuaId getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by identifier
     * 
     * @param createdBy
     */
    public void setCreatedBy(KapuaId createdBy) {
        this.createdBy = createdBy != null ? (createdBy instanceof KapuaEid ? (KapuaEid) createdBy : new KapuaEid(createdBy)) : null;
    }

    /**
     * Before create action sets the {@link KapuaEntity} {@link #id}, {@link #createdBy} and {@link #createdOn}.
     * 
     * @since 1.0.0
     */
    @PrePersist
    protected void prePersistsAction() {
        setId(new KapuaEid(IdGenerator.generate()));

        setCreatedBy(KapuaSecurityUtils.getSession().getUserId());
        setCreatedOn(new Date());
    }

}
