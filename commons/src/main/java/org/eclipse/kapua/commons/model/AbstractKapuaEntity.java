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
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.generator.id.IdGeneratorService;

@SuppressWarnings("serial")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractKapuaEntity implements KapuaEntity, Serializable
{
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
    @Column(name = "created_on", nullable = false)
    protected Date     createdOn;

    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "created_by", nullable = false, updatable = false))
    })
    protected KapuaEid createdBy;

    protected AbstractKapuaEntity()
    {
    }

    public AbstractKapuaEntity(KapuaId scopeId)
    {
        this();
        if (scopeId != null) {
            this.scopeId = new KapuaEid(scopeId.getId());
        }
    }

    public KapuaId getScopeId()
    {
        return scopeId;
    }

    public KapuaId getId()
    {
        return id;
    }

    public void setId(KapuaId id)
    {
    	this.id = (KapuaEid)id;
    }

    public void setScopeId(KapuaId scopeId)
    {
        this.scopeId = (KapuaEid)scopeId;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public KapuaId getCreatedBy()
    {
        return createdBy;
    }

    @PrePersist
    protected void prePersistsAction()
        throws KapuaException
    {
        KapuaLocator locator = KapuaLocator.getInstance();
        IdGeneratorService idGenerator = locator.getService(IdGeneratorService.class);

        this.id = new KapuaEid(idGenerator.generate().getId());
        this.createdBy = new KapuaEid(KapuaSecurityUtils.getSession().getUserId().getId());
        this.createdOn = new Date();
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    public void setCreatedBy(KapuaId createdBy) {
        this.createdBy = (KapuaEid)createdBy;
    }
}
