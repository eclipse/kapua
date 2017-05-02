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
 *******************************************************************************/
package org.eclipse.kapua.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class DatabaseInstance {

    private Connection connection;

    
    public DatabaseInstance() throws SQLException {
        // FIXME: find out why start/stop don't get called
        connection = DriverManager.getConnection("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua");
    }
    
    @Before(order = 100)
    public void start() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua");
    }

    @After
    public void stop() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
