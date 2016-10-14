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
package org.eclipse.kapua.service.device.management.command.internal;

import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;

public class DeviceCommandOutputImpl implements DeviceCommandOutput
{
	
	private String stdErr;
	private String stdOut;
	private String exceptionMessage;
	private String exceptionStack;
	private Integer exitCode;
    private Boolean timedOut;

	@Override
    public String getStderr()
    {
        return stdErr;
    }

    @Override
    public void setStderr(String stderr)
    {
        this.stdErr= stderr;
    }

    @Override
    public String getStdout()
    {
        return stdOut;
    }

    @Override
    public void setStdout(String stdout)
    {
        this.stdOut = stdout;
    }

    @Override
    public String getExceptionMessage()
    {
        return exceptionMessage;
    }

    @Override
    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage= exceptionMessage;
    }

    @Override
    public String getExceptionStack()
    {
        return exceptionStack;
    }

    @Override
    public void setExceptionStack(String exceptionStack)
    {
        this.exceptionStack = exceptionStack;
    }

    @Override
    public Integer getExitCode()
    {
        return exitCode;
    }

    @Override
    public void setExitCode(Integer exitCode)
    {
        this.exitCode = exitCode;
    }

    @Override
    public Boolean getHasTimedout()
    {
        return timedOut;
    }

    @Override
    public void setHasTimedout(Boolean hasTimedout)
    {
       this.timedOut= hasTimedout;
    }

}
