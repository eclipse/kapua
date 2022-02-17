/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for executing SQL scripts form resource files, not from external SQL files.
 */
public class ResourceSqlScriptExecutor {


    private static final Logger logger = LoggerFactory.getLogger(SimpleSqlScriptExecutor.class);

    private final List<String> queryStrings = new ArrayList<>();

    /**
     * Return the query list string
     *
     * @return
     */
    public List<String> getQueries() {
        return Collections.unmodifiableList(queryStrings);
    }

    /**
     * Add a queries to the query string list
     *
     * @param sqlStrings
     * @return
     */
    public ResourceSqlScriptExecutor addQueries(List<String> sqlStrings) {
        if (sqlStrings == null) {
            return this;
        }

        queryStrings.addAll(sqlStrings);

        return this;
    }

    /**
     * Add a query to the query string list
     *
     * @param sqlString single sql script
     * @return
     */
    public ResourceSqlScriptExecutor addQuery(String sqlString) {
        if (sqlString == null) {
            return this;
        }

        queryStrings.add(sqlString);

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

            logger.info("Running resources script: " + qStr);
            StringBuilder sqlStringBuilder = new StringBuilder();
            try {
                try (
                    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(qStr);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

                    String sqlLine;
                    while ((sqlLine = bufferedReader.readLine()) != null) {
                        sqlStringBuilder.append(sqlLine).append(System.lineSeparator());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Query q = entityManager.createNativeQuery(sqlStringBuilder.toString());
            q.executeUpdate();
            i++;
        }

        return i;
    }
}
