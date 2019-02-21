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

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Properties;

/**
 * {@link KapuaUpdatableEntity} definition.
 *
 * @since 1.0.0
 */
@XmlType(propOrder = {
        "modifiedOn",
        "modifiedBy",
        "optlock"
})
public interface KapuaUpdatableEntity extends KapuaEntity {

    /**
     * Gets the last date that this {@link KapuaEntity} has been updated.
     *
     * @return the last date that this {@link KapuaEntity} has been updated.
     * @since 1.0.0
     */
    @XmlElement(name = "modifiedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getModifiedOn();

    /**
     * Get the last identity {@link KapuaId} that has updated this {@link KapuaEntity}
     *
     * @return the last identity {@link KapuaId} that has updated this {@link KapuaEntity}
     * @since 1.0.0
     */
    @XmlElement(name = "modifiedBy")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getModifiedBy();

    /**
     * Gets the optlock
     *
     * @return the optlock
     * @since 1.0.0
     */
    @XmlElement(name = "optlock")
    int getOptlock();

    /**
     * Sets the optlock
     *
     * @param optlock the optlock
     * @since 1.0.0
     */
    void setOptlock(int optlock);

    /**
     * Gets the attributes
     *
     * @return the attributes
     * @throws KapuaException
     */
    @XmlTransient
    Properties getEntityAttributes()
            throws KapuaException;

    /**
     * Sets the attributes
     *
     * @param props the attributes
     * @throws KapuaException
     * @since 1.0.0
     */
    void setEntityAttributes(Properties props)
            throws KapuaException;

    /**
     * Gets the property entities
     *
     * @return the property entities
     * @throws KapuaException
     * @since 1.0.0
     */
    @XmlTransient
    Properties getEntityProperties()
            throws KapuaException;

    /**
     * Sets the property entities
     *
     * @param props the property entities
     * @throws KapuaException
     * @since 1.0.0
     */
    void setEntityProperties(Properties props)
            throws KapuaException;
}
