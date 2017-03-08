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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.permission.Action;
import org.eclipse.kapua.service.authorization.permission.Actions;

/**
 * {@link Domain} creator definition.<br>
 * It is used to create a new {@link Domain} with {@link Action}s associated
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "domainCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
public interface DomainCreator extends KapuaEntityCreator<Domain> {

    /**
     * Sets the {@link Domain} name.
     * 
     * @param name
     *            The {@link Domain} name.
     * 
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
     * Sets the {@link KapuaService} name that uses the {@link Domain}.
     * 
     * @param serviceName
     *            The {@link KapuaService} name that uses the {@link Domain}.
     * 
     * @since 1.0.0
     */
    public void setServiceName(String serviceName);

    /**
     * Gets the {@link KapuaService} name that uses the {@link Domain}.
     * 
     * @return The {@link KapuaService} name that uses the {@link Domain}.
     * @since 1.0.0
     */
    @XmlElement(name = "serviceName")
    public String getServiceName();

    /**
     * Sets the set of {@link Actions} available in the {@link Domain}.<br>
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
     * Gets the set of {@link Actions} available in the {@link Domain}.<br>
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link Actions}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    public Set<Actions> getActions();

}
