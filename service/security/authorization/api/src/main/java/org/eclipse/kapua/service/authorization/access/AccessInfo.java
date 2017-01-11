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
package org.eclipse.kapua.service.authorization.access;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.service.authorization.subject.Subject;

/**
 * Access info entity definition.<br>
 * It contains all authorization accesses for a {@link Subject}.<br>
 * It refers to the {@link Subject} entity by the {@link AccessInfo#getSubject()} property.<br>
 * <br>
 * {@link AccessInfo} is unique by the {@link AccessInfo#getSubject()} property.
 * 
 * @since 1.0.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "subject" }, //
        factoryClass = AccessInfoXmlRegistry.class, factoryMethod = "newAccessInfo")
public interface AccessInfo extends KapuaUpdatableEntity {

    public static final String TYPE = "accessInfo";

    public default String getType() {
        return TYPE;
    }

    /**
     * Returns the {@link Subject} of this {@link AccessInfo}.
     * 
     * @return The {@link Subject} of this {@link AccessInfo}.
     * @since 1.0.0
     */
    @XmlElement(name = "subject")
    public Subject getSubject();

    /**
     * Sets the {@link Subject} of this {@link AccessInfo}.
     * 
     * @param subject
     *            The {@link Subject} of this {@link AccessInfo}.
     * @since 1.0.0
     */
    public void setSubject(Subject subject);
}
