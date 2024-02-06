/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.device.data.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class DataMessageXmlRegistry {

    private final KapuaDataMessageFactory kapuaDataMessageFactory = KapuaLocator.getInstance().getFactory(KapuaDataMessageFactory.class);

    public KapuaDataMessage newKapuaDataMessage() {
        return kapuaDataMessageFactory.newKapuaDataMessage();
    }

    public KapuaDataChannel newKapuaDataChannel() {
        return kapuaDataMessageFactory.newKapuaDataChannel();
    }

    public KapuaDataPayload newKapuaDataPayload() {
        return kapuaDataMessageFactory.newKapuaDataPayload();
    }

}
