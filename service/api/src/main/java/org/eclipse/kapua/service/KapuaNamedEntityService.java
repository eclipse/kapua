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
package org.eclipse.kapua.service;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;

import javax.validation.constraints.NotNull;

/**
 * Base {@code interface} for all {@link KapuaService}s that are managing {@link KapuaNamedEntity}es.
 *
 * @param <E> Type of the {@link KapuaNamedEntity} being managed.
 * @since 1.0.0
 */
public interface KapuaNamedEntityService<E extends KapuaNamedEntity> extends KapuaService {

    /**
     * Finds a {@link KapuaNamedEntity} by its {@link KapuaNamedEntity#getName()}
     *
     * @param name The {@link KapuaNamedEntity#getName()}.
     * @return The found {@link KapuaEntity}, or {@code null}.
     * @throws KapuaException
     * @since 1.0.0
     */
    E findByName(@NotNull String name) throws KapuaException;
}
