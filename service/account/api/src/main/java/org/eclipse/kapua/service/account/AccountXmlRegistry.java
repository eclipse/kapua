/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Account xml factory class
 *
 * @since 1.0
 *
 */
@XmlRegistry
public class AccountXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountFactory factory = locator.getFactory(AccountFactory.class);

    /**
     * Creates a new account instance
     *
     * @return
     */
    public Account newAccount() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new organization instance
     *
     * @return
     */
    public Organization newOrganization() {
        return factory.newOrganization();
    }

    /**
     * Creates a new account creator instance
     *
     * @return
     */
    public AccountCreator newAccountCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new account list result instance
     *
     * @return
     */
    public AccountListResult newAccountListResult() {
        return factory.newListResult();
    }

    public AccountQuery newQuery() {
        return factory.newQuery(null);
    }
}
