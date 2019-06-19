/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sql script executor bean. Used to invoke the execution of sql scripts to update database schema.
 *
 * @since 1.0
 */
public class SimpleSqlScriptExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSqlScriptExecutor.class);

    private static final String WILDCAR_ANY = "*";

    private static final String RUN_SCRIPT_CMD = "RUNSCRIPT FROM '%s'";
    private static final String RUN_RESOURCE_CMD = "RUNSCRIPT FROM 'classpath:%s'";

    /**
     * Default sql scripts path
     */
    private static final String DEFAULT_SCRIPTS_PATH = "src/main/sql/H2";

    // Members

    private final List<String> queryStrings = new ArrayList<>();

    // Operations

    /**
     * Clear the query string list
     */
    public void clearQueries() {
        queryStrings.clear();
    }

    /**
     * Return the query list string
     *
     * @return
     */
    public List<String> getQueries() {
        return Collections.unmodifiableList(queryStrings);
    }

    /**
     * Creates and configure a {@link SimpleSqlScriptExecutor} adding all the sql scripts matching the filter in the specified path
     *
     * @param scanPath
     *            path to be scanned
     * @param filenameFilter
     *            name filter matching <b>(must be not null!)</b>
     * @return
     */
    public SimpleSqlScriptExecutor scanScripts(String scanPath, String filenameFilter) {

        String prefix = "";
        String suffix = "";
        if (filenameFilter.contains(WILDCAR_ANY)) {
            int pos = filenameFilter.indexOf(WILDCAR_ANY);
            prefix = filenameFilter.substring(0, pos);
            suffix = filenameFilter.substring(pos + 1);
        }

        final String finalPrefix = prefix;
        final String finalSuffix = suffix;

        FilenameFilter sqlfilter = (dir, name) -> {
            if (finalPrefix.isEmpty() && finalSuffix.isEmpty()) {
                return filenameFilter.equals(name);
            }

            if (!finalPrefix.isEmpty() && !name.startsWith(finalPrefix)) {
                return false;
            }

            if (!finalSuffix.isEmpty() && !name.endsWith(finalSuffix)) {
                return false;
            }

            return true;
        };

        String[] dirContents = new String[] {};
        File sqlDir = new File(scanPath);
        if (sqlDir.isDirectory()) {
            dirContents = sqlDir.list(sqlfilter);
        }

        List<String> dropScripts = new ArrayList<>();
        List<String> createScripts = new ArrayList<>();
        List<String> seedScripts = new ArrayList<>();
        List<String> deleteScripts = new ArrayList<>();

        String sep = String.valueOf(File.separatorChar);
        for (String sqlItem : dirContents) {
            String sqlFileName = scanPath + (scanPath.endsWith(sep) ? "" : sep) + sqlItem;
            File sqlFile = new File(sqlFileName);
            if (sqlFile.isFile() && sqlItem.endsWith("_drop.sql")) {
                dropScripts.add(String.format(RUN_SCRIPT_CMD, sqlFileName));
            }
            if (sqlFile.isFile() && sqlItem.endsWith("_create.sql")) {
                createScripts.add(String.format(RUN_SCRIPT_CMD, sqlFileName));
            }
            if (sqlFile.isFile() && sqlItem.endsWith("_seed.sql")) {
                seedScripts.add(String.format(RUN_SCRIPT_CMD, sqlFileName));
            }
            if (sqlFile.isFile() && sqlItem.endsWith("_delete.sql")) {
                deleteScripts.add(String.format(RUN_SCRIPT_CMD, sqlFileName));
            }
        }

        addQueries(dropScripts);
        addQueries(createScripts);
        addQueries(seedScripts);
        addQueries(deleteScripts);
        return this;
    }

    /**
     * Creates and configure a {@link SimpleSqlScriptExecutor} adding the SQL script from the resources that matches the supplied name
     *
     * @param resourceName
     *            the name of the requested resource file
     * @return
     */
    public SimpleSqlScriptExecutor scanResources(String resourceName) {

        clearQueries();
        addQuery(String.format(RUN_RESOURCE_CMD, resourceName));
        return this;
    }

    /**
     * Creates and configure a {@link SimpleSqlScriptExecutor} adding all the sql scripts matching the filter in the default path {@link SimpleSqlScriptExecutor#DEFAULT_SCRIPTS_PATH}
     *
     * @param filenameFilter
     * @return
     */
    public SimpleSqlScriptExecutor scanScripts(String filenameFilter) {
        return scanScripts(DEFAULT_SCRIPTS_PATH, filenameFilter);
    }

    /**
     * Add a query to the query string list
     *
     * @param sqlString
     * @return
     */
    public SimpleSqlScriptExecutor addQuery(String sqlString) {
        queryStrings.add(sqlString);
        return this;
    }

    /**
     * Add a queries to the query string list
     *
     * @param sqlStrings
     * @return
     */
    public SimpleSqlScriptExecutor addQueries(List<String> sqlStrings) {
        if (sqlStrings == null) {
            return this;
        }

        queryStrings.addAll(sqlStrings);

        return this;
    }

    /**
     * Execute all the queries using the provided entity manager
     *
     * @param entityManager
     * @return
     */
    public int executeUpdate(EntityManager entityManager) {
        int i = 0;

        for (String qStr : queryStrings) {
            logger.info("Running script: " + qStr);
            Query q = entityManager.createNativeQuery(qStr);
            q.executeUpdate();
            i++;
        }

        return i;
    }
}
