/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.model.KapuaEntityAttributes;

/**
 * Query predicate attribute name for {@link AccessPermission} entity.
 * 
 * @since 1.0.0
 * 
 */
public class AccessRoleAttributes extends KapuaEntityAttributes {

    /**
     * {@link AccessRole#setAccessInfoId(org.eclipse.kapua.model.id.KapuaId)} access info id
     */
    public static final String ACCESS_INFO_ID = "accessInfoId";

    /**
     * {@link AccessRole#setRoleId(org.eclipse.kapua.model.id.KapuaId)} role id
     */
    public static final String ROLE_ID = "roleId";
}
