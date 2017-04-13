/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    /**
     * Constructor
     * 
     * @param scopeId
     */
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
