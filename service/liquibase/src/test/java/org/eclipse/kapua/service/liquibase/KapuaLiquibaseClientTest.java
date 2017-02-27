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

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KapuaLiquibaseClientTest {

    @Test
    public void shouldCreateTable() throws Exception {
        // When
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "", "").update();

        // Then
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:kapua;MODE=MySQL", "", "");
        ResultSet sqlResults = connection.prepareStatement("SHOW TABLES").executeQuery();
        List<String> tables = new LinkedList<>();
        while(sqlResults.next()) {
            tables.add(sqlResults.getString(1));
        }
        assertThat(tables).contains("act_account");
    }

    @Test
    public void shouldCreateTableOnlyOnce() throws Exception {
        // When
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "", "").update();
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "", "").update();

        // Then
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:kapua;MODE=MySQL", "", "");
        ResultSet sqlResults = connection.prepareStatement("SHOW TABLES").executeQuery();
        List<String> tables = new LinkedList<>();
        while(sqlResults.next()) {
            tables.add(sqlResults.getString(1));
        }
        assertThat(tables).contains("act_account");
    }

}
