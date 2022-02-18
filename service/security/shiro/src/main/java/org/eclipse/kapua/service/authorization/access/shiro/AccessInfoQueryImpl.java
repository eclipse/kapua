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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;

/**
 * {@link AccessInfoQuery} implementation.
 *
 * @since 1.0.0
 */
public class AccessInfoQueryImpl extends AbstractKapuaQuery implements AccessInfoQuery {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public AccessInfoQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.0.0
     */
    public AccessInfoQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
