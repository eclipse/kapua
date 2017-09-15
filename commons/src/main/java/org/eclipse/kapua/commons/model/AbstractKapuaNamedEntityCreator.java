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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua entity named creator service (reference abstract implementation).
 *
 * @param <E>
 *            entity type
 * 
 * @since 1.0
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractKapuaNamedEntityCreator<E extends KapuaEntity> extends AbstractKapuaUpdatableEntityCreator<E> implements KapuaNamedEntityCreator<E> {

    protected String name;

    /**
     * Constructor
     * 
     * @param scopeId
     * @param name
     */
    protected AbstractKapuaNamedEntityCreator(KapuaId scopeId,
            String name) {
        super(scopeId);
        this.name = name;
    }

    public AbstractKapuaNamedEntityCreator(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
