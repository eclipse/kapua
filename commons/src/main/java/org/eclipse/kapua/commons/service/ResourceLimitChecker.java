/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service;

import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;

public interface ResourceLimitChecker {

    /**
     *
     * @param scopeId
     *            The {@link KapuaId} of the account to be tested
     * @param targetScopeId
     *            Optional scopeId of the child account to be excluded when validating the new configuration for that scopeId.
     * @param configuration
     *            The configuration to be tested. If null will be read
     *            from the current service configuration; otherwise the passed configuration
     *            will be used in the test
     * @return the number of child accounts spots still available
     * @throws KapuaException
     *            When something goes wrong
     */
    int allowedChildEntities(KapuaId scopeId, KapuaId targetScopeId, Map<String, Object> configuration) throws KapuaException;

    // TODO: doc and rename
    boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId) throws KapuaException;
}
