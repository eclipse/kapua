/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import com.ibm.jbatch.container.exception.PersistenceException;
import com.ibm.jbatch.container.services.IPersistenceManagerService;
import com.ibm.jbatch.container.services.impl.JDBCPersistenceManagerImpl;

public class KapuaJDBCPersistenceManagerImpl extends JDBCPersistenceManagerImpl implements IPersistenceManagerService {

    private static final String CLASSNAME = KapuaJDBCPersistenceManagerImpl.class.getName();

    private static final String JDBC_CONNECTION_URL = JdbcConnectionUrlResolvers.resolveJdbcUrl();

    private static final SystemSetting CONFIG = SystemSetting.getInstance();
    private static final String USERNAME = CONFIG.getString(SystemSettingKey.DB_USERNAME);
    private static final String PASSWORD = CONFIG.getString(SystemSettingKey.DB_PASSWORD);

    private final static Logger logger = Logger.getLogger(CLASSNAME);

    protected Connection getConnectionToDefaultSchema() throws SQLException {
        return getConnection();
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_CONNECTION_URL, USERNAME, PASSWORD);
    }

    public void purgeByName(String jobName) {
        logger.entering(CLASSNAME, "purgeByName", jobName);
        String deleteJobs = "DELETE FROM jobinstancedata WHERE name = ?";
        String deleteJobExecutions = "DELETE FROM executioninstancedata "
                + "WHERE jobexecid IN ("
                + "SELECT B.jobexecid FROM jobinstancedata A INNER JOIN executioninstancedata B "
                + "ON A.jobinstanceid = B.jobinstanceid "
                + "WHERE A.name = ?)";
        String deleteStepExecutions = "DELETE FROM stepexecutioninstancedata "
                + "WHERE stepexecid IN ("
                + "SELECT C.stepexecid FROM jobinstancedata A INNER JOIN executioninstancedata B "
                + "ON A.jobinstanceid = B.jobinstanceid INNER JOIN stepexecutioninstancedata C "
                + "ON B.jobexecid = C.jobexecid "
                + "WHERE A.name = ?)";

        try (Connection conn = getConnection()) {
            try(PreparedStatement statement = conn.prepareStatement(deleteStepExecutions)) {
                statement.setString(1, jobName);
                statement.executeUpdate();
            }

            try(PreparedStatement statement = conn.prepareStatement(deleteJobExecutions)) {
                statement.setString(1, jobName);
                statement.executeUpdate();
            }
            try(PreparedStatement statement = conn.prepareStatement(deleteJobs)) {
                statement.setString(1, jobName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        logger.exiting(CLASSNAME, "purge");
    }

}
