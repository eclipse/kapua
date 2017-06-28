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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 0.3.0
 */
public class EventListenerImpl implements EventListener {

    private final static Logger logger = LoggerFactory.getLogger(EventListenerImpl.class);

    private KapuaEventListener kapuaEventListner;
    private List<Method> listenerMethods = new ArrayList<>();

    public EventListenerImpl(KapuaEventListener kapuaEventListner, List<Method> methods) {
        this.kapuaEventListner = kapuaEventListner;
        listenerMethods.addAll(methods);
    }

    public void addListener(Method method) {
        listenerMethods.add(method);
    }

    @Override
    public void onKapuaEvent(KapuaEvent event) {
        for (Method method : listenerMethods) {
            try {
                KapuaSecurityUtils.doPrivileged(() -> method.invoke(kapuaEventListner, event));
            } catch (KapuaException e) {
                logger.error("Unable to process KapuaEvent", e);
            }
        }
    }

}
