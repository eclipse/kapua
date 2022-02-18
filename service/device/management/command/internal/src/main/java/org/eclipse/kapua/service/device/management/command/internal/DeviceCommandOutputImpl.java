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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.command.internal;

import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;

/**
 * {@link DeviceCommandOutput} implementation.
 *
 * @since 1.0.0
 */
public class DeviceCommandOutputImpl implements DeviceCommandOutput {

    private static final long serialVersionUID = -3987291078035621467L;

    private String stdErr;
    private String stdOut;
    private String exceptionMessage;
    private String exceptionStack;
    private Integer exitCode;
    private Boolean timedOut;

    @Override
    public String getStderr() {
        return stdErr;
    }

    @Override
    public void setStderr(String stderr) {
        this.stdErr = stderr;
    }

    @Override
    public String getStdout() {
        return stdOut;
    }

    @Override
    public void setStdout(String stdout) {
        this.stdOut = stdout;
    }

    @Override
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    @Override
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getExceptionStack() {
        return exceptionStack;
    }

    @Override
    public void setExceptionStack(String exceptionStack) {
        this.exceptionStack = exceptionStack;
    }

    @Override
    public Integer getExitCode() {
        return exitCode;
    }

    @Override
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public Boolean getHasTimedout() {
        return timedOut;
    }

    @Override
    public void setHasTimedout(Boolean hasTimedout) {
        this.timedOut = hasTimedout;
    }

}
