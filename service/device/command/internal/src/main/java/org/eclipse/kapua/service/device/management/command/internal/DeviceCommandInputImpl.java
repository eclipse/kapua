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
package org.eclipse.kapua.service.device.management.command.internal;

import java.util.Date;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;

/**
 * Device command input entity implementation.
 *
 * @since 1.0
 *
 */
public class DeviceCommandInputImpl implements DeviceCommandInput {

    private String command;
    private String password;
    private String[] arguments;
    private Integer timeout;
    private String workingDir;
    private byte[] body;
    private String[] envVars;
    private boolean runAsync;
    private String stdIn;

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public void setBody(byte[] bytes) {
        this.body = bytes;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public void setEnvironment(String[] environment) {
        this.envVars = environment;
    }

    @Override
    public String[] getEnvironment() {
        return envVars;
    }

    @Override
    public void setRunAsynch(boolean runAsync) {
        this.runAsync = runAsync;
    }

    @Override
    public boolean isRunAsynch() {
        return runAsync;
    }

    @Override
    public void setStdin(String stdIn) {
        this.stdIn = stdIn;
    }

    @Override
    public String getStdin() {
        return stdIn;
    }

    @Override
    public KapuaId getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(KapuaId id) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaId getScopeId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getCreatedOn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaId getCreatedBy() {
        // TODO Auto-generated method stub
        return null;
    }
}
