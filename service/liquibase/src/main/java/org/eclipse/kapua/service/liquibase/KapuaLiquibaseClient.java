/*******************************************************************************
 * Copyright (c) 2011, 2017 Red Hat and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.kapua.service.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.SystemUtils.getJavaIoTmpDir;

public class KapuaLiquibaseClient {

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
            if(parseBoolean(System.getProperty("LIQUIBASE_ENABLED", "true")) || parseBoolean(System.getenv("LIQUIBASE_ENABLED"))) {
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

                Reflections reflections = new Reflections("liquibase", new ResourcesScanner());
                Set<String> changeLogs = reflections.getResources(Pattern.compile(".*\\.sql"));
                for (String script : changeLogs) {
                    URL scriptUrl = getClass().getResource("/" + script);
                    File changelogFile = new File(getJavaIoTmpDir(), "kapua-liquibase");
                    changelogFile.mkdir();
                    changelogFile = new File(changelogFile, script.replaceFirst("liquibase/", ""));
                    IOUtils.write(IOUtils.toString(scriptUrl), new FileOutputStream(changelogFile));
                    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    if(schema.isPresent()) {
                        database.setDefaultSchemaName(schema.get());
                    }
                    Liquibase liquibase = new Liquibase(changelogFile.getAbsolutePath(), new FileSystemResourceAccessor(), database);
                    liquibase.update(null);
                }
            }
        } catch (LiquibaseException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
