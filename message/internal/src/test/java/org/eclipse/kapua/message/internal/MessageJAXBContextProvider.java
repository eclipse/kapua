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
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.internal.xml.KapuaMetric;
import org.eclipse.kapua.message.internal.xml.KapuaMetricValue;
import org.eclipse.kapua.message.internal.xml.KapuaMetricsMap;
import org.eclipse.kapua.message.internal.xml.KapuaMetricsMapType;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class MessageJAXBContextProvider implements JAXBContextProvider {

    private static final Logger logger = LoggerFactory.getLogger(MessageJAXBContextProvider.class);

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        if (context == null) {
            Class<?>[] classes = new Class<?>[]{
                    KapuaMessage.class,
                    KapuaChannel.class,
                    KapuaPayload.class,
                    KapuaPosition.class,
                    KapuaMetric.class,
                    KapuaMetricValue.class,
                    KapuaMetricsMapType.class,
                    KapuaMetricsMap.class
            };
            try {
                context = JAXBContextFactory.createContext(classes, null);
            } catch (JAXBException jaxbException) {
                logger.warn("Error creating JAXBContext, tests will fail!");
            }
        }
        return context;
    }
}
