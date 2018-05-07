/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Account factory service definition.
 *
 * @since 1.0
 */
public interface AccountFactory extends KapuaEntityFactory<Account, AccountCreator, AccountQuery, AccountListResult> {

    /**
     * Creates a new {@link AccountCreator} for the specified name
     *
     * @param scopeId
     * @param name
     * @return
     */
    AccountCreator newCreator(KapuaId scopeId, String name);

    /**
     * Creates a new organization entity
     *
     * @return
     */
    Organization newOrganization();

}
