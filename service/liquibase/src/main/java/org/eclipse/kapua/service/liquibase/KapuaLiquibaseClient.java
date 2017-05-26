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

import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.SystemUtils.getJavaIoTmpDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

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
            if (parseBoolean(System.getProperty("LIQUIBASE_ENABLED", "true")) || parseBoolean(System.getenv("LIQUIBASE_ENABLED"))) {
                try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

                    //
                    // Copy files to temporary directory
                    String tmpDirectory = getJavaIoTmpDir().getAbsolutePath();

                    File changelogTempDirectory = new File(tmpDirectory, "kapua-liquibase");

                    if (changelogTempDirectory.exists()) {
                        FileUtils.deleteDirectory(changelogTempDirectory);
                    }

                    changelogTempDirectory.mkdirs();
                    LOG.trace("Tmp dir: {}", changelogTempDirectory.getAbsolutePath());

                    Reflections reflections = new Reflections("liquibase", new ResourcesScanner());
                    Set<String> changeLogs = reflections.getResources(Pattern.compile(".*\\.xml|.*\\.sql"));
                    for (String script : changeLogs) {
                        URL scriptUrl = getClass().getResource("/" + script);
                        File changelogFile = new File(changelogTempDirectory, script.replaceFirst("liquibase/", ""));
                        if (changelogFile.getParentFile() != null && !changelogFile.getParentFile().exists()) {
                            LOG.trace("Creating parent dir: {}", changelogFile.getParentFile().getAbsolutePath());
                            changelogFile.getParentFile().mkdirs();
                        }
                        IOUtils.write(IOUtils.toString(scriptUrl), new FileOutputStream(changelogFile));
                        LOG.trace("Copied file: {}", changelogFile.getAbsolutePath());
                    }

                    //
                    // Find and execute all master scripts
                    File[] masterChangelogs = changelogTempDirectory.listFiles((FilenameFilter) (dir, name) -> name.endsWith("-master.xml"));

                    LOG.info("Master Liquibase files found: {}", masterChangelogs.length);

                    for (File masterChangelog : masterChangelogs) {
                        LOG.info("Excuting liquibase script: {}", masterChangelog.getAbsolutePath());
                        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                        if (schema.isPresent()) {
                            database.setDefaultSchemaName(schema.get());
                        }
                        Liquibase liquibase = new Liquibase(masterChangelog.getAbsolutePath(), new FileSystemResourceAccessor(), database);
                        liquibase.update(null);

                        LOG.debug("Excuted liquibase script: {}", masterChangelog.getAbsolutePath());
                    }
                }
            }
        } catch (LiquibaseException | SQLException | IOException e) {
            LOG.error("Error while running Liquibase scripts!", e);
            throw new RuntimeException(e);
        }
    }

}
