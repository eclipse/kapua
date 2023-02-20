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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import java.util.Map;
import java.util.Optional;

/**
 * Base {@link KapuaConfigurableService} implementation, build upon {@link ServiceConfigurationManager}.
 * <p>
 * Note: at first glance, this might seems like a violation of Composition over Inheritance principle, however:
 * - in this case inheritance is an acceptable strategy due to the strong link between {@link ServiceConfigurationManager#isServiceEnabled(KapuaId)}
 * and {@link org.eclipse.kapua.service.KapuaService#isServiceEnabled(KapuaId)} (the latter being dependent from the first for configurable services).
 * - this class is nothing more than glue and convenience, demanding all of its logic to the {@link ServiceConfigurationManager}'s instance provided, so no flexibility has been sacrificed
 *
 * @since 2.0.0
 */
public class KapuaConfigurableServiceLinker
        implements KapuaConfigurableService,
        KapuaService {

    protected final ServiceConfigurationManager serviceConfigurationManager;

    public KapuaConfigurableServiceLinker(ServiceConfigurationManager serviceConfigurationManager) {
        this.serviceConfigurationManager = serviceConfigurationManager;
    }

    @Override
    public boolean isServiceEnabled(KapuaId scopeId) {
        return serviceConfigurationManager.isServiceEnabled(scopeId);
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        return serviceConfigurationManager.getConfigMetadata(scopeId, true);
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return serviceConfigurationManager.getConfigValues(scopeId, true);
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        serviceConfigurationManager.setConfigValues(scopeId, Optional.ofNullable(parentId), values);
    }
}
