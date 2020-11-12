/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Service configuration query reference implementation.
 *
 * @since 1.0
 */
public class ServiceConfigQueryImpl extends AbstractKapuaQuery implements ServiceConfigQuery {

    /**
     * Constructor
     */
    private ServiceConfigQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public ServiceConfigQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
