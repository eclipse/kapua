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

public class CommandRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload
{
    public String[] getArguments()
    {
        List<String> argumentsList = new ArrayList<>();

        for (int i = 0;; i++) {
            String value = (String) getProperties().get(CommandAppProperties.APP_PROPERTY_ARG.getValue() + i);
            if (value != null) {
                argumentsList.add(value);
            }
            else {
                break;
            }
        }

        if (argumentsList.isEmpty()) {
            return null;
        }
        else {
            return argumentsList.toArray(new String[argumentsList.size()]);
        }
    }

    public void setArguments(String[] arguments)
    {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                getProperties().put(CommandAppProperties.APP_PROPERTY_ARG.getValue() + i, arguments[i]);
            }
        }
    }

    public String[] getEnvironmentPairs()
    {
        List<String> v = new ArrayList<>();

        for (int i = 0;; i++) {
            String value = (String) getProperties().get(CommandAppProperties.APP_PROPERTY_ENVP.getValue() + i);
            if (value != null) {
                v.add(value);
            }
            else {
                break;
            }
        }

        if (v.isEmpty()) {
            return null;
        }
        else {
            return v.toArray(new String[v.size()]);
        }
    }

    public void setEnvironmentPairs(String[] environmentPairs)
    {
        if (environmentPairs != null) {
            for (int i = 0; i < environmentPairs.length; i++) {
                getProperties().put(CommandAppProperties.APP_PROPERTY_ENVP.getValue() + i, environmentPairs[i]);
            }
        }
    }

    public String getWorkingDir()
    {
        return (String) getProperties().get(CommandAppProperties.APP_PROPERTY_DIR.getValue());
    }

    public void setWorkingDir(String workingDir)
    {
        if (workingDir != null) {
            getProperties().put(CommandAppProperties.APP_PROPERTY_DIR.getValue(), workingDir);
        }
    }

    public String getStdin()
    {
        return (String) getProperties().get(CommandAppProperties.APP_PROPERTY_STDIN.getValue());
    }

    public void setStdin(String stdin)
    {
        if (stdin != null) {
            getProperties().put(CommandAppProperties.APP_PROPERTY_STDIN.getValue(), stdin);
        }
    }

    public Integer getTimeout()
    {
        return (Integer) getProperties().get(CommandAppProperties.APP_PROPERTY_TOUT.getValue());
    }

    public void setTimeout(int timeout)
    {
        getProperties().put(CommandAppProperties.APP_PROPERTY_TOUT.getValue(), Integer.valueOf(timeout));
    }

    public Boolean isRunAsync()
    {
        return (Boolean) getProperties().get(CommandAppProperties.APP_PROPERTY_ASYNC.getValue());
    }

    public void setRunAsync(boolean runAsync)
    {
        getProperties().put(CommandAppProperties.APP_PROPERTY_ASYNC.getValue(), Boolean.valueOf(runAsync));
    }

    public void setCommand(String cmd)
    {
        getProperties().put(CommandAppProperties.APP_PROPERTY_CMD.getValue(), cmd);
    }

    public String getCommand()
    {
        return (String) getProperties().get(CommandAppProperties.APP_PROPERTY_CMD.getValue());
    }

    public void setPassword(String password)
    {
        getProperties().put(CommandAppProperties.APP_PROPERTY_PASSWORD.getValue(), password);
    }

    public String getPassword()
    {
        return (String) getProperties().get(CommandAppProperties.APP_PROPERTY_PASSWORD.getValue());
    }
}
