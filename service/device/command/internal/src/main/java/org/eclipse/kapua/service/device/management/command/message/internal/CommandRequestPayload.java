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
package org.eclipse.kapua.service.device.management.command.message.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.command.internal.CommandAppProperties;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;

/**
 * Device command request payload.
 * 
 * @since 1.0
 * 
 */
public class CommandRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload {

    /**
     * Get the command argument list
     * 
     * @return
     */
    public String[] getArguments() {
        List<String> argumentsList = new ArrayList<>();

        for (int i = 0;; i++) {
            String value = (String) getMetrics().get(CommandAppProperties.APP_PROPERTY_ARG.getValue() + i);
            if (value != null) {
                argumentsList.add(value);
            } else {
                break;
            }
        }

        if (argumentsList.isEmpty()) {
            return null;
        } else {
            return argumentsList.toArray(new String[argumentsList.size()]);
        }
    }

    /**
     * Set the command argument list
     * 
     * @param arguments
     */
    public void setArguments(String[] arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                getMetrics().put(CommandAppProperties.APP_PROPERTY_ARG.getValue() + i, arguments[i]);
            }
        }
    }

    /**
     * Get the environment pairs
     * 
     * @return
     */
    public String[] getEnvironmentPairs() {
        List<String> v = new ArrayList<>();

        for (int i = 0;; i++) {
            String value = (String) getMetrics().get(CommandAppProperties.APP_PROPERTY_ENVP.getValue() + i);
            if (value != null) {
                v.add(value);
            } else {
                break;
            }
        }

        if (v.isEmpty()) {
            return null;
        } else {
            return v.toArray(new String[v.size()]);
        }
    }

    /**
     * Set the environment pairs
     * 
     * @param environmentPairs
     */
    public void setEnvironmentPairs(String[] environmentPairs) {
        if (environmentPairs != null) {
            for (int i = 0; i < environmentPairs.length; i++) {
                getMetrics().put(CommandAppProperties.APP_PROPERTY_ENVP.getValue() + i, environmentPairs[i]);
            }
        }
    }

    /**
     * Get the working directory
     * 
     * @return
     */
    public String getWorkingDir() {
        return (String) getMetrics().get(CommandAppProperties.APP_PROPERTY_DIR.getValue());
    }

    /**
     * Set the working directory
     * 
     * @param workingDir
     */
    public void setWorkingDir(String workingDir) {
        if (workingDir != null) {
            getMetrics().put(CommandAppProperties.APP_PROPERTY_DIR.getValue(), workingDir);
        }
    }

    /**
     * Get the standard input
     * 
     * @return
     */
    public String getStdin() {
        return (String) getMetrics().get(CommandAppProperties.APP_PROPERTY_STDIN.getValue());
    }

    /**
     * Set the standard input
     * 
     * @param stdin
     */
    public void setStdin(String stdin) {
        if (stdin != null) {
            getMetrics().put(CommandAppProperties.APP_PROPERTY_STDIN.getValue(), stdin);
        }
    }

    /**
     * Get the command timeout
     * 
     * @return
     */
    public Integer getTimeout() {
        return (Integer) getMetrics().get(CommandAppProperties.APP_PROPERTY_TOUT.getValue());
    }

    /**
     * Set the command timeout
     * 
     * @param timeout
     */
    public void setTimeout(int timeout) {
        getMetrics().put(CommandAppProperties.APP_PROPERTY_TOUT.getValue(), Integer.valueOf(timeout));
    }

    /**
     * Get the run asynchronously flag
     * 
     * @return
     */
    public Boolean isRunAsync() {
        return (Boolean) getMetrics().get(CommandAppProperties.APP_PROPERTY_ASYNC.getValue());
    }

    /**
     * Set the run asynchronously flag
     * 
     * @param runAsync
     */
    public void setRunAsync(boolean runAsync) {
        getMetrics().put(CommandAppProperties.APP_PROPERTY_ASYNC.getValue(), Boolean.valueOf(runAsync));
    }

    /**
     * Set the command
     * 
     * @param cmd
     */
    public void setCommand(String cmd) {
        getMetrics().put(CommandAppProperties.APP_PROPERTY_CMD.getValue(), cmd);
    }

    /**
     * Get the command
     * 
     * @return
     */
    public String getCommand() {
        return (String) getMetrics().get(CommandAppProperties.APP_PROPERTY_CMD.getValue());
    }

    /**
     * Set the password
     * 
     * @param password
     */
    public void setPassword(String password) {
        getMetrics().put(CommandAppProperties.APP_PROPERTY_PASSWORD.getValue(), password);
    }

    /**
     * Get the password
     * 
     * @return
     */
    public String getPassword() {
        return (String) getMetrics().get(CommandAppProperties.APP_PROPERTY_PASSWORD.getValue());
    }
}
