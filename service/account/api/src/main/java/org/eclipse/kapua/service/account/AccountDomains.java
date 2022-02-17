/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.domain.Domain;

/**
 * The {@code kapua-account-api} module {@link Domain} list.
 *
 * @since 1.0.0
 */
public class AccountDomains {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private AccountDomains() {
    }

    /**
     * The {@link Account} {@link Domain}
     *
     * @since 1.0.0
     */
    public static final AccountDomain ACCOUNT_DOMAIN = new AccountDomain();
}
