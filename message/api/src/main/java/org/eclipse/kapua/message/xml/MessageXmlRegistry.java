/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.xml;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.transport.TransportChannel;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportPayload;

/**
 * Message XML factory class
 */
@XmlRegistry
public class MessageXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final KapuaMessageFactory factory = locator.getFactory(KapuaMessageFactory.class);

    public KapuaPayload newPayload() {
        return factory.newPayload();
    }

    public KapuaMessage<?,?> newKapuaMessage() {
        return factory.newMessage();
    }

    public KapuaDataMessage newKapuaDataMessage() {
        return factory.newKapuaDataMessage();
    }

    public KapuaChannel newKapuaChannel() {
        return factory.newChannel();
    }

    public KapuaPosition newPosition() {
        return factory.newPosition();
    }

    public KapuaDataChannel newKapuaDataChannel() {
        return factory.newKapuaDataChannel();
    }

    public KapuaDataPayload newKapuaDataPayload() {
        return factory.newKapuaDataPayload();
    }

    public TransportMessage newTransportMessage() {
        return factory.newTransportMessage();
    }

    public TransportChannel newTransportChannel() {
        return factory.newTransportChannel();
    }

    public TransportPayload newTransportPayload() {
        return factory.newTransportPayload();
    }
}
