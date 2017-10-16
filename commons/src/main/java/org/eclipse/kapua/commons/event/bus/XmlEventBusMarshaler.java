/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.bus;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventBusException;

public class XmlEventBusMarshaler extends EventBusMarshaler {

    @Override
    public void marshal(TextMessage textMessage, KapuaEvent kapuaEvent) throws KapuaEventBusException {
        try {
            textMessage.setText(XmlUtil.marshal(kapuaEvent));
            textMessage.setStringProperty(CONTENT_TYPE_KEY, CONTENT_TYPE_XML);
        } catch (JAXBException | JMSException e) {
            throw new KapuaEventBusException(e);
        }
    }

}
