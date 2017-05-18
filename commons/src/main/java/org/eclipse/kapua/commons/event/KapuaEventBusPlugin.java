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
package org.eclipse.kapua.commons.event;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.AbstractKapuaPlugin;
import org.eclipse.kapua.commons.core.KapuaPlugin;
import org.eclipse.kapua.commons.core.LifecycleHandler;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.eclipse.kapua.locator.inject.PoolListener;
import org.eclipse.kapua.service.event.KapuaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaEventBusPlugin extends AbstractKapuaPlugin implements KapuaPlugin, PoolListener {

    private final static Logger logger = LoggerFactory.getLogger(KapuaEventBusPlugin.class);

    @Inject ManagedObjectPool managedObjects;
    
    private List<EventBusConnector> eventConnectors = new ArrayList<EventBusConnector>();
    
    @Inject public KapuaEventBusPlugin(LifecycleHandler lifecycleHandler) {
        super(lifecycleHandler);
    }
    
    @Override
    public void start() {
        logger.info("Starting...");
        
        // After a container stop/start sequence the pool may be already populated with objects
        // The event listeners in the pool have to be collected and plugged into an EventConnector
        List<KapuaEventListener> eventListeners = managedObjects.getImplementationsOf(KapuaEventListener.class);
        for (KapuaEventListener listener:eventListeners) {
            onObjectAdded(listener);
        }

        // The plugin wants to be notified when a new object is added to the pool.
        // If the object is an event listener it will then manage to plug it into
        // an event bus connector.
        managedObjects.register(this);
    }

    @Override
    public void stop() {
        logger.info("Stopping...");
        
        for(EventBusConnector connector:eventConnectors) {
            connector.stop();
        }
        
        eventConnectors.clear();
        managedObjects.deregister(this);
    }

    @Override
    public void onObjectAdded(Object object) {
        if (KapuaEventListener.class.isAssignableFrom(object.getClass())) {
            logger.info("Adding new event listener {} ...", object.getClass().getName());
            EventBusConnector connector = new EventBusConnector((KapuaEventListener) object);
            eventConnectors.add(connector);
            connector.start();
        }
    }
}
