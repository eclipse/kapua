/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.app;

/**
 * Command metrics properties definition.
 * 
 * @since 1.0
 *
 */
public enum CommandMetrics
{
    /**
     * Application identifier
     */
    APP_ID("CMD"),
    /**
     * Application version
     */
    APP_VERSION("V1"),

    // Request
    /**
     * Command
     */
    APP_METRIC_CMD("command.command"),
    /**
     * ARGUMENT
     */
    APP_METRIC_ARG("command.argument"),
    /**
     * Environment
     */
    APP_METRIC_ENVP("command.environment.pair"),
    /**
     * Working directory
     */
    APP_METRIC_DIR("command.working.directory"),
    /**
     * Standard input
     */
    APP_METRIC_STDIN("command.stdin"),
    /**
     * Command timeout
     */
    APP_METRIC_TOUT("command.timeout"),
    /**
     * Asynchronous running
     */
    APP_METRIC_ASYNC("command.run.async"),
    /**
     * Password
     */
    APP_METRIC_PASSWORD("command.password"),

    // Response
    /**
     * Standard error
     */
    APP_METRIC_STDERR("command.stderr"),
    /**
     * Standard output
     */
    APP_METRIC_STDOUT("command.stdout"),
    /**
     * Command exit code
     */
    APP_METRIC_EXIT_CODE("command.exit.code"),
    /**
     * Command timed out flag
     */
    APP_METRIC_TIMED_OUT("command.timedout"),
    ;

    private String value;

    CommandMetrics(String value)
    {
        this.value = value;
    }

    /**
     * Get a value property associated to this specific enumeration key.
     * 
     * @return
     */
    public String getValue()
    {
        return value;
    }
}
