/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.bus.ServiceEventBusManager;
import org.eclipse.kapua.locator.KapuaLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaApplication {

    private final static Logger logger = LoggerFactory.getLogger(KapuaApplication.class);

    public KapuaApplication() {
        // Initialize the kapua locator
        KapuaLocator.getInstance();
    }

    public final void startup() throws KapuaException {
        logger.info("Starting up...");

        logger.info("Startup Kapua Eventbus...");
        ServiceEventBusManager.start();

        logger.info("Startup Kapua Service Modules...");
        ServiceModuleProvider moduleProvider = ServiceModuleLocator.getModuleProvider();
        if (moduleProvider == null) {
            throw KapuaException.internalError("Unable to retrieve KapuaModuleProvider");
        }

        for (ServiceModule service : moduleProvider.getModules()) {
            service.start();
        }

        logger.info("Startup...DONE");
    }

    public final void shutdown() throws KapuaException {
        logger.info("Shutting down...");

        logger.info("Shutdown Kapua Service Modules...");
        ServiceModuleProvider moduleProvider = ServiceModuleLocator.getModuleProvider();
        if (moduleProvider == null) {
            throw KapuaException.internalError("Unable to retrieve KapuaModuleProvider");
        }

        for (ServiceModule service : moduleProvider.getModules()) {
            service.stop();
        }

        logger.info("Shutdown Kapua Eventbus...");
        ServiceEventBusManager.stop();

        logger.info("Shutdown...DONE");
    }

}
