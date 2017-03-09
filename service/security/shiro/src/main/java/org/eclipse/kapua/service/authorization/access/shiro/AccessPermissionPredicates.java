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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.model.KapuaEntityPredicates;
import org.eclipse.kapua.service.authorization.access.AccessPermission;

/**
 * Query predicate attribute name for {@link AccessPermission} entity.
 * 
 * @since 1.0.0
 * 
 */
public interface AccessPermissionPredicates extends KapuaEntityPredicates {

    /**
     * {@link AccessPermission#setAccessInfoId(org.eclipse.kapua.model.id.KapuaId)} id
     */
    public static final String ACCESS_INFO_ID = "accessInfoId";
}
