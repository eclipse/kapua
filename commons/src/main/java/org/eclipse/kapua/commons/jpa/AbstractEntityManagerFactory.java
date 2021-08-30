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
package org.eclipse.kapua.commons.jpa;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Base {@code abstract} {@link EntityManager}.
 *
 * @since 1.0.0
 */
public abstract class AbstractEntityManagerFactory implements org.eclipse.kapua.commons.jpa.EntityManagerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEntityManagerFactory.class);

    private static final SystemSetting SYSTEM_SETTING = SystemSetting.getInstance();

    private static final String DEFAULT_DATASOURCE_NAME = "kapua-dbpool";

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Constructor.
     *
     * @param persistenceUnitName The {@link PersistenceUnit} name.
     * @param datasourceName      The {@link DataSource} name.
     * @param uniqueConstraints   The unique constraints for the given {@link PersistenceUnit}.
     * @since 1.0.0
     * @deprecated since 2.0.0. Unique constraint are not used.
     */
    @Deprecated
    protected AbstractEntityManagerFactory(String persistenceUnitName, String datasourceName, Map<String, String> uniqueConstraints) {
        this(persistenceUnitName, datasourceName);
    }

    /**
     * Constructor.
     *
     * @param persistenceUnitName The {@link PersistenceUnit} name.
     * @since 2.0.0
     */
    protected AbstractEntityManagerFactory(String persistenceUnitName) {
        this(persistenceUnitName, DEFAULT_DATASOURCE_NAME);
    }

    /**
     * Constructor.
     *
     * @param persistenceUnitName The {@link PersistenceUnit} name.
     * @param datasourceName      The {@link DataSource} name.
     * @since 2.0.0
     */
    protected AbstractEntityManagerFactory(String persistenceUnitName, String datasourceName) {
        //
        // Initialize the EntityManagerFactory
        try {

            // JPA configuration overrides
            Map<String, Object> configOverrides = new HashMap<>();

            configOverrides.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, "false"); // This has to be set to false in order to disable the local object cache of EclipseLink.
            configOverrides.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, DataSource.getDataSource());

            String targetDatabase = SYSTEM_SETTING.getString(SystemSettingKey.DB_JDBC_DATABASE_TARGET);
            if (!Strings.isNullOrEmpty(targetDatabase)) {
                configOverrides.put(PersistenceUnitProperties.TARGET_DATABASE, targetDatabase);
            }

            // Standalone JPA
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, configOverrides);

            printEntityManagerConfiguration(persistenceUnitName, datasourceName, configOverrides);
        } catch (Throwable ex) {
            LOG.error("Error creating EntityManagerFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns an EntityManager instance.
     *
     * @return An entity manager for the persistence unit.
     * @throws KapuaException If {@link EntityManagerFactory#createEntityManager()} cannot create the {@link EntityManager}
     * @since 1.0.0
     */
    @Override
    public EntityManager createEntityManager() throws KapuaException {
        return new EntityManager(entityManagerFactory.createEntityManager());
    }

    //
    // Private Methods
    //

    /**
     * Prints the {@link EntityManager}'s configuration.
     *
     * @param persistenceUnitName The {@link PersistenceUnit#name()}.
     * @param datasourceName      The datasource name.
     * @param configOverrides     The configuration overrides for the {@link EntityManager}.
     * @since 2.0.0
     */
    private void printEntityManagerConfiguration(String persistenceUnitName, String datasourceName, Map<String, Object> configOverrides) {
        ConfigurationPrinter configurationPrinter =
                ConfigurationPrinter.create()
                        .withLogger(LOG)
                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                        .withTitle("Persistence Unit Config: " + persistenceUnitName)
                        .addParameter("Datasource Name", datasourceName)
                        .openSection("Configuration Overrides");

        for (Map.Entry<String, Object> config : configOverrides.entrySet()) {
            configurationPrinter.addParameter(config.getKey(), config.getValue());
        }

        configurationPrinter.printLog();
    }
}
