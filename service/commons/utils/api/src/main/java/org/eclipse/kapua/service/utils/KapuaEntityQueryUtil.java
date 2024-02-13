/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.utils;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaForwardableEntityQuery;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaService;

/**
 * Service to help process/transform queries
 *
 * @since 2.0.0
 */
public interface KapuaEntityQueryUtil extends KapuaService {

    /**
     * Transform the specified query for the {@link KapuaForwardableEntityQuery} getIncludeInherited() option (i.e.
     * to also query the parent accounts).
     * 
     * @param query
     * @return
     * @throws KapuaException
     */
    public KapuaQuery transformInheritedQuery(@NotNull KapuaForwardableEntityQuery query) throws KapuaException;
}
