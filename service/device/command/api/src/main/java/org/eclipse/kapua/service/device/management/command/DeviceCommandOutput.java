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

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Device command output entity definition.
 *
 * @since 1.0
 */
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
public interface DeviceCommandOutput extends KapuaSerializable {

    /**
     * Get the standard error
     *
     * @return
     */
    @XmlElement(name = "stderr")
    String getStderr();

    /**
     * Set the standard error
     *
     * @param stderr
     */
    void setStderr(String stderr);

    /**
     * Get the standard output
     *
     * @return
     */
    @XmlElement(name = "stdout")
    String getStdout();

    /**
     * Set the standard output
     *
     * @param stdout
     */
    void setStdout(String stdout);

    /**
     * Get the command execution exception message
     *
     * @return
     */
    @XmlElement(name = "exceptionMessage")
    String getExceptionMessage();

    /**
     * Set the command execution exception message
     *
     * @param exceptionMessage
     */
    void setExceptionMessage(String exceptionMessage);

    /**
     * Get the command execution exception stack
     *
     * @return
     */
    @XmlElement(name = "exceptionStack")
    String getExceptionStack();

    /**
     * Set the command execution exception stack
     *
     * @param exceptionStack
     */
    void setExceptionStack(String exceptionStack);

    /**
     * Get the command execution exit code
     *
     * @return
     */
    @XmlElement(name = "exitCode")
    Integer getExitCode();

    /**
     * Set the command execution exit code
     *
     * @param exitCode
     */
    void setExitCode(Integer exitCode);

    /**
     * Get the command execution timed out flag
     *
     * @return
     */
    @XmlElement(name = "hasTimedout")
    Boolean getHasTimedout();

    /**
     * Set the command execution timed out flag
     *
     * @param hasTimedout
     */
    void setHasTimedout(Boolean hasTimedout);
}
