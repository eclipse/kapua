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
package org.eclipse.kapua.model;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * {@link KapuaEntity} definition.
 * <p>
 * All the {@link KapuaEntity}s will be an extension of this entity.
 *
 * @since 1.0.0
 */
@XmlType(propOrder = {
        "id",
        "scopeId",
        "createdOn",
        "createdBy"})
public interface KapuaEntity extends KapuaSerializable {

    @XmlTransient
    String getType();

    /**
     * Gets the unique {@link KapuaId}
     *
     * @return the unique {@link KapuaId}
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getId();

    /**
     * Sets the unique {@link KapuaId}
     *
     * @param id the unique {@link KapuaId}
     * @since 1.0.0
     */
    void setId(KapuaId id);

    /**
     * Gets the scope {@link KapuaId}
     *
     * @return the scope {@link KapuaId}
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Sets the scope {@link KapuaId}
     *
     * @param scopeId the scope {@link KapuaId}
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);

    /**
     * Gets the creation date.
     *
     * @return the creation date.
     * @since 1.0.0
     */
    @XmlElement(name = "createdOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getCreatedOn();

    /**
     * Gets the identity {@link KapuaId} who has created this {@link KapuaEntity}
     *
     * @return the identity {@link KapuaId} who has created this {@link KapuaEntity}
     * @since 1.0.0
     */
    @XmlElement(name = "createdBy")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getCreatedBy();
}
