/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
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
import org.eclipse.kapua.model.xml.DateXmlAdapter;

/**
 * Kapua base entity definition.<br>
 * All the Kapua entities will be an extension of this entity.
 * 
 * @since 1.0.0
 */
@XmlType(propOrder = { //
        "id", //
        "scopeId", //
        "createdOn", //
        "createdBy" })
public interface KapuaEntity extends KapuaSerializable {

    @XmlTransient
    public String getType();

    /**
     * Get the Kapua identifier for the entity
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getId();

    /**
     * Set the Kapua identifier for the entity
     * 
     * @param id
     * @since 1.0.0
     */
    public void setId(KapuaId id);

    /**
     * Get the Kapua scope identifier for the entity
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getScopeId();

    /**
     * Set the Kapua scope identifier for the entity.
     * 
     * @param scopeId
     *            The Kapua scope identifier to set.
     * @since 1.0.0
     */
    public void setScopeId(KapuaId scopeId);

    /**
     * Get the created on date
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "createdOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    public Date getCreatedOn();

    /**
     * Set the created by Kapua identifier
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "createdBy")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getCreatedBy();
}
