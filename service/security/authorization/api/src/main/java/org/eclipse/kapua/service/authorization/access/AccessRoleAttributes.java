/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
