/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.extras.migrator.encryption.job.JobStepMigratorServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link JobStepMigratorServiceImpl} {@link EntityManagerFactory} implementation.
 *
 * @since 2.0.0
 */
public class MigratorEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-encryption-migrator";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONTRAINTS = new HashMap<>();

    private static final MigratorEntityManagerFactory INSTANCE = new MigratorEntityManagerFactory();

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    private MigratorEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME, DATASOURCE_NAME, UNIQUE_CONTRAINTS);
    }

    /**
     * Returns the {@link EntityManagerFactory} instance.
     *
     * @return The {@link EntityManagerFactory} instance.
     * @since 2.0.0
     */
    public static MigratorEntityManagerFactory getInstance() {
        return INSTANCE;
    }
}
