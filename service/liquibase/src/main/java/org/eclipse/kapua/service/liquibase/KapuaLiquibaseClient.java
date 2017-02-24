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
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class KapuaLiquibaseClient {

    private final String jdbcUrl;

    private final String username;

    private final String password;

    public KapuaLiquibaseClient(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void update() {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Enumeration<URL> changeLogs = getClass().getClassLoader().getResources("liquibase.sql");
            while (changeLogs.hasMoreElements()) {
                File changelogFile = File.createTempFile("kapua", ".sql");
                IOUtils.write(IOUtils.toString(changeLogs.nextElement()), new FileOutputStream(changelogFile));
                Liquibase liquibase = new Liquibase(changelogFile.getAbsolutePath(), new FileSystemResourceAccessor(), new JdbcConnection(connection));
                liquibase.update(null);
            }
        } catch (LiquibaseException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
