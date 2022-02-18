/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.command;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;

public class GwtDeviceCommandOutput extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 922409273611552321L;

    public Integer getExitCode() {
        return get("exitCode");
    }

    public void setExitCode(Integer exitCode) {
        set("exitCode", exitCode);
    }

    public Boolean isTimedout() {
        return get("timedout");
    }

    public void setTimedout(Boolean timedout) {
        set("timedout", timedout);
    }

    public String getStderr() {
        return get("stderr");
    }

    public void setStderr(String stderr) {
        set("stderr", stderr);
    }

    public String getStdout() {
        return get("stdout");
    }

    public void setStdout(String stdout) {
        set("stdout", stdout);
    }

    public String getExceptionMessage() {
        return get("exceptionMessage");
    }

    public void setExceptionMessage(String exceptionMessage) {
        set("exceptionMessage", exceptionMessage);
    }

    public String getExceptionStack() {
        return get("exceptionStack");
    }

    public void setExceptionStack(String exceptionStack) {
        set("exceptionStack", exceptionStack);
    }
}
