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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua base entity reference abstract implementation.
 * 
 * @see KapuaEntity
 * 
 * @since 1.0
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
    protected Date     createdOn;

    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "created_by", nullable = false, updatable = false))
    })
    protected KapuaEid createdBy;

    /**
     * Constructor
     */
    protected AbstractKapuaEntity() {
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AbstractKapuaEntity(KapuaId scopeId) {
        this();
        if (scopeId != null) {
            this.scopeId = new KapuaEid(scopeId.getId());
        }
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
    	this.id = (KapuaEid)id;
    }

    /**
     * Set scope identifier
     * 
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = (KapuaEid)scopeId;
    }

    @Override
    public Date getCreatedOn() {
        return createdOn;
    }

    @Override
    public KapuaId getCreatedBy() {
        return createdBy;
    }

    /**
     * Before update action to correctly set the modified on and modified by fields
     * 
     * @throws KapuaException
     */
    @PrePersist
    protected void prePersistsAction()
            throws KapuaException {
        this.id = new KapuaEid(IdGenerator.generate());

        this.createdBy = new KapuaEid(KapuaSecurityUtils.getSession().getUserId().getId());
        this.createdOn = new Date();
    }

    /**
     * Set the created on date
     * 
     * @param createdOn
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set the created by identifier
     * 
     * @param createdBy
     */
    public void setCreatedBy(KapuaId createdBy) {
        this.createdBy = (KapuaEid)createdBy;
    }
}
