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
package org.eclipse.kapua.service.config;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

import java.util.Map;

/**
 * Configurable {@link KapuaService} definition.
 * <p>
 * Identifies a {@link KapuaService} with per-scope configurations
 *
 * @since 1.0.0
 */
public interface KapuaConfigurableService {

    /**
     * Gets the {@link KapuaTocd} for the given scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @return The {@link KapuaTocd} for the given scope {@link KapuaId}.
     * @throws KapuaException
     * @since 1.0.0
     */
    KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException;

    /**
     * Gets the {@link Map} properties values for the given scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @return The {@link Map} properties values for the given scope {@link KapuaId}.
     * @throws KapuaException
     * @since 1.0.0
     */
    Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException;

    /**
     * Updates the {@link Map} properties values for the given scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @param values  The {@link Map} properties values to update
     * @throws KapuaException
     * @since 1.0.0
     */
    void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException;

}
