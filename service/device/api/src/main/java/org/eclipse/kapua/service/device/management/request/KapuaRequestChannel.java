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
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.service.device.management.KapuaAppChannel;
import org.eclipse.kapua.service.device.management.RequestMessageXmlRegistry;
import org.eclipse.kapua.service.device.management.KapuaMethod;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Kapua request message channel definition.<br>
 * This object defines the channel for a Kapua request message.<br>
 * The request message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "channel")
@XmlType(propOrder = { "method" }, factoryClass = RequestMessageXmlRegistry.class, factoryMethod = "newRequestChannel")
public interface KapuaRequestChannel extends KapuaAppChannel {

    /**
     * Get the request method
     *
     * @return
     */
    public KapuaMethod getMethod();

    /**
     * Set the request method
     *
     * @param method
     */
    public void setMethod(KapuaMethod method);
}
