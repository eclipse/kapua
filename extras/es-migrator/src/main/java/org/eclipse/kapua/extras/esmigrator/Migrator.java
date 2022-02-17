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
package org.eclipse.kapua.extras.esmigrator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSetting;
import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSettingKey;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.core.MainResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Migrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Migrator.class.getName());

    private static final int DEFAULT_ELASTICSEARCH_SOCKET_TIMEOUT = 30000;
    private static final int DEFAULT_ELASTICSEARCH_BATCH_SIZE = 100;
    private static final int DEFAULT_ELASTICSEARCH_TASK_POLLING_INTERVAL = 30000;
    private static final String DEFAULT_JDBC_CONNECTION_STRING = "jdbc:h2:tcp://localhost:3306/kapuadb;schema=kapuadb";
    private static final String DEFAULT_JDBC_USERNAME = "kapua";
    private static final String DEFAULT_JDBC_PASSWORD = "kapua";

    private final EsClusterDescriptor esClusterDescriptor;
    private final boolean reportToFile;
    private final String jdbcConnectionString;
    private final String jdbcUsername;
    private final String jdbcPassword;
    private final int esSocketTimeout;
    private final boolean esIgnoreSslCertificate;
    private final int batchSize;
    private final int taskPollingInterval;
    private final Map<String, String> migrationReport = new TreeMap<>();

    public Migrator() {
        EsMigratorSetting esMigratorSetting = EsMigratorSetting.getInstance();
        esClusterDescriptor = new EsClusterDescriptor();
        reportToFile = esMigratorSetting.getBoolean(EsMigratorSettingKey.MIGRATOR_REPORT_TO_FILE, false);
        jdbcConnectionString = esMigratorSetting.getString(EsMigratorSettingKey.MIGRATOR_JDBC_CONNECTION_STRING, DEFAULT_JDBC_CONNECTION_STRING);
        jdbcUsername = esMigratorSetting.getString(EsMigratorSettingKey.MIGRATOR_JDBC_USERNAME, DEFAULT_JDBC_USERNAME);
        jdbcPassword = esMigratorSetting.getString(EsMigratorSettingKey.MIGRATOR_JDBC_PASSWORD, DEFAULT_JDBC_PASSWORD);
        esSocketTimeout = esMigratorSetting.getInt(EsMigratorSettingKey.ELASTICSEARCH_SOCKET_TIMEOUT, DEFAULT_ELASTICSEARCH_SOCKET_TIMEOUT);
        batchSize = esMigratorSetting.getInt(EsMigratorSettingKey.ELASTICSEARCH_BATCH_SIZE, DEFAULT_ELASTICSEARCH_BATCH_SIZE);
        taskPollingInterval = esMigratorSetting.getInt(EsMigratorSettingKey.ELASTICSEARCH_TASK_POLLING_INTERVAL, DEFAULT_ELASTICSEARCH_TASK_POLLING_INTERVAL);
        esIgnoreSslCertificate = esMigratorSetting.getBoolean(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_SSL_IGNORE_CERTIFICATE, false);
    }

    void doMigrate() {
        Set<String> accountIds = gatherAccountIds();
        if (accountIds == null) {
            LOGGER.error("Unable to gather Account IDs from the DB. Migration failed.");
            return;
        }
        try (EsClientWrapper client = new EsClientWrapper(esClusterDescriptor, esSocketTimeout, batchSize, taskPollingInterval, esIgnoreSslCertificate)) {
            // Determine ES Version
            MainResponse mainResponse = client.info();
            LOGGER.debug("Elasticsearch Version {}", mainResponse.getVersion());
            String version = mainResponse.getVersion().getNumber();
            if (!version.startsWith("7")) {
                LOGGER.error("This version of the Migration Tool MUST run against an Elasticsearch 7 cluster. Version found: {}", version);
            } else {
                Es7Migration migration = new Es7Migration(client, esClusterDescriptor, migrationReport);
                for (String accountId : accountIds) {
                    try {
                        migration.migrateAccountIndices(accountId);
                    } catch (IOException | ElasticsearchException exception) {
                        LOGGER.error("Unmanaged Elasticsearch exception in migration steps: {}", MigratorUtils.getExceptionMessageOrName(exception));
                    }
                }
            }
        } catch (IOException | ElasticsearchException exception) {
            LOGGER.error("Unmanaged Elasticsearch exception in pre-migration steps: {}", MigratorUtils.getExceptionMessageOrName(exception));
        }
        printFinalReport("Migration Report");
        if (reportToFile) {
            writeReportToFile();
        }
    }


    private Set<String> gatherAccountIds() {
        Set<String> accountIds = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(jdbcConnectionString, jdbcUsername, jdbcPassword)) {
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT DISTINCT(id) FROM ACT_ACCOUNT;";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    accountIds.add(resultSet.getBigDecimal(1).toPlainString());
                }
            }
        } catch (Exception exception) {
            LOGGER.warn("Unable to gather Account IDs from the DB: {}", exception.getMessage());
            return null;
        }
        return accountIds;
    }

    private void printFinalReport(String title) {
        String separator = "======================";
        LOGGER.info(separator);
        LOGGER.info(title);
        LOGGER.info(separator);
        for (Map.Entry<String, String> resultsEntry : migrationReport.entrySet()) {
            LOGGER.info("index: {} - result: {}", resultsEntry.getKey(), resultsEntry.getValue());
        }
        LOGGER.info(separator);
        LOGGER.info("{} END", title);
        LOGGER.info(separator);
    }

    private void writeReportToFile() {
        File reportDir;
        try {
            reportDir = new File("reports");
            reportDir.mkdir();
        } catch (SecurityException securityException) {
            LOGGER.warn("Unable to create report file", securityException);
            return;
        }
        File reportFile = new File(reportDir, new Date().toString().replace(" ", "_") + ".txt");
        try (PrintWriter printWriter = new PrintWriter(reportFile)) {
            for (Map.Entry<String, String> resultsEntry : migrationReport.entrySet()) {
                printWriter.println(String.format("index: %s - result: %s", resultsEntry.getKey(), resultsEntry.getValue()));
            }
        } catch (IOException ioException) {
            LOGGER.warn("Unable to write report file", ioException);
        }
    }

}
