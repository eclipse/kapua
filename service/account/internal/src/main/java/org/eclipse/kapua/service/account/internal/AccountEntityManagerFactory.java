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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import javax.inject.Singleton;

/**
 * {@link AccountServiceImpl} {@link EntityManagerFactory} implementation.
 *
 * @since 1.0.0
 * @deprecated since 2.0.0, use {@link org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory} instead
 */
@Singleton
@Deprecated
public class AccountEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-account";

    private static final AccountEntityManagerFactory INSTANCE = new AccountEntityManagerFactory();

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public AccountEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME);
    }

    /**
     * Returns the {@link EntityManagerFactory} instance.
     *
     * @return The {@link EntityManagerFactory} instance.
     * @since 1.0.0
     * @deprecated Since 2.0.0 - Please use {@link AccountEntityManagerFactory#AccountEntityManagerFactory()} instead. This may be removed in future releases
     */
    @Deprecated
    public static AccountEntityManagerFactory getInstance() {
        return INSTANCE;
    }
}
