/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.jpa.AbstractNamedEntityCacheFactory;
import org.eclipse.kapua.commons.jpa.CacheFactory;

/**
 * {@link AccountServiceImpl} {@link CacheFactory} implementation.
 *
 * @since 1.2.0
 */
public class AccountCacheFactory extends AbstractNamedEntityCacheFactory implements CacheFactory {

    /**
     * Constructor.
     *
     * @since 1.2.0
     */
    private AccountCacheFactory() {
        super("AccountId", "AccountName");
    }

    /**
     * Gets the {@link AccountCacheFactory} instance.
     *
     * @return The {@link AccountCacheFactory} instance.
     * @since 1.2.0
     */
    protected static AccountCacheFactory getInstance() {
        return new AccountCacheFactory();
    }
}
