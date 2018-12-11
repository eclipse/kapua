/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common.utils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtilsWithResources;
import org.eclipse.kapua.qa.common.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class EmbeddedDatabase {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDatabase.class);

    /**
     * Path to root of full DB scripts.
     */
    public static final String FULL_SCHEMA_PATH = "database";

    /**
     * Filter for deleting all new DB data except base data.
     */
    public static final String DELETE_SCRIPT = "all_delete.sql";

    public EmbeddedDatabase() {
    }

    private DBHelper dbHelper;

    @Before(value = "@StartDB")
    public void start() throws SQLException {

        logger.info("Starting embedded in memory H2 database.");

        dbHelper = new DBHelper();
        dbHelper.setup();
    }

    @After(value = "@StopDB")
    public void close() throws SQLException {
    }

    public void deleteAll() throws SQLException {

        KapuaConfigurableServiceSchemaUtilsWithResources.scriptSession(FULL_SCHEMA_PATH, DELETE_SCRIPT);
    }

}
