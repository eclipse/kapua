/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class KapuaLiquibaseClient {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaLiquibaseClient.class);

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final Optional<String> schema;

    public KapuaLiquibaseClient(String jdbcUrl, String username, String password, Optional<String> schema) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.schema = schema;
    }

    public KapuaLiquibaseClient(String jdbcUrl, String username, String password) {
        this(jdbcUrl, username, password, Optional.empty());
    }

    public void update() {
        try {
            if (Boolean.parseBoolean(System.getProperty("LIQUIBASE_ENABLED", "true")) || Boolean.parseBoolean(System.getenv("LIQUIBASE_ENABLED"))) {
                try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                    loadResourcesStatic(connection, schema);
                }
            }
        } catch (LiquibaseException | SQLException | IOException e) {
            LOG.error("Error while running Liquibase scripts!", e);
            throw new RuntimeException(e);
        }
    }

    protected static synchronized void loadResourcesStatic(Connection connection, Optional<String> schema) throws LiquibaseException, IOException {
        //
        // Copy files to temporary directory
        String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();

        File changelogTempDirectory = new File(tmpDirectory, "kapua-liquibase");

        if (changelogTempDirectory.exists()) {
            FileUtils.deleteDirectory(changelogTempDirectory);
        }

        changelogTempDirectory.mkdirs();
        LOG.trace("Tmp dir: {}", changelogTempDirectory.getAbsolutePath());

        Reflections reflections = new Reflections("liquibase", new ResourcesScanner());
        Set<String> changeLogs = reflections.getResources(Pattern.compile(".*\\.xml|.*\\.sql"));
        for (String script : changeLogs) {
            URL scriptUrl = KapuaLiquibaseClient.class.getResource("/" + script);
            File changelogFile = new File(changelogTempDirectory, script.replaceFirst("liquibase/", ""));
            if (changelogFile.getParentFile() != null && !changelogFile.getParentFile().exists()) {
                LOG.trace("Creating parent dir: {}", changelogFile.getParentFile().getAbsolutePath());
                changelogFile.getParentFile().mkdirs();
            }
            try (FileOutputStream tmpStream = new FileOutputStream(changelogFile)) {
                IOUtils.write(IOUtils.toString(scriptUrl), tmpStream);
            }
            LOG.trace("Copied file: {}", changelogFile.getAbsolutePath());
        }

        //
        // Find and execute all master scripts
        LOG.info("Executing pre master files...");
        executeMasters(connection, schema, changelogTempDirectory, "-master.pre.xml");
        LOG.info("Executing pre master files... DONE!");

        LOG.info("Executing master files...");
        executeMasters(connection, schema, changelogTempDirectory, "-master.xml");
        LOG.info("Executing master files... DONE!");

        LOG.info("Executing post master files...");
        executeMasters(connection, schema, changelogTempDirectory, "-master.post.xml");
        LOG.info("Executing post master files... DONE!");

    }

    private static void executeMasters(Connection connection, Optional<String> schema, File changelogTempDirectory, String preMaster) throws LiquibaseException {
        List<File> masterChangelogs = Arrays.asList(changelogTempDirectory.listFiles((FilenameFilter) (dir, name) -> name.endsWith(preMaster)));

        LOG.info("\tMaster Liquibase files found: {}", masterChangelogs.size());

        LOG.trace("\tSorting master Liquibase files found.");
        masterChangelogs.sort((f1, f2) -> f1.getAbsolutePath().compareTo(f2.getAbsolutePath()));

        for (File masterChangelog : masterChangelogs) {
            LOG.info("\t\tExcuting liquibase script: {}", masterChangelog.getAbsolutePath());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            if (schema.isPresent()) {
                database.setDefaultSchemaName(schema.get());
            }
            Liquibase liquibase = new Liquibase(masterChangelog.getAbsolutePath(), new FileSystemResourceAccessor(), database);
            liquibase.update(null);

            LOG.debug("\t\tExcuted liquibase script: {}", masterChangelog.getAbsolutePath());
        }
    }

}
