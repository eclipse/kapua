/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.core;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.locator.KapuaLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ServiceModuleBundle {

    private static final Logger logger = LoggerFactory.getLogger(ServiceModuleBundle.class);

    public ServiceModuleBundle() {
        // Initialize the kapua locator
        KapuaLocator.getInstance();
    }

    public final void startup() throws KapuaException {
        logger.info("Starting up...");

        logger.info("Startup Kapua Eventbus...");
        ServiceEventBusManager.start();

        logger.info("Startup Kapua Service Modules...");
        Set<ServiceModule> serviceModules = ServiceModuleConfiguration.getServiceModules();
        for (ServiceModule service : serviceModules) {
            service.start();
        }

        logger.info("Startup...DONE");
    }

    public final void shutdown() throws KapuaException {
        logger.info("Shutting down...");

        logger.info("Shutdown Kapua Service Modules...");
        Set<ServiceModule> serviceModules = ServiceModuleConfiguration.getServiceModules();
        for (ServiceModule service : serviceModules) {
            service.stop();
        }

        logger.info("Shutdown Kapua Eventbus...");
        ServiceEventBusManager.stop();

        logger.info("Shutdown...DONE");
    }

}
