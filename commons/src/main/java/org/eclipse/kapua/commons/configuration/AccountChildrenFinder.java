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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.account.AccountListResult;


/**
 * Service to retrieve all child accounts for a given scope
 *
 * @since 2.0.0
 */
public interface AccountChildrenFinder extends KapuaService {

    /**
     * @param scopeId       The scope id - must be provided
     * @param targetScopeId - nullable target scope id
     * @return the list of child accounts
     * @throws KapuaException
     */
    AccountListResult findChildren(KapuaId scopeId, KapuaId targetScopeId) throws KapuaException;
}
