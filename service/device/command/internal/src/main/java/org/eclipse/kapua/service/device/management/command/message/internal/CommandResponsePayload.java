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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.command.message.internal;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;

public class CommandResponsePayload extends KapuaResponsePayloadImpl implements KapuaPayload
{
    public static final String APP_PROPERTY_STDERR    = "kapua.cmd.stderr";
    public static final String APP_PROPERTY_STDOUT    = "kapua.cmd.stdout";
    public static final String APP_PROPERTY_EXIT_CODE = "kapua.cmd.exit.code";
    public static final String APP_PROPERTY_TIMEDOUT  = "kapua.cmd.timedout";

    public String getStderr()
    {
        return (String) getProperties().get(APP_PROPERTY_STDERR);
    }

    public void setStderr(String stderr)
    {
        getProperties().put(APP_PROPERTY_STDERR, stderr);
    }

    public String getStdout()
    {
        return (String) getProperties().get(APP_PROPERTY_STDOUT);
    }

    public void setStdout(String stdout)
    {
        getProperties().put(APP_PROPERTY_STDOUT, stdout);
    }

    public Integer getExitCode()
    {
        return (Integer) getProperties().get(APP_PROPERTY_EXIT_CODE);
    }

    public void setExitCode(Integer exitCode)
    {
        getProperties().put(APP_PROPERTY_EXIT_CODE, exitCode);
    }

    public Boolean hasTimedout()
    {
        return (Boolean) getProperties().get(APP_PROPERTY_TIMEDOUT);
    }

    public void setTimedout(boolean timedout)
    {
        getProperties().put(APP_PROPERTY_TIMEDOUT, timedout);
    }

}
