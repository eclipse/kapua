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

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Message XML factory class
 */
@XmlRegistry
public class MessageXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaMessageFactory KAPUA_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaMessageFactory.class);

    public KapuaPayload newPayload() {
        return KAPUA_MESSAGE_FACTORY.newPayload();
    }

    public KapuaMessage newKapuaMessage() {
        return KAPUA_MESSAGE_FACTORY.newMessage();
    }

    public KapuaDataMessage newKapuaDataMessage() {
        return KAPUA_MESSAGE_FACTORY.newKapuaDataMessage();
    }

    public KapuaChannel newKapuaChannel() {
        return KAPUA_MESSAGE_FACTORY.newChannel();
    }

    public KapuaPosition newPosition() {
        return KAPUA_MESSAGE_FACTORY.newPosition();
    }

    public KapuaDataChannel newKapuaDataChannel() {
        return KAPUA_MESSAGE_FACTORY.newKapuaDataChannel();
    }

    public KapuaDataPayload newKapuaDataPayload() {
        return KAPUA_MESSAGE_FACTORY.newKapuaDataPayload();
    }

    public TransportMessage newTransportMessage() {
        return KAPUA_MESSAGE_FACTORY.newTransportMessage();
    }

    public TransportChannel newTransportChannel() {
        return KAPUA_MESSAGE_FACTORY.newTransportChannel();
    }

    public TransportPayload newTransportPayload() {
        return KAPUA_MESSAGE_FACTORY.newTransportPayload();
    }
}
