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
package org.eclipse.kapua.service.authorization.group;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.authorization.role.RoleXmlRegistry;

/**
 * {@link Group} list result definition.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "groups")
@XmlType(factoryClass = GroupXmlRegistry.class, factoryMethod = "newGroupListResult")
public interface GroupListResult extends KapuaListResult<Group> {

}
