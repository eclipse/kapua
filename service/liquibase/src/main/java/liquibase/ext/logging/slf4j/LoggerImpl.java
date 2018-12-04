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

import liquibase.logging.LogType;
import liquibase.logging.core.AbstractLogger;

/**
 * A logger implementation for liquibase
 * <p>
 * <strong>Note:</strong> This class must reside in a package starting with <code>liquibase.ext.logging</code>.
 * </p>
 */
public class LoggerImpl extends AbstractLogger {

    public LoggerImpl() {
        super();
    }

    @Override
    public void severe(String message) {
        super.severe(message);
    }

    @Override
    public void severe(String message, Throwable e) {
        super.severe(message, e);
    }

    @Override
    public void warning(String message) {
        super.warning(message);
    }

    @Override
    public void warning(String message, Throwable e) {
        super.warning(message, e);
    }

    @Override
    public void info(String message) {
        super.info(message);
    }

    @Override
    public void info(String message, Throwable e) {
        super.info(message, e);
    }

    @Override
    public void debug(String message) {
        super.debug(message);
    }

    @Override
    public void debug(String message, Throwable e) {
        super.debug(message, e);
    }

    @Override
    public void severe(LogType logType, String s) {

    }

    @Override
    public void severe(LogType logType, String s, Throwable throwable) {

    }

    @Override
    public void warning(LogType logType, String s) {

    }

    @Override
    public void warning(LogType logType, String s, Throwable throwable) {

    }

    @Override
    public void info(LogType logType, String s) {

    }

    @Override
    public void info(LogType logType, String s, Throwable throwable) {

    }

    @Override
    public void debug(LogType logType, String s) {

    }

    @Override
    public void debug(LogType logType, String s, Throwable throwable) {

    }
}
