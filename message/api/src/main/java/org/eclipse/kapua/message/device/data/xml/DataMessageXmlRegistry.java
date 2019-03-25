/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.device.data.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class DataMessageXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaDataMessageFactory KAPUA_DATA_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaDataMessageFactory.class);

    public KapuaDataMessage newKapuaDataMessage() {
        return KAPUA_DATA_MESSAGE_FACTORY.newKapuaDataMessage();
    }

    public KapuaDataChannel newKapuaDataChannel() {
        return KAPUA_DATA_MESSAGE_FACTORY.newKapuaDataChannel();
    }

    public KapuaDataPayload newKapuaDataPayload() {
        return KAPUA_DATA_MESSAGE_FACTORY.newKapuaDataPayload();
    }

}
