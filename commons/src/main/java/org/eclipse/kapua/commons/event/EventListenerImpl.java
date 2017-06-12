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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListener;
import org.eclipse.kapua.service.event.ListenKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventListenerImpl implements EventListener {

    private final static Logger logger = LoggerFactory.getLogger(EventListenerImpl.class);

    private KapuaEventListener kapuaEventListner;
    private List<Method> listenerMethods = new ArrayList<>();
    
    private EventListenerImpl(KapuaEventListener kapuaEventListner) {
        this.kapuaEventListner = kapuaEventListner;
    }
    
    public static EventListenerImpl newInstance(KapuaEventListener kapuaEventListner) {
        EventListenerImpl eventListener = new EventListenerImpl(kapuaEventListner);
        Method[] methods = kapuaEventListner.getClass().getMethods();
        for(Method method:methods) {
            ListenKapuaEvent[] listenEnnotations = method.getAnnotationsByType(ListenKapuaEvent.class);
            if(listenEnnotations != null && listenEnnotations.length > 0) {
                eventListener.listenerMethods.add(method);
            }
        }
        return eventListener;
    }
    
    @Override
    public void onKapuaEvent(KapuaEvent event) {
        for(Method method:listenerMethods) {
            try {
                method.invoke(kapuaEventListner, event);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error("Unable to process KapuaEvent", e);
            }
        }
    }

}
