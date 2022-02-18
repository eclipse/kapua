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

import java.util.Map;

/**
 * Configurable service definition
 *
 * @since 1.0
 */
public interface KapuaConfigurableService {

    /**
     * Return the service configuration metadata
     *
     * @param scopeId
     * @return
     * @throws KapuaException
     */
    KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException;

    /**
     * Return a map of configuration values associated with the provided scope id
     *
     * @param scopeId
     * @return
     * @throws KapuaException
     */
    Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException;

    /**
     * Set the configuration values for the specified scope id
     *
     * @param scopeId
     * @param values
     * @throws KapuaException
     */
    void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException;

}
