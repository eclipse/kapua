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
package org.eclipse.kapua.service.device.management.command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.KapuaSerializable;

@XmlRootElement(name = "commandOutput")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "stderr",
        "stdout",
        "exceptionMessage",
        "exceptionStack",
        "exitCode",
        "hasTimedout"
}, factoryClass = DeviceCommandXmlRegistry.class, factoryMethod = "newCommandOutput")
public interface DeviceCommandOutput extends KapuaSerializable
{
    @XmlElement(name = "stderr")
    public String getStderr();

    public void setStderr(String stderr);

    @XmlElement(name = "stdout")
    public String getStdout();

    public void setStdout(String stdout);

    @XmlElement(name = "exceptionMessage")
    public String getExceptionMessage();

    public void setExceptionMessage(String exceptionMessage);

    @XmlElement(name = "exceptionStack")
    public String getExceptionStack();

    public void setExceptionStack(String exceptionStack);

    @XmlElement(name = "exitCode")
    public Integer getExitCode();

    public void setExitCode(Integer exitCode);

    @XmlElement(name = "hasTimedout")
    public Boolean getHasTimedout();

    public void setHasTimedout(Boolean hasTimedout);
}
