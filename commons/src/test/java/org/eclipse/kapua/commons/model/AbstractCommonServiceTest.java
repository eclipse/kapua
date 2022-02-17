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
package org.eclipse.kapua.commons.model;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.jpa.CommonsEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommonServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommonServiceTest.class);

    public static final String DEFAULT_TEST_PATH = "./src/test/sql/H2/";
    public static final String DEFAULT_COMMONS_PATH = "../commons";
    public static final String DROP_TEST_FILTER = "test_*_drop.sql";

    public static void scriptSession(String path, String fileFilter) {
        EntityManager em = null;
        try {

            logger.info("Running database scripts...");

            em = CommonsEntityManagerFactory.getInstance().createEntityManager();
            em.beginTransaction();

            SimpleSqlScriptExecutor sqlScriptExecutor = new SimpleSqlScriptExecutor();
            sqlScriptExecutor.scanScripts(path, fileFilter);
            sqlScriptExecutor.executeUpdate(em);

            em.commit();

            logger.info("...database scripts done!");
        } catch (KapuaException e) {
            logger.error("Database scripts failed: {}", e.getMessage());
            if (em != null) {
                em.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    @BeforeClass
    public static void tearUp() throws KapuaException {

        SystemSetting config = SystemSetting.getInstance();
        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
        String jdbcUrl = JdbcConnectionUrlResolvers.resolveJdbcUrl();

        new KapuaLiquibaseClient(jdbcUrl, "kapua", "kapua", schema).update();
    }

    @AfterClass
    public static void tearDown() {
        scriptSession(DEFAULT_TEST_PATH, DROP_TEST_FILTER);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
    }
}
