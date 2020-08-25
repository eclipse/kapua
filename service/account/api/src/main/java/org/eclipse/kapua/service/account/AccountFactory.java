/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
 * {@link AccountFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface AccountFactory extends KapuaEntityFactory<Account, AccountCreator, AccountQuery, AccountListResult> {

    /**
     * Instantiates a new {@link AccountCreator} with the given name.
     *
     * @param scopeId The scope {@link KapuaId} to set in the {@link AccountCreator}
     * @param name    The name to set in the {@link AccountCreator}
     * @return The newly instantiated {@link AccountCreator}
     * @since 1.0.0
     */
    AccountCreator newCreator(KapuaId scopeId, String name);

    /**
     * Instantiates a new {@link Organization}.
     *
     * @return The newly instantiated {@link Organization}.
     * @since 1.0.0
     */
    Organization newOrganization();
}
