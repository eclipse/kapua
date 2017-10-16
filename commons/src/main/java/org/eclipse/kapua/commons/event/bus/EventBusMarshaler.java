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
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventBusException;
import org.xml.sax.SAXException;

public abstract class EventBusMarshaler {

    public final static String CONTENT_TYPE_KEY = "ContentType";
    public final static String CONTENT_TYPE_JSON = "application/json";
    public final static String CONTENT_TYPE_XML = "application/xml";

    public KapuaEvent unmarshal(TextMessage textMessage) throws KapuaException {
        try {
            String contentType = textMessage.getStringProperty(CONTENT_TYPE_KEY);
            if (CONTENT_TYPE_XML.equals(contentType)) {
                return XmlUtil.unmarshal(textMessage.getText(), KapuaEvent.class);
            }
            else if (CONTENT_TYPE_JSON.equals(contentType)) {
                return XmlUtil.unmarshalJson(textMessage.getText(), KapuaEvent.class, null);
            }
            else {
                throw new KapuaEventBusException(String.format("Unsupported content type '{}'", contentType));
            }
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | JMSException e) {
            throw new KapuaEventBusException(e);
        }
    }

    public abstract void marshal(TextMessage textMessage, KapuaEvent kapuaEvent) throws KapuaException;

}