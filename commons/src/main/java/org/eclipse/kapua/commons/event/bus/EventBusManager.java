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

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.event.bus.jms.JMSEventBus;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBusManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventBusManager.class);

    private final static JMSEventBus JMS_EVENT_BUS;
    private static boolean started;

    static {
        try {
            JMS_EVENT_BUS = new JMSEventBus();
        } catch (Throwable t) {
            LOGGER.error("Error while initializing EventbusProvider", t);
            throw KapuaRuntimeException.internalError(t, "Cannot initialize event bus manager");
        }
    }

    private EventBusManager() {

    }

    public static KapuaEventBus getInstance() throws KapuaEventBusException {
        if (!started) {
            throw new KapuaEventBusException("The event bus isn't initialized! Cannot perform any operation!");
        }
        return JMS_EVENT_BUS;
    }

    public static void start() throws KapuaEventBusException {
        JMS_EVENT_BUS.start();
        started = true;
    }

    public static void stop() throws KapuaEventBusException {
        JMS_EVENT_BUS.stop();
        started = false;
    }

}
