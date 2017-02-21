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
import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.subject.Subject;

/**
 * {@link KapuaUpdatableEntity} definition.
 * All the {@link KapuaEntity}es that can be updated will be an extension of this entity.
 * 
 * This class extends {@link KapuaEntity} and adds further properties to updatable model objects in Kapua.
 * 
 * @since 1.0.0
 * 
 */
@XmlType(propOrder = { "modifiedOn", "modifiedBy", "optlock" })
public interface KapuaUpdatableEntity extends KapuaEntity {

    /**
     * Gets the {@link Date} of last modification of this {@link KapuaUpdatableEntity}.
     * 
     * @return The last modified on {@link Date} of this {@link KapuaUpdatableEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "modifiedOn")
    public Date getModifiedOn();

    /**
     * Gets the {@link Subject} that has lastly modified this {@link KapuaUpdatableEntity}.
     * 
     * @return The {@link Subject} that has lastly modified this {@link KapuaUpdatableEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "modifiedBy")
    public Subject getModifiedBy();

    /**
     * Gets the version of this {@link KapuaUpdatableEntity}.<br>
     * This field is primarily intended to manage concurrent modifications of the same {@link KapuaUpdatableEntity}.
     * 
     * @return The version of this {@link KapuaUpdatableEntity}.
     * @since 1.0.0
     */
    @XmlElement(name = "optlock")
    public int getOptlock();

    /**
     * Sets version of this {@link KapuaUpdatableEntity}.
     * 
     * @param optlock
     *            The version to set for this {@link KapuaUpdatableEntity}.
     * @since 1.0.0
     */
    public void setOptlock(int optlock);

    /**
     * Gets the attributes of this {@link KapuaUpdatableEntity}.<br>
     * This field is formatted, serialized and deserialized as a {@link Properties} Object.<br>
     * This field is a container for additional properties that anyone can manage as its own preference.
     * Kapua will only store and return them without any validation or login upon.
     * 
     * @return The attributes of this {@link KapuaUpdatableEntity}.
     * @throws KapuaIllegalArgumentException
     *             If the stored value cannot be parsed into a {@link Properties} object.
     * @since 1.0.0
     */
    @XmlTransient
    public Properties getEntityAttributes()
            throws KapuaException;

    /**
     * Sets the entity attributes of this {@link KapuaUpdatableEntity}.<br>
     * 
     * @param attributes
     *            The entity attributes to set for this {@link KapuaUpdatableEntity}.
     * @throws KapuaIllegalArgumentException
     *             If the given value cannot be serialized from a {@link Properties} object.
     * @since 1.0.0
     */
    public void setEntityAttributes(Properties attributes)
            throws KapuaException;

    /**
     * Gets the properties of this {@link KapuaUpdatableEntity}.<br>
     * This field is formatted, serialized and deserialized as a {@link Properties} Object.<br>
     * This field is a container for additional properties that the platform may use.
     * These properties are not exposed outside of the platform and are only meant for internal usage.
     * 
     * @return The properties of this {@link KapuaUpdatableEntity}.
     * @throws KapuaIllegalArgumentException
     *             If the stored value cannot be parsed into a {@link Properties} object.
     * @since 1.0.0
     */
    @XmlTransient
    public Properties getEntityProperties()
            throws KapuaException;

    /**
     * Sets the entity properties of this {@link KapuaUpdatableEntity}.<br>
     * 
     * @param properties
     *            The entity properties to set for this {@link KapuaUpdatableEntity}.
     * @throws KapuaIllegalArgumentException
     *             If the given value cannot be serialized from a {@link Properties} object.
     * @since 1.0.0
     */
    public void setEntityProperties(Properties properties)
            throws KapuaException;
}
