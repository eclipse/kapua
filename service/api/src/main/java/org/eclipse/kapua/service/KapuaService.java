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

import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaService} definition.
 *
 * @since 1.0.0
 */
public interface KapuaService {

    /**
     * Whether this {@link KapuaService} is enabled for the given scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId} for which to check.
     * @return {@code true} if the {@link KapuaService} is enabled, {@code false} otherwise.
     * @since 2.0.0
     */
    default boolean isServiceEnabled(KapuaId scopeId) {
        return true;
    }
}
