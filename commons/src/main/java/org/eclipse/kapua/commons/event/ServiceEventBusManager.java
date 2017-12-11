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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.event.jms.JMSServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service event bus manager. It handles the service event bus life cycle
 * 
 * @since 1.0
 *
 */
public class ServiceEventBusManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceEventBusManager.class);

    private final static JMSServiceEventBus JMS_EVENT_BUS;
    private static boolean started;

    static {
        try {
            JMS_EVENT_BUS = new JMSServiceEventBus();
        } catch (Throwable t) {
            LOGGER.error("Error while initializing EventbusProvider", t);
            throw KapuaRuntimeException.internalError(t, "Cannot initialize event bus manager");
        }
    }

    private ServiceEventBusManager() {
    }

    /**
     * Get the event bus instance
     * 
     * @return
     * @throws ServiceEventBusException
     */
    public static ServiceEventBus getInstance() throws ServiceEventBusException {
        if (!started) {
            throw new ServiceEventBusException("The event bus isn't initialized! Cannot perform any operation!");
        }
        return JMS_EVENT_BUS;
    }

    /**
     * Start the event bus
     * 
     * @throws ServiceEventBusException
     */
    public static void start() throws ServiceEventBusException {
        JMS_EVENT_BUS.start();
        started = true;
    }

    /**
     * Stop the event bus
     * 
     * @throws ServiceEventBusException
     */
    public static void stop() throws ServiceEventBusException {
        JMS_EVENT_BUS.stop();
        started = false;
    }

}
