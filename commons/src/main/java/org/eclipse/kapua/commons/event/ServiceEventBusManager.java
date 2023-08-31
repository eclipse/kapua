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

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Service event bus manager. It handles the service event bus life cycle
 *
 * @since 1.0
 */
public class ServiceEventBusManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEventBusManager.class);

    public static final String JMS_20_EVENT_BUS = "JMS_20_EVENT_BUS";

    private static Map<String, ServiceEventBusDriver> serviceEventBusDrivers;
    private static boolean started;

    static {
        try {
            LOGGER.info("Finding event bus driver instance...");
            serviceEventBusDrivers = new HashMap<>();
            ServiceLoader<ServiceEventBusDriver> eventBusLoaders = ServiceLoader.load(ServiceEventBusDriver.class);
            for (ServiceEventBusDriver eventBusDriverLoader : eventBusLoaders) {
                if (serviceEventBusDrivers.containsKey(eventBusDriverLoader.getType())) {
                    LOGGER.warn("Event bus driver instance of type {} is already loaded...SKIPPED", eventBusDriverLoader.getType());
                    continue;
                }
                serviceEventBusDrivers.put(eventBusDriverLoader.getType(), eventBusDriverLoader);
                LOGGER.info("Event bus driver instance {}...ADDED", eventBusDriverLoader.getType());
            }
            LOGGER.info("Finding event bus driver instance...DONE");
        } catch (Exception ex) {
            LOGGER.error("Error while initializing {}, {}", ServiceEventBusManager.class.getName(), ex.getMessage(), ex);
            throw KapuaRuntimeException.internalError(ex, String.format("Error while initializing %s", ServiceEventBusManager.class.getName()));
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

        // Currently hard wired to use the provided instance of JMS_20_EVENT_BUS
        // May be extended in future versions

        if (serviceEventBusDrivers.get(JMS_20_EVENT_BUS) == null) {
            throw new ServiceEventBusException(String.format("No eventbus drivers found for type %s", JMS_20_EVENT_BUS));
        }
        return serviceEventBusDrivers.get(JMS_20_EVENT_BUS).getEventBus();
    }

    /**
     * Start the event bus
     *
     * @throws ServiceEventBusException
     */
    public static void start() throws ServiceEventBusException {
        if (serviceEventBusDrivers.get(JMS_20_EVENT_BUS) != null) {
            serviceEventBusDrivers.get(JMS_20_EVENT_BUS).start();
        }
        started = true;
    }

    /**
     * Stop the event bus
     *
     * @throws ServiceEventBusException
     */
    public static void stop() throws ServiceEventBusException {
        if (serviceEventBusDrivers.get(JMS_20_EVENT_BUS) != null) {
            serviceEventBusDrivers.get(JMS_20_EVENT_BUS).stop();
        }
        started = false;
    }

}
