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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.kapua.commons.locator.ComponentLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kpaua container hosts a Kapua application and provides the means to configure it
 * and manage its lifecycle.
 * 
 * @since 1.0
 */
public abstract class KapuaContainer {

    private final static Logger logger = LoggerFactory.getLogger(KapuaContainer.class);

    private KapuaConfiguration configuration;
    
    public KapuaContainer() {
        this(new KapuaConfiguration());
    }

    public KapuaContainer(KapuaConfiguration configuration) {
        this.configuration = configuration;
        ComponentLocator.getInstance();
    }

    public final void startup() {
        logger.info("Startup...");
        Set<LifecycleComponent> services = ComponentLocator.getInstance().getServiceComponents();
        for(LifecycleComponent service:services) {
            service.start();
        }
        
        List<LifecyleListener> listeners = configuration.getImplementationsOf(LifecyleListener.class);
        for(LifecyleListener listener:listeners) {
            listener.onStartup();
        }
        logger.info("Startup...DONE");
    }

    public final void shutdown() {
        logger.info("Shutdown...");
        List<LifecyleListener> listeners = configuration.getImplementationsOf(LifecyleListener.class);
        for(int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).onShutdown();
        }
        
        ArrayList<LifecycleComponent> services = new ArrayList<>(ComponentLocator.getInstance().getServiceComponents());
        for(int i = services.size() - 1; i >= 0; i--) {
            services.get(i).stop();
        }
        logger.info("Shutdown...DONEs");
    }
}
