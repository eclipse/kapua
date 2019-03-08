/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * {@link KapuaUpdatableEntity} {@code abstract} implementation.
 *
 * @since 1.0.0
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractKapuaUpdatableEntity extends AbstractKapuaEntity implements KapuaUpdatableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on")
    protected Date modifiedOn;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "modified_by"))
    })
    protected KapuaEid modifiedBy;

    @Version
    @Column(name = "optlock")
    protected int optlock;

    @Basic
    @Column(name = "attributes")
    protected String attributes;

    @Basic
    @Column(name = "properties")
    protected String properties;

    /**
     * Protected default constructor.<br>
     * Required by JPA.
     *
     * @since 1.0.0
     */
    protected AbstractKapuaUpdatableEntity() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set for this {@link KapuaUpdatableEntity}
     * @since 1.0.0
     */
    public AbstractKapuaUpdatableEntity(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor.
     * <p>
     * It can be used to clone the {@link KapuaUpdatableEntity}
     *
     * @throws KapuaException if {@link KapuaUpdatableEntity#getEntityAttributes()} and/or {@link KapuaUpdatableEntity#getEntityProperties()} cannot be parsed.
     * @since 1.0.0
     */
    protected AbstractKapuaUpdatableEntity(KapuaUpdatableEntity entity) throws KapuaException {
        super(entity);

        setModifiedOn(entity.getModifiedOn());
        setModifiedBy(entity.getModifiedBy());
        setOptlock(entity.getOptlock());
        setEntityAttributes(entity.getEntityAttributes());
        setEntityProperties(entity.getEntityProperties());
    }

    @Override
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the date of the last update
     *
     * @param modifiedOn the date of the last update
     * @since 1.0.0
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public KapuaId getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the identity {@link KapuaId} who has updated this {@link KapuaEntity}
     *
     * @param modifiedBy the identity {@link KapuaId} who has updated this {@link KapuaEntity}
     * @since 1.0.0
     */
    public void setModifiedBy(KapuaId modifiedBy) {
        this.modifiedBy = KapuaEid.parseKapuaId(modifiedBy);
    }

    @Override
    public int getOptlock() {
        return optlock;
    }

    @Override
    public void setOptlock(int optlock) {
        this.optlock = optlock;
    }

    // -------------------------------------------------
    //
    // Attributes APIs
    //
    // -------------------------------------------------

    @Override
    public Properties getEntityAttributes()
            throws KapuaException {
        Properties props = new Properties();
        if (attributes != null) {
            try {
                props.load(new StringReader(attributes));
            } catch (IOException e) {
                throw KapuaException.internalError(e);
            }
        }
        return props;
    }

    @Override
    public void setEntityAttributes(Properties props)
            throws KapuaException {
        if (props != null) {
            try {
                StringWriter writer = new StringWriter();
                props.store(writer, null);
                attributes = writer.toString();
            } catch (IOException e) {
                throw KapuaException.internalError(e);
            }
        }
    }

    // -------------------------------------------------
    //
    // Properties APIs
    //
    // -------------------------------------------------

    @Override
    public Properties getEntityProperties()
            throws KapuaException {
        Properties props = new Properties();
        if (properties != null) {
            try {
                props.load(new StringReader(properties));
            } catch (IOException e) {
                throw KapuaException.internalError(e);
            }
        }
        return props;
    }

    @Override
    public void setEntityProperties(Properties props)
            throws KapuaException {
        if (props != null) {
            try {
                StringWriter writer = new StringWriter();
                props.store(writer, null);
                properties = writer.toString();
            } catch (IOException e) {
                throw KapuaException.internalError(e);
            }
        }
    }

    /**
     * Before create action invokes {@link AbstractKapuaEntity#prePersistsAction()} and
     * sets {@link KapuaUpdatableEntity} {@link #modifiedBy} and {@link #modifiedOn}.
     *
     * @since 1.0.0
     */
    @Override
    protected void prePersistsAction() {
        super.prePersistsAction();

        setModifiedBy(getCreatedBy());
        setModifiedOn(getCreatedOn());
    }

    /**
     * Before update action sets the {@link KapuaUpdatableEntity} {@link #modifiedBy} and {@link #modifiedOn}.
     *
     * @since 1.0.0
     */
    @PreUpdate
    protected void preUpdateAction() {
        setModifiedBy(KapuaSecurityUtils.getSession().getUserId());
        setModifiedOn(new Date());
    }
}
