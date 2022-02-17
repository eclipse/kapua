/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Message XML factory class
 */
@XmlRegistry
public class MessageXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaMessageFactory KAPUA_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaMessageFactory.class);

    public KapuaMessage newKapuaMessage() {
        return KAPUA_MESSAGE_FACTORY.newMessage();
    }

    public KapuaChannel newKapuaChannel() {
        return KAPUA_MESSAGE_FACTORY.newChannel();
    }

    public KapuaPayload newPayload() {
        return KAPUA_MESSAGE_FACTORY.newPayload();
    }

    public KapuaPosition newPosition() {
        return KAPUA_MESSAGE_FACTORY.newPosition();
    }
}
