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
package org.eclipse.kapua.service.authorization.domain;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.permission.Actions;

/**
 * Domain entity definition.<br>
 * Domain contains the information about the available {@link Actions} for a entity domain<br>
 * Services needs to register their own domain
 * {@link Domain#getName()} must be unique.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "domain")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name",
        "serviceName",
        "actions" })
public interface Domain extends KapuaEntity {

    public static final String TYPE = "domain";

    default public String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Domain} name.<br>
     * It must be unique within the scope.
     * 
     * @param name
     *            The name of the {@link Domain}
     * @since 1.0.0
     */
    public void setName(String name);

    /**
     * Gets the {@link Domain} name.
     * 
     * @return The {@link Domain} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Sets the {@link KapuaService} name that use this {@link Domain}.<br>
     * The value must represent the {@code interface} fully qualified name of the implemented {@link KapuaService}<br>
     * Examples:
     * <ul>
     * <li>org.eclipse.kapua.service.authorization.DomainService</li>
     * <li>org.eclipse.kapua.service.authorization.RoleService</li>
     * </ul>
     * 
     * @param serviceName
     *            The {@link KapuaService} that use this {@link Domain}.<br>
     * @since 1.0.0
     */
    public void setServiceName(String serviceName);

    /**
     * Gets the {@link KapuaService} name that use this {@link Domain}.<br>
     * The value represent the {@code interface} fully qualified name of the implemented {@link KapuaService}<br>
     * 
     * @return The {@link KapuaService} that use this {@link Domain}.<br>
     * @since 1.0.0
     */
    @XmlElement(name = "serviceName")
    public String getServiceName();

    /**
     * Sets the set of {@link Actions} available in this {@link Domain}.<br>
     * It up to the implementation class to make a clone of the set or use the given set.
     * 
     * @param actions
     *            The set of {@link Actions}.
     * @throws KapuaException
     *             If the given set is incompatible with the implementation-specific field.
     * @since 1.0.0
     */
    public void setActions(Set<Actions> actions);

    /**
     * Gets the set of {@link Actions} available in this {@link Domain}.<br>
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link Actions}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    public Set<Actions> getActions();

}
