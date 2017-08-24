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
package org.eclipse.kapua.app.console.module.device.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;

public class GwtDeviceCommandInput extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -6088132160238324922L;

    public String getCommand() {
        return get("command");
    }

    public void setCommand(String command) {
        set("command", command);
    }

    public Integer getTimeout() {
        return get("timeout");
    }

    public void setTimeout(Integer timeout) {
        set("timeout", timeout);
    }

    public String getStdin() {
        return get("stdin");
    }

    public void setStdin(String stdin) {
        set("stdin", stdin);
    }

    public String[] getEnvironment() {
        return get("environment");
    }

    public void setEnvironment(String[] environment) {
        set("environment", environment);
    }

    public String getWorkingDir() {
        return get("workingDir");
    }

    public void setWorkingDir(String workingDir) {
        set("workingDir", workingDir);
    }

    public Boolean isRunAsynch() {
        return get("runAsynch");
    }

    public void setRunAsynch(Boolean runAsynch) {
        set("runAsynch", runAsynch);
    }

    public byte[] getZipBytes() {
        return get("zipBytes");
    }

    public void setZipBytes(byte[] zipBytes) {
        set("zipBytes", zipBytes);
    }
}
