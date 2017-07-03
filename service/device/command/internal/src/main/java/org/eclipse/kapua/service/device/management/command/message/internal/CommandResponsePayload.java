/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.command.message.internal;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;

/**
 * Device command response payload.
 * 
 * @since 1.0
 * 
 */
public class CommandResponsePayload extends KapuaResponsePayloadImpl implements KapuaPayload {

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
