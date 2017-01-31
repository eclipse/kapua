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
import org.eclipse.kapua.commons.model.subject.SubjectImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;

/**
 * {@link KapuaEntity} abstract implementation.
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
            @AttributeOverride(name = "subjectType", column = @Column(name = "created_by_type", nullable = false, updatable = false)),
            @AttributeOverride(name = "subjectId.eid", column = @Column(name = "created_by_id", nullable = false, updatable = false))
    })
    private SubjectImpl createdBy;

    /**
     * Constructor
     */
    protected AbstractKapuaEntity() {
    }

    /**
     * Constructor
     */
    protected AbstractKapuaEntity(AbstractKapuaEntity entity) {
        this();

        setId(entity.getId());
        setScopeId(entity.getScopeId());
        setCreatedBy(entity.getCreatedBy());
        setCreatedOn(entity.getCreatedOn());
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AbstractKapuaEntity(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = id != null ? new KapuaEid(id) : null;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    /**
     * Set scope identifier
     * 
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId != null ? new KapuaEid(scopeId) : null;
    }

    @Override
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Set the created on date
     * 
     * @param createdOn
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public Subject getCreatedBy() {
        return createdBy;
    }

    /**
     * Set the created by identifier
     * 
     * @param createdBy
     */
    public void setCreatedBy(Subject createdBy) {
        this.createdBy = createdBy != null ? new SubjectImpl(createdBy) : null;
    }

    /**
     * Before persist action we need to correctly set the {@link #createdOn} and {@link #createdBy} fields.
     * 
     * @since 1.0.0
     */
    @PrePersist
    protected void prePersistsAction() {
        setId(new KapuaEid(IdGenerator.generate()));
        setCreatedBy(KapuaSecurityUtils.getSession().getSubject());
        setCreatedOn(new Date());
    }
}
