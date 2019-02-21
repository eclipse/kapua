/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.Properties;

/**
 * {@link KapuaUpdatableEntityCreator} {@code abstract} implementation
 *
 * @param <E> the {@link KapuaEntity} for which this {@link AbstractKapuaEntityCreator} is for
 * @since 1.0.0
 */
public abstract class AbstractKapuaUpdatableEntityCreator<E extends KapuaEntity> extends AbstractKapuaEntityCreator<E> implements KapuaUpdatableEntityCreator<E> {

    protected Properties entityAttributes;

    /**
     * Constructor
     *
     * @param scopeId the scope {@link KapuaId}
     * @since 1.0.0
     */
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
