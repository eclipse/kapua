/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaForwardableEntityQuery;
import org.eclipse.kapua.model.query.KapuaQuery;

public class AbstractKapuaForwardableEntityQuery extends AbstractKapuaNamedQuery implements KapuaForwardableEntityQuery {

    protected Boolean includeInherited = Boolean.FALSE;

    /**
     * Constructor.
     *
     */
    public AbstractKapuaForwardableEntityQuery() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     */
    public AbstractKapuaForwardableEntityQuery(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param query The {@link AbstractKapuaForwardableEntityQuery} to clone.
     */
    public AbstractKapuaForwardableEntityQuery(KapuaQuery query) {
        super(query);
        if(query instanceof KapuaForwardableEntityQuery) {
            this.includeInherited = ((KapuaForwardableEntityQuery) query).getIncludeInherited();
        }
    }

    @Override
    public Boolean getIncludeInherited() {
        return includeInherited;
    }

    @Override
    public void setIncludeInherited(Boolean includeInherited) {
        this.includeInherited = includeInherited;
    }
}
