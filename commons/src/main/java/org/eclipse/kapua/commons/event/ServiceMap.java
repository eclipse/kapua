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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class used to handle the mapping between services and address used to republish events.
 *
 * @since 1.0
 */
public class ServiceMap {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMap.class);

    //no need to have a concurrent map since:
    //if a service is not available (due to register process in progress) no event is sent
    //if a service is going to be deregistered may some event is still fired and will remain in the queue until the service consuming these events will come available later
    private static final Map<String, String> AVAILABLE_SERVICES = new HashMap<>();

    private ServiceMap() {
    }

    /**
     * Register the list of services to the provided address
     *
     * @param serviceDefaultAddress
     * @param servicesEntryList
     */
    public static synchronized void registerServices(String serviceDefaultAddress, List<ServiceEntry> servicesEntryList) {
        for (ServiceEntry serviceEntry : servicesEntryList) {
            //register service name
            String tmpServiceName = AVAILABLE_SERVICES.get(serviceEntry.getServiceName());
            if (tmpServiceName == null) {
                AVAILABLE_SERVICES.put(serviceEntry.getServiceName(), serviceEntry.getAddress());
                LOG.info("Bound service '{}' to address '{}'", serviceEntry.getServiceName(), serviceEntry.getAddress());
            } else if (!serviceEntry.getAddress().equals(tmpServiceName)) {
                LOG.warn("The service '{}' is already registered with a different address (old '{}' - new '{}'). No change will be made", serviceEntry.getServiceName(), tmpServiceName, serviceEntry.getAddress());
            } else {
                LOG.info("The service '{}' is already registered with address '{}'", serviceEntry.getServiceName(), serviceEntry.getAddress());
            }
        }
    }

    /**
     * Unregister the provided services from the address map
     *
     * @param servicesNames
     */
    public static synchronized void unregisterServices(List<String> servicesNames) {
        if (servicesNames != null) {
            for (String serviceName : servicesNames) {
                String tmpServiceName = AVAILABLE_SERVICES.remove(serviceName);
                if (tmpServiceName == null) {
                    LOG.warn("Cannot deregister service '{}'. The service wasn't registered!", serviceName);
                } else {
                    LOG.info("Deregistered service '{}' from address '{}'", serviceName, tmpServiceName);
                }
            }
        }
    }

    /**
     * Get the address associated to the specific service
     *
     * @param serviceName
     * @return
     */
    public static String getAddress(String serviceName) {
        return AVAILABLE_SERVICES.get(serviceName);
    }

}
