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
package org.eclipse.kapua.commons.model;

import java.io.Serializable;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua entity base creator service (reference abstract implementation).
 *
 * @param <E>
 *            entity type
 *
 * @since 1.0
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractKapuaEntityCreator<E extends KapuaEntity> implements KapuaEntityCreator<E>, Serializable {

    protected KapuaId scopeId;

    /**
     * Constructor
     *
     * @param scopeId
     */
    protected AbstractKapuaEntityCreator(KapuaId scopeId) {
        super();
        setScopeId(scopeId);
    }

    protected AbstractKapuaEntityCreator(AbstractKapuaEntityCreator<E> abstractEntityCreator) {
        this(abstractEntityCreator.getScopeId());
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {

        this.scopeId = scopeId;
    }
}
