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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.Map;
import java.util.Optional;

public interface ServiceConfigurationManager {
    void checkAllowedEntities(KapuaId scopeId, String entityType) throws KapuaException;

    void setConfigValues(KapuaId scopeId, Optional<KapuaId> parentId, Map<String, Object> values) throws KapuaException;

    Map<String, Object> getConfigValues(KapuaId scopeId, boolean excludeDisabled) throws KapuaException;

    KapuaTocd getConfigMetadata(KapuaId scopeId, boolean excludeDisabled) throws KapuaException;
}
