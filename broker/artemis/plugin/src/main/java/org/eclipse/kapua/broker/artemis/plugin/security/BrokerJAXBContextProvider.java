/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.security;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;

/**
 * @deprecated since 2.1.0 - rely on autodiscovery. Leaving this here for comparison
 */
@Deprecated
public class BrokerJAXBContextProvider implements JAXBContextProvider {

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() {
        try {
            if (context == null) {
                Map<String, Object> properties = new HashMap<>(1);
                properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
                context = JAXBContextFactory.createContext(new Class<?>[] {
                        // KapuaEvent
                        ServiceEvent.class,
                }, properties);
            }
            return context;
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
