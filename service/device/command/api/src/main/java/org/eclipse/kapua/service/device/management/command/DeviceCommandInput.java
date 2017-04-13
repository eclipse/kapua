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
package org.eclipse.kapua.service.device.management.command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaEntity;

/**
 * Device command input entity definition.
 * 
 * @since 1.0
 *
 */
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

    /**
     * Get the device command
     * 
     * @return
     */
    @XmlElement(name = "command")
    public String getCommand();
    
    /**
     * Set the device command
     * 
     * @param command
     */
    public void setCommand(String command);

    /**
     * Get the device password
     * 
     * @return
     */
    @XmlElement(name = "password")
    public String getPassword();
    
    /**
     * Set the device password
     * 
     * @param password
     */
    public void setPassword(String password);    

    /**
     * Get command arguments
     * 
     * @return
     */
    @XmlElementWrapper(name = "arguments")
    @XmlElement(name = "argument")
    public String[] getArguments();
    
    /**
     * Set command arguments
     * 
     * @param arguments
     */
    public void setArguments(String[] arguments);    

    /**
     * Get the command timeout
     * 
     * @return
     */
    @XmlElement(name = "timeout")
    public Integer getTimeout();
    
    /**
     * Set the command timeout
     * 
     * @param timeout
     */
    public void setTimeout(Integer timeout);

    /**
     * Get the working directory
     * 
     * @return
     */
    @XmlElement(name = "workingDir")
    public String getWorkingDir();
    
    /**
     * Set the working directory
     * 
     * @param workingDir
     */
    public void setWorkingDir(String workingDir);

    /**
     * Get the command input body
     * 
     * @return
     */
    @XmlElement(name = "body")
    public byte[] getBody();
    
    /**
     * Set the command input body
     * 
     * @param bytes
     */
    public void setBody(byte[] bytes);    

    /**
     * Get the environment attributes
     * 
     * @return
     */
    @XmlElement(name = "environment")
    public String[] getEnvironment();
    
    /**
     * Set the environment attributes
     * 
     * @param environment
     */
    public void setEnvironment(String[] environment);
    
    /**
     * Get the asynchronous run flag
     * 
     * @return
     */
    @XmlElement(name = "runAsynch")
    public boolean isRunAsynch();
    
    /**
     * Set the asynchronous run flag
     * 
     * @param runAsync
     */
    public void setRunAsynch(boolean runAsync);

    /**
     * Get the device standard input
     * 
     * @return
     */
    @XmlElement(name = "stdin")
    public String getStdin();
    
    /**
     * Set the device standard input
     * 
     * @param stdin
     */
    public void setStdin(String stdin);
}
