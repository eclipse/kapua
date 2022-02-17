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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaNamedEntityCreator} {@code abstract} implementation.
 *
 * @param <E> {@link KapuaEntity} which this {@link AbstractKapuaUpdatableEntityCreator} is for
 * @since 1.0.0
 */
public abstract class AbstractKapuaNamedEntityCreator<E extends KapuaEntity> extends AbstractKapuaUpdatableEntityCreator<E> implements KapuaNamedEntityCreator<E> {

    protected String name;
    protected String description;

    /**
     * Constructor
     *
     * @param scopeId the scope {@link KapuaId}
     * @param name    the name
     * @since 1.0.0
     */
    protected AbstractKapuaNamedEntityCreator(KapuaId scopeId, String name) {
        super(scopeId);

        setName(name);
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
