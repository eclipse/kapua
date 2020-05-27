/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;

/**
 * {@link AccessRole} query implementation.
 *
 * @since 1.0
 */
public class AccessRoleQueryImpl extends AbstractKapuaQuery implements AccessRoleQuery {

    /**
     * Constructor
     */
    public AccessRoleQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public AccessRoleQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
