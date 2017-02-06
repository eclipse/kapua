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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for JPA operations.
 * 
 * @since 1.0.0
 */
public abstract class AbstractEntityManagerFactory implements org.eclipse.kapua.commons.jpa.EntityManagerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEntityManagerFactory.class);

    private static final Map<String, String> s_uniqueConstraints = new HashMap<>();
    private EntityManagerFactory entityManagerFactory;

    /**
     * Jdbc url connection resolver service
     */
    private final JdbcConnectionUrlResolver jdbcConnectionUrlResolver;

    /**
     * Protected constructor
     * 
     * @param persistenceUnitName
     * @param datasourceName
     * @param uniqueConstraints
     */
    protected AbstractEntityManagerFactory(String persistenceUnitName, String datasourceName, Map<String, String> uniqueConstraints) {
        SystemSetting config = SystemSetting.getInstance();

        String connectionUrlResolverType = config.getString(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER, "DEFAULT");
        LOG.debug("The following JDBC connection URL resolver type will be used: {}", connectionUrlResolverType);
        if (connectionUrlResolverType.equals("DEFAULT")) {
            jdbcConnectionUrlResolver = new DefaultConfigurableJdbcConnectionUrlResolver();
        } else if (connectionUrlResolverType.equals("H2")) {
            jdbcConnectionUrlResolver = new H2JdbcConnectionUrlResolver();
        } else {
            throw new IllegalArgumentException("Unknown JDBC connection URL resolver type: " + connectionUrlResolverType);
        }

        //
        // Initialize the EntityManagerFactory
        try {
            // JPA configuration overrides
            Map<String, Object> configOverrides = new HashMap<>();
            configOverrides.put("javax.persistence.jdbc.driver", config.getString(SystemSettingKey.DB_JDBC_DRIVER));

            configOverrides.put("eclipselink.cache.shared.default", "false"); // This has to be set to false in order to disable the local object cache of EclipseLink.

            configOverrides.put("eclipselink.connection-pool.default.url", jdbcConnectionUrlResolver.connectionUrl());
            configOverrides.put("eclipselink.connection-pool.default.user", config.getString(SystemSettingKey.DB_USERNAME));
            configOverrides.put("eclipselink.connection-pool.default.password", config.getString(SystemSettingKey.DB_PASSWORD));

            configOverrides.put("eclipselink.connection-pool.default.dataSourceName", datasourceName);
            configOverrides.put("eclipselink.connection-pool.default.initial", config.getString(SystemSettingKey.DB_POOL_SIZE_INITIAL));
            configOverrides.put("eclipselink.connection-pool.default.min", config.getString(SystemSettingKey.DB_POOL_SIZE_MIN));
            configOverrides.put("eclipselink.connection-pool.default.max", config.getString(SystemSettingKey.DB_POOL_SIZE_MAX));
            configOverrides.put("eclipselink.connection-pool.default.wait", config.getString(SystemSettingKey.DB_POOL_BORROW_TIMEOUT));

            // Standalone JPA
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, configOverrides);
        } catch (Throwable ex) {
            LOG.error("Error creating EntityManagerFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }

        //
        // Set unique constrains for this persistence unit
        // FIXME: this is needed? With EclipseLink we lost the ConstraintViolationException.
        for (Entry<String, String> uc : uniqueConstraints.entrySet()) {
            s_uniqueConstraints.put(uc.getKey(), uc.getValue());
        }
    }

    // Entity manager factory methods

    /**
     * Returns an EntityManager instance.
     * 
     * @return An entity manager for the persistence unit.
     * @throws KapuaException
     *             If {@link EntityManagerFactory#createEntityManager()} cannot create the {@link EntityManager}
     * 
     * @since 1.0.0
     */
    public EntityManager createEntityManager()
            throws KapuaException {
        return new EntityManager(entityManagerFactory.createEntityManager());
    }

}
