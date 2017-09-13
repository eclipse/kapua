/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua updatable entity creator service (reference abstract implementation).
 *
 * @param <E>
 *            entity type
 * 
 * @since 1.0
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractKapuaUpdatableEntityCreator<E extends KapuaEntity> extends AbstractKapuaEntityCreator<E> implements KapuaUpdatableEntityCreator<E> {

    protected String name;
    protected Properties entityAttributes;

    /**
     * Constructor
     * 
     * @param scopeId
     * @param name
     */
    protected AbstractKapuaUpdatableEntityCreator(KapuaId scopeId,
            String name) {
        super(scopeId);
        this.name = name;
        entityAttributes = new Properties();
    }

    public AbstractKapuaUpdatableEntityCreator(KapuaId scopeId) {
        super(scopeId);
        entityAttributes = new Properties();
    }

    @Override
    public Properties getEntityAttributes() throws KapuaException {
        return entityAttributes;
    }

    @Override
    public void setEntityAttributes(Properties entityAttributes) throws KapuaException {
        this.entityAttributes = entityAttributes;
    }
}
