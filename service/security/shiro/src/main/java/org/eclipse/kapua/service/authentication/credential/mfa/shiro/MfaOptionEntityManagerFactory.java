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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity manager factory for the {@link MfaOptionServiceImpl} module.
 */
public class MfaOptionEntityManagerFactory extends AbstractEntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-authentication";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONSTRAINTS = new HashMap<>();

    private static MfaOptionEntityManagerFactory instance = new MfaOptionEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the user persistence unit.
     */
    private MfaOptionEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME, DATASOURCE_NAME, UNIQUE_CONSTRAINTS);
    }

    /**
     * Return the {@link EntityManager} singleton instance
     *
     * @return
     */
    public static EntityManagerFactory getInstance() {
        return instance;
    }
}
