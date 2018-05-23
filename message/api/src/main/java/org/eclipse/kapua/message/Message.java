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
package org.eclipse.kapua.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.KapuaSerializable;

/**
 * Message definition.
 *
 * @param <C> channel type
 * @param <P> payload type
 * @since 1.0
 */
@XmlType(propOrder = { //
        "channel", //
        "payload"
})
public interface Message<C extends Channel, P extends Payload> extends KapuaSerializable {
    /**
     * Get the message channel
     *
     * @return
     */
    @XmlElement(name = "channel")
    public C getChannel();

    /**
     * Set the message channel
     *
     * @param semanticChannel
     */
    public void setChannel(C semanticChannel);

    /**
     * Get the message payload
     *
     * @return
     */
    @XmlElement(name = "payload")
    public P getPayload();

    /**
     * Set the message payload
     *
     * @param payload
     */
    public void setPayload(P payload);
}
