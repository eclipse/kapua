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
package org.eclipse.kapua.commons.event.dummy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.commons.event.EventBus;
import org.eclipse.kapua.commons.event.EventListener;
import org.eclipse.kapua.commons.event.EventListenerImpl;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.eclipse.kapua.locator.inject.ObjectInspector;
import org.eclipse.kapua.locator.inject.PoolListener;
import org.eclipse.kapua.locator.inject.Service;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListener;
import org.eclipse.kapua.service.event.ListenKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 0.3.0
  */
@ComponentProvider
@Service(provides=EventBus.class)
public class DummyEventBus implements EventBus {

    private final static Logger logger = LoggerFactory.getLogger(DummyEventBus.class);
    
    private ObjectInspector objInspector;
    private ManagedObjectPool managedObjects;
    private PoolListener poolListener;    
    private boolean isStarted;

    private Map<Class<?>, Object> availableServices;
    private Map<KapuaEventListener, EventListener> eventListeners;
    private BlockingQueue<KapuaEvent> eventQueue;
    private ExecutorService threadPool;
    private Thread mainLoop;

    @Inject public DummyEventBus(ManagedObjectPool managedObjects, ObjectInspector objInspector) {
        
        this.availableServices = new HashMap<Class<?>, Object>();
        availableServices.put(EventBus.class, this);
        
        this.objInspector = objInspector;
        this.managedObjects = managedObjects;
        
        this.isStarted = false;
        this.eventListeners = new HashMap<KapuaEventListener, EventListener>();
        this.eventQueue = new ArrayBlockingQueue<KapuaEvent>(1000); // Fixed capacity
        
        // The plugin wants to be notified when a new object is added to the pool.
        // If the object is an event listener it will then manage to plug it into
        // an event bus connector.
        @SuppressWarnings("resource")
        final DummyEventBus thisEventBus = this;
        poolListener = new PoolListener(){

            @Override
            public void onObjectAdded(Object object) {
                if (KapuaEventListener.class.isAssignableFrom(object.getClass())) {
                    thisEventBus.subscribe((KapuaEventListener) object);
                }
            }
        };
        this.managedObjects.register(poolListener);
       
    }

    @Override
    public void publish(KapuaEvent event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            logger.error("Message publish interrupted: {}", e.getMessage());
        }
    }

    @Override
    public void subscribe(KapuaEventListener eventListener) {
        logger.info("Adding new event listener {} ...", eventListener.getClass().getName());
        List<Method> listeningMethods = this.getListeningMethods(objInspector, eventListener);
        EventListener eventBusListener = new EventListenerImpl(eventListener, listeningMethods);
        eventListeners.put(eventListener, eventBusListener);
    }

    @Override
    public KapuaEventListener unsubscribe(KapuaEventListener eventListener) {
        if(eventListeners.containsKey(eventListener)) {
            eventListeners.remove(eventListener);
            return eventListener;
        }
        return null;
    }

    @Override
    public boolean isSubscribed(KapuaEventListener eventListener) {
        return eventListeners.containsKey(eventListener);
    }

    public void start() {
        logger.info("Starting...");
        
        // After a container stop/start sequence the pool may be already populated with objects
        // The event listeners in the pool have to be collected and plugged into an EventConnector
        List<KapuaEventListener> eeventListeners = managedObjects.getImplementationsOf(KapuaEventListener.class);
        for (KapuaEventListener listener:eeventListeners) {
            subscribe(listener);
        }
        
        threadPool = Executors.newFixedThreadPool(10);
        
        setStarted(true);
        
        mainLoop = new Thread(new Runnable() {

            @Override
            public void run() {
                while (isStarted()) {
                    
                    KapuaEvent event = null;
                    
                    try {
                        event = eventQueue.poll(2, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        logger.error("Interrupted while pulling from queue: {}", e.getMessage());
                    }
                    
                    if (event == null) {
                        continue;
                    }
                    
                    final KapuaEvent theEvent = event;
                    threadPool.execute(new Runnable() {

                        @Override
                        public void run() {
                            Set<KapuaEventListener> kapuaEventListeners = eventListeners.keySet();
                            for(KapuaEventListener kapuaEventListener:kapuaEventListeners) {
                                try {
                                    eventListeners.get(kapuaEventListener).onKapuaEvent(theEvent);
                                } catch (Throwable t) {
                                    logger.error("Can't notify event to {}", kapuaEventListener);
                                }
                            }
                        }
                        
                    });
                }
            }
            
        });
        
        // Uncomment this to actually start the event bus
        //mainLoop.start();
        
        logger.info("Starting...DONE");
    }

    public void stop() {
        logger.info("Stopping...");
        
        setStarted(false);
        
        try {
            mainLoop.join();
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for main loop termination: {}", e.getMessage());
        }
        
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for thread pool termination: {}", e.getMessage());
            threadPool.shutdownNow();
        }       
        
        logger.info("Stopping...DONE");
    }
    
    private boolean isStarted() {
        return isStarted;
    }

    
    private void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public List<Method> getListeningMethods(ObjectInspector objInspector, KapuaEventListener kapuaEventListner) {
        
        Method[] methods;
        boolean isEnhancedClass = objInspector.isEnhancedClass(kapuaEventListner);
        if (isEnhancedClass) {
            methods = objInspector.getSuperMethods(kapuaEventListner);
        } else {
            methods = objInspector.getMethods(kapuaEventListner);
        }

        List<Method> annotatedForListening = new ArrayList<>();
        
        for(Method method:methods) {
            ListenKapuaEvent listenEnnotation = method.getAnnotation(ListenKapuaEvent.class);
            if(listenEnnotation != null) {
                annotatedForListening.add(method);
            }
        }
        return annotatedForListening;
    }
}
