/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc- initial API and implementation
 *******************************************************************************/
package liquibase.ext.logging.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.logging.core.AbstractLogger;

/**
 * A logger implementation for liquibase
 * <p>
 * <strong>Note:</strong> This class must reside in a package starting with <code>liquibase.ext.logging</code>.
 * </p>
 */
public class LoggerImpl extends AbstractLogger {

    private Logger logger;

    @Override
    public void setName(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    @Override
    public void setLogLevel(String logLevel, String logFile) {
    }

    @Override
    public void severe(String message) {
        logger.error(message);
    }

    @Override
    public void severe(String message, Throwable e) {
        logger.error(message, e);
    }

    @Override
    public void warning(String message) {
        logger.warn(message);
    }

    @Override
    public void warning(String message, Throwable e) {
        logger.warn(message, e);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String message, Throwable e) {
        logger.info(message, e);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(String message, Throwable e) {
        logger.debug(message, e);
    }

    @Override
    public void setChangeLog(DatabaseChangeLog databaseChangeLog) {
    }

    @Override
    public void setChangeSet(ChangeSet changeSet) {
    }

    @Override
    public int getPriority() {
        return Integer.getInteger("org.eclipse.kapua.liquibase.logger.priority", 10);
    }
}
