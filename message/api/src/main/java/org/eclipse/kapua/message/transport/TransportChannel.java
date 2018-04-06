/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.transport;

import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;

import javax.xml.bind.annotation.XmlType;

/**
 * Kapua data message channel object definition.
 * 
 * @since 1.0
 *
 */
@XmlType(factoryClass = MessageXmlRegistry.class, factoryMethod = "newTransportChannel")
public interface TransportChannel extends KapuaChannel {

    /**
     * Gets the topic used in the in the transport of the message
     * 
     * @return
     */
    public String getOriginalDestination();

    /**
     * Sets the topic used in the in the transport of the message
     */
    public void setOriginalDestination(String originalDestination);

}
