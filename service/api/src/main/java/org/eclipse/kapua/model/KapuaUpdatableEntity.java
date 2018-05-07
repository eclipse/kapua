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
 * Kapua updatable entity definition.
 *
 * @since 1.0
 */
@XmlType(propOrder = { //
        "modifiedOn", //
        "modifiedBy", //
        "optlock" //
})
public interface KapuaUpdatableEntity extends KapuaEntity {

    /**
     * Get modified on date
     *
     * @return
     */
    @XmlElement(name = "modifiedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getModifiedOn();

    /**
     * Get modified by kapua identifier
     *
     * @return
     */
    @XmlElement(name = "modifiedBy")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    KapuaId getModifiedBy();

    /**
     * Get optlock
     *
     * @return
     */
    @XmlElement(name = "optlock")
    int getOptlock();

    /**
     * Set optlock
     *
     * @param optlock
     */
    void setOptlock(int optlock);

    /**
     * Get entity attributes
     *
     * @return
     * @throws KapuaException
     */
    @XmlTransient
    Properties getEntityAttributes()
            throws KapuaException;

    /**
     * Set entity attributes
     *
     * @param props
     * @throws KapuaException
     */
    void setEntityAttributes(Properties props)
            throws KapuaException;

    /**
     * Get the property entities
     *
     * @return
     * @throws KapuaException
     */
    @XmlTransient
    Properties getEntityProperties()
            throws KapuaException;

    /**
     * Set the property entities
     *
     * @param props
     * @throws KapuaException
     */
    void setEntityProperties(Properties props)
            throws KapuaException;
}
