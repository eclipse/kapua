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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;

/**
 * {@link DomainQuery} implementation.
 *
 * @since 1.0.0
 */
public class DomainQueryImpl extends AbstractKapuaNamedQuery implements DomainQuery {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public DomainQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.5.0
     */
    public DomainQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
