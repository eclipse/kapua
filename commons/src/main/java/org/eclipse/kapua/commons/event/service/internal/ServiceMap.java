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
package org.eclipse.kapua.commons.event.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMap.class);

    //no need to have a concurrent map since:
    //if a service is not available (due to register process in progress) no event is sent
    //if a service is going to be deregistered may some event is still fired and will remain in the queue until the service consuming these events will come available later
    private final static Map<String, String> AVAILABLE_SERVICES = new HashMap<>();

    private ServiceMap() {
    }

    public static synchronized void registerServices(String serviceQueueAddress, List<String> servicesNames) {
        for (String serviceName : servicesNames) {
            //register service name
            String tmpServiceName = AVAILABLE_SERVICES.get(serviceName);
            if (tmpServiceName==null) {
                AVAILABLE_SERVICES.put(serviceName, serviceQueueAddress);
                LOGGER.info("Bound service '{}' to queue address '{}'",
                        new Object[]{serviceName, serviceQueueAddress});
            }
            else if (!serviceQueueAddress.equals(tmpServiceName)) {
                LOGGER.warn("The service '{}' is already registered with a different queue address (old '{}' - new '{}'). No change will be made",
                        new Object[]{serviceName, tmpServiceName, serviceQueueAddress});
            }
            else {
                LOGGER.info("The service '{}' is already registered with queue address '{}'",
                        new Object[]{serviceName, serviceQueueAddress});
            }
        }
    }

    public static synchronized void unregisterServices(List<String> servicesNames) {
        for (String serviceName : servicesNames) {
            String tmpServiceName = AVAILABLE_SERVICES.remove(serviceName);
            if (tmpServiceName==null) {
                LOGGER.warn("Cannot deregister service '{}'. The service wasn't registered!", serviceName);
            }
            else {
                LOGGER.info("Deregistered service '{}' from queue address '{}'",
                        new Object[]{serviceName, tmpServiceName});
            }
        }
    }

    public static String getQueueAddress(String serviceName) {
        return AVAILABLE_SERVICES.get(serviceName);
    }

}