/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link StorableCreator} definition.
 * <p>
 * It is the base {@code interface} for all {@link Object} creators that are {@link Storable}.
 *
 * @param <E> The {@link Storable} for which this is a {@link StorableCreator} for.
 * @since 1.0.0
 */
public interface StorableCreator<E extends Storable> {

    /**
     * Gets the scope {@link KapuaId}.
     *
     * @return The scope {@link KapuaId}.
     * @since 1.0.0
     */
    KapuaId getScopeId();

    /**
     * Sets the scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.3.0
     */
    void setScopeId(KapuaId scopeId);
}
