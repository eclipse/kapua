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
package org.eclipse.kapua.storage;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;


public interface KapuaNamedEntityTransactedRepository<E extends KapuaNamedEntity, L extends KapuaListResult<E>> extends KapuaUpdatableEntityTransactedRepository<E, L> {
    /**
     * Finds a {@link KapuaNamedEntity} by {@link KapuaNamedEntity#getName()}.
     *
     * @param value The value of the {@link KapuaNamedEntity#getName()} to search.
     * @return The {@link KapuaNamedEntity} found, or {@code null} if not found.
     * @since 2.0.0
     */
    E findByName(String value) throws KapuaException;

    /**
     * Finds a {@link KapuaNamedEntity} by {@link KapuaNamedEntity#getName()}.
     *
     * @param scopeId The {@link KapuaNamedEntity#getScopeId()} in which to look for results.
     * @param value   The value of the field from which to search.
     * @return The {@link KapuaNamedEntity} found, or {@code null} if not found.
     * @since 2.0.0
     */
    E findByName(KapuaId scopeId, String value) throws KapuaException;
}
