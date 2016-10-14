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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = "commandInput")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "command",
        "password",
        "arguments",
        "timeout",
        "workingDir",
        "body",
        "environment",
        "runAsynch",
        "stdin"
}, factoryClass = DeviceCommandXmlRegistry.class, factoryMethod = "newCommandInput")
public interface DeviceCommandInput extends KapuaEntity {
    @XmlElement(name = "command")
    public String getCommand();
    
    public void setCommand(String command);

    @XmlElement(name = "password")
    public String getPassword();
    
    public void setPassword(String password);    

    @XmlElementWrapper(name = "arguments")
    @XmlElement(name = "argument")
    public String[] getArguments();
    
    public void setArguments(String[] arguments);    

    @XmlElement(name = "timeout")
    public Integer getTimeout();
    
    public void setTimeout(Integer timeout);

    @XmlElement(name = "workingDir")
    public String getWorkingDir();
    
    public void setWorkingDir(String workingDir);

    @XmlElement(name = "body")
    public byte[] getBody();
    
    public void setBody(byte[] bytes);    

    @XmlElement(name = "environment")
    public String[] getEnvironment();
    
    public void setEnvironment(String[] environment);
    
    @XmlElement(name = "runAsynch")
    public boolean isRunAsynch();
    
    public void setRunAsynch(boolean runAsync);

    @XmlElement(name = "stdin")
    public String getStdin();
    
    public void setStdin(String stdin);
}
