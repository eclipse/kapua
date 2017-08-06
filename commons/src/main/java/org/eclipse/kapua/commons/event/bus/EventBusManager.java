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
package org.eclipse.kapua.commons.event.bus;

import org.eclipse.kapua.commons.event.bus.jms.JMSEventBus;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventBusException;
import org.eclipse.kapua.service.event.KapuaEventBusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBusManager implements KapuaEventBus {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventBusManager.class);

    private static EventBusManager instance;
    private static Throwable initFailureCause;

    private JMSEventBus jmsEventbus;

    static {
        try {
            instance = new EventBusManager();
        } catch (Throwable e) {
            LOGGER.error("Error while initializing EventbusProvider", e);
            instance = null;
            initFailureCause = e;
        }
    }

    private EventBusManager() throws KapuaEventBusException {
        jmsEventbus = new JMSEventBus();
    }

    public static EventBusManager getInstance() throws KapuaEventBusException {
        if (initFailureCause != null) {
            throw new KapuaEventBusException(initFailureCause);
        }
        return instance;
    }

    @Override
    public void publish(String address, KapuaEvent event) throws KapuaEventBusException {
        jmsEventbus.publish(address, event);
    }

    @Override
    public void subscribe(String address, KapuaEventBusListener eventListener) throws KapuaEventBusException {
        jmsEventbus.subscribe(address, eventListener);
    }

    public void start() throws KapuaEventBusException {
        jmsEventbus.start();
    }

    public void stop() throws KapuaEventBusException {
        jmsEventbus.stop();
    }
}
