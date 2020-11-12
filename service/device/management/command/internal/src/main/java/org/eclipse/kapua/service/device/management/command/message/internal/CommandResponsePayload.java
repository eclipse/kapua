/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.command.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

/**
 * Device command response payload.
 *
 * @since 1.0
 */
public class CommandResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    /**
     * Standard error application property
     */
    public static final String APP_PROPERTY_STDERR = "kapua.cmd.stderr";

    /**
     * Standard output application property
     */
    public static final String APP_PROPERTY_STDOUT = "kapua.cmd.stdout";

    /**
     * Command exit code application property
     */
    public static final String APP_PROPERTY_EXIT_CODE = "kapua.cmd.exit.code";

    /**
     * Command timed out application property
     */
    public static final String APP_PROPERTY_TIMEDOUT = "kapua.cmd.timedout";

    /**
     * Get the command standard error
     *
     * @return
     */
    public String getStderr() {
        return (String) getMetrics().get(APP_PROPERTY_STDERR);
    }

    /**
     * Set the command standard error
     *
     * @param stderr
     */
    public void setStderr(String stderr) {
        getMetrics().put(APP_PROPERTY_STDERR, stderr);
    }

    /**
     * Get the command standard output
     *
     * @return
     */
    public String getStdout() {
        return (String) getMetrics().get(APP_PROPERTY_STDOUT);
    }

    /**
     * Set the command standard output
     *
     * @param stdout
     */
    public void setStdout(String stdout) {
        getMetrics().put(APP_PROPERTY_STDOUT, stdout);
    }

    /**
     * Get the command exit code
     *
     * @return
     */
    public Integer getExitCode() {
        return (Integer) getMetrics().get(APP_PROPERTY_EXIT_CODE);
    }

    /**
     * Get the command exit code
     *
     * @param exitCode
     */
    public void setExitCode(Integer exitCode) {
        getMetrics().put(APP_PROPERTY_EXIT_CODE, exitCode);
    }

    /**
     * Get the command execution timed out flag
     *
     * @return
     */
    public Boolean hasTimedout() {
        return (Boolean) getMetrics().get(APP_PROPERTY_TIMEDOUT);
    }

    /**
     * Set the command execution timed out flag
     *
     * @param timedout
     */
    public void setTimedout(boolean timedout) {
        getMetrics().put(APP_PROPERTY_TIMEDOUT, timedout);
    }

}
