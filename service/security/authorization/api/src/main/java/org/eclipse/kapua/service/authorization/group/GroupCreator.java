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
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link GroupCreator} definition.
 * <p>
 * It is used to create a new {@link Group}.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "groupCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = GroupXmlRegistry.class, factoryMethod = "newGroupCreator")
public interface GroupCreator extends KapuaNamedEntityCreator<Group> {
}
