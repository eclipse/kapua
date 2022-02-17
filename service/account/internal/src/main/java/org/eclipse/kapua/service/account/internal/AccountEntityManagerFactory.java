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
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link AccountServiceImpl} {@link EntityManagerFactory} implementation.
 *
 * @since 1.0.0
 */
public class AccountEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-account";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONTRAINTS = new HashMap<>();

    private static final AccountEntityManagerFactory INSTANCE = new AccountEntityManagerFactory();

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private AccountEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME,
                DATASOURCE_NAME,
                UNIQUE_CONTRAINTS);
    }

    /**
     * Returns the {@link EntityManager} instance
     *
     * @return The {@link EntityManager} instance.
     * @since 1.0.0
     */
    public static AccountEntityManagerFactory getInstance() {
        return INSTANCE;
    }
}
