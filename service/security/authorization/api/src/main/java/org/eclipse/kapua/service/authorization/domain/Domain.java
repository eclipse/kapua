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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.permission.Permission;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Set;

/**
 * {@link Domain} entity definition.<br>
 * {@link Domain} contains the information about the available {@link Actions} for a entity {@link Domain}<br>
 * Services needs to register their own specific {@link Domain}.
 * {@link Domain#getName()} must be unique.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "domain")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name",
        "serviceName",
        "actions",
        "groupable" })
public interface Domain extends KapuaEntity {//, org.eclipse.kapua.model.domain.Domain {

    public static final String TYPE = "domain";

    @Override
    public default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Domain} name.<br>
     * It must be unique within the scope.
     *
     * @param name The name of the {@link Domain}
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
     * <li>org.eclipse.kapua.service.authorization.DomainRegistryService</li>
     * <li>org.eclipse.kapua.service.authorization.RoleService</li>
     * </ul>
     *
     * @param serviceName The {@link KapuaService} that use this {@link Domain}.<br>
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
     * @param actions The set of {@link Actions}.
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

    /**
     * Sets whether or not this {@link Domain} is group-able or not.
     * This determines if the {@link org.eclipse.kapua.service.authorization.permission.Permission} in this {@link Domain} can have a {@link org.eclipse.kapua.service.authorization.group.Group} or not.
     * This is related to the {@link org.eclipse.kapua.service.authorization.group.Groupable} property of a {@link KapuaEntity}.
     *
     * @param groupable {@code true} if the {@link org.eclipse.kapua.service.authorization.permission.Permission} on this {@link Domain} can have the {@link Permission#getGroupId()} property set, {@code false} otherwise.
     * @since 0.3.1
     */
    public void setGroupable(boolean groupable);

    /**
     * Gets whether or not this {@link Domain} is group-able or not.
     *
     * @return {@code true} if the {@link Permission#getGroupId()} can be set, {@code false} otherwise.
     * @since 0.3.1
     */
    @XmlElement(name = "groupable")
    public boolean getGroupable();

}
