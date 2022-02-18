/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;

/**
 * XML {@link ServiceEventMarshaler} implementation.
 *
 * @since 1.0.0
 */
public class XmlServiceEventMarshaler implements ServiceEventMarshaler {

    public static final String CONTENT_TYPE_XML = "application/xml";

    @Override
    public String getContentType() {
        return CONTENT_TYPE_XML;
    }

    @Override
    public ServiceEvent unmarshal(String message) throws KapuaException {
        try {
            return XmlUtil.unmarshal(message, ServiceEvent.class);
        } catch (JAXBException | SAXException e) {
            throw new ServiceEventBusException(e);
        }
    }

    @Override
    public String marshal(ServiceEvent kapuaEvent) throws ServiceEventBusException {
        try {
            return XmlUtil.marshal(kapuaEvent);
        } catch (JAXBException e) {
            throw new ServiceEventBusException(e);
        }
    }

}
