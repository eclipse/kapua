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
package org.eclipse.kapua.event;

/**
 * Service event bus definition.
 * 
 * @since 1.0
 */
public interface ServiceEventBus {

    /**
     * Publish the event to the bus
     * 
     * @param address
     *            address in which to publish the event
     * @param event
     *            event to publish
     * @throws ServiceEventBusException
     */
    public void publish(String address, ServiceEvent event) throws ServiceEventBusException;

    /**
     * Subscribe for a specific address event
     * 
     * @param address
     *            address to listen for events
     * @param name
     *            subscriber name. It's used to share events between multiple instances of the same consumer.
     * @param eventListener
     *            listener to invoke when an event is received
     * @throws ServiceEventBusException
     */
    public void subscribe(String address, String name, ServiceEventBusListener eventListener) throws ServiceEventBusException;
}