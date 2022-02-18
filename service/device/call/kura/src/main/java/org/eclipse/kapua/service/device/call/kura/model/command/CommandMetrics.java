/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.command;

import org.eclipse.kapua.service.device.call.message.DeviceMetrics;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppMetrics;

/**
 * Command {@link DeviceMetrics}.
 *
 * @since 1.0.0
 */
public enum CommandMetrics implements DeviceAppMetrics {

    /**
     * Application identifier.
     *
     * @since 1.0.0
     */
    APP_ID("CMD"),

    /**
     * Application version.
     *
     * @since 1.0.0
     */
    APP_VERSION("V1"),

    // Request

    /**
     * Command metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_CMD("command.command"),

    /**
     * Arguments metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_ARG("command.argument"),

    /**
     * Environment metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_ENVP("command.environment.pair"),

    /**
     * Working directory metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_DIR("command.working.directory"),

    /**
     * Standard input metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_STDIN("command.stdin"),

    /**
     * Command timeout metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_TOUT("command.timeout"),

    /**
     * Asynchronous running metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_ASYNC("command.run.async"),

    /**
     * Password metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_PASSWORD("command.password"),

    // Response
    /**
     * Standard error metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_STDERR("command.stderr"),

    /**
     * Standard output metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_STDOUT("command.stdout"),

    /**
     * Command exit code metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_EXIT_CODE("command.exit.code"),

    /**
     * Command timed out flag metric name.
     *
     * @since 1.0.0
     */
    APP_METRIC_TIMED_OUT("command.timedout"),
    ;

    /**
     * The name of the metric.
     *
     * @since 1.0.0
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name The name of the metric.
     * @since 1.0.0
     */
    CommandMetrics(String name) {
        this.name = name;
    }

    /**
     * Gets the value property associated to this specific enumeration key.
     *
     * @return the value property associated to this specific enumeration key.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Renamed to {@link #getName()}.
     */
    @Deprecated
    public String getValue() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
