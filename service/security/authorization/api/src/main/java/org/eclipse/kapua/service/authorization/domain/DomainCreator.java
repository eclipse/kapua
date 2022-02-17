/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * {@link DomainCreator} definition.<br>
 * <p>
 * It is used to create a new {@link Domain} with {@link Actions} associated
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "domainCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
public interface DomainCreator extends KapuaEntityCreator<Domain> { // org.eclipse.kapua.model.domain.Domain {

    String getName();

    Set<Actions> getActions();

    boolean getGroupable();

    /**
     * Sets the {@link Domain} name.
     *
     * @param name The {@link Domain} name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Sets the set of {@link Actions} available in the {@link Domain}.<br>
     * It up to the implementation class to make a clone of the set or use the given set.
     *
     * @param actions The set of {@link Actions}.
     * @since 1.0.0
     */
    void setActions(Set<Actions> actions);

    /**
     * Sets whether or not this {@link Domain} is group-able or not.
     * This determines if the {@link org.eclipse.kapua.service.authorization.permission.Permission} in this {@link Domain} can have a {@link org.eclipse.kapua.service.authorization.group.Group} or not.
     * This is related to the {@link org.eclipse.kapua.service.authorization.group.Groupable} property of a {@link KapuaEntityCreator}.
     *
     * @param groupable {@code true} if the {@link org.eclipse.kapua.service.authorization.permission.Permission} on this {@link Domain} can have the {@link Permission#getGroupId()} property set, {@code false} otherwise.
     * @since 0.3.1
     */
    void setGroupable(boolean groupable);

}
