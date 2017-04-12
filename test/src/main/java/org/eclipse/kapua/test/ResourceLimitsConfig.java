/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.test;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Usage of this class instances is mainly in test cases that have to configure
 * services with specific values. Otherwise this is set in kapua user specific
 * configuration.
 */
public class ResourceLimitsConfig {

    /**
     * Scope id for which service limits are set.
     */
    private final KapuaEid scopeId;

    /**
     * Parent scope id for which service limits are set.
     */
    private final KapuaEid parentScopeId;

    /**
     * Map of limits set for service.
     */
    private Map<String, Object> valueMap = new HashMap<>();


    /**
     * Only allowed constructor with id of user for which limits are set.
     *
     * @param scopeId       users scope Id
     * @param parentScopeId parent id of specified scopeId
     */
    public ResourceLimitsConfig(BigInteger scopeId, BigInteger parentScopeId) {

        this.scopeId = new KapuaEid(scopeId);
        this.parentScopeId = new KapuaEid(parentScopeId);
    }

    /**
     * Add new resource limit value.
     *
     * @param key   resource limit key
     * @param value resource limit value, can be String, Boolean, Integer
     */
    public void addConfig(String key, Object value) {

        valueMap.put(key, value);
    }

    /**
     * Apply configuration of resource limits to specific service.
     *
     * @param service that supports resource limits
     * @throws KapuaException
     */
    public void setServiceConfig(KapuaConfigurableService service) throws KapuaException {

        service.setConfigValues(scopeId, parentScopeId, valueMap);
    }
}
