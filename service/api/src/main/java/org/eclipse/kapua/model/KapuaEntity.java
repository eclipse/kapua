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
package org.eclipse.kapua.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.model.subject.SubjectAdapter;

/**
 * {@link KapuaEntity} definition.<br>
 * All the {@link KapuaEntity}es will be an extension of this entity.
 * 
 * This base class provides the basic properties of a model object in Kapua.
 * 
 * @since 1.0.0
 */
@XmlType(propOrder = { "id", "scopeId", "createdOn", "createdBy" })
public interface KapuaEntity extends KapuaSerializable {

    @XmlTransient
    public String getType();

    /**
     * Gets the {@link KapuaId} of this {@link KapuaEntity}.
     * 
     * @return The {@link KapuaId} of this {@link KapuaEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getId();

    /**
     * Sets the {@link KapuaId} of this {@link KapuaEntity}.
     * 
     * @param id
     *            The {@link KapuaId} to set.
     * @since 1.0.0
     */
    public void setId(KapuaId id);

    /**
     * Gets the scope {@link KapuaId} of this {@link KapuaEntity}.
     * 
     * @return The scope {@link KapuaId} of this {@link KapuaEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getScopeId();

    /**
     * Gets the {@link Date} of creation of this {@link KapuaEntity}.
     * 
     * @return The created on {@link Date} of this {@link KapuaEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "createdOn")
    public Date getCreatedOn();

    /**
     * Gets the {@link Subject} that has created this {@link KapuaEntity}.
     * 
     * @return The {@link Subject} that created this {@link KapuaEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "createdBy")
    @XmlJavaTypeAdapter(SubjectAdapter.class)
    public Subject getCreatedBy();
}
