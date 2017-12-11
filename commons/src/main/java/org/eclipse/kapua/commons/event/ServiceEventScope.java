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
package org.eclipse.kapua.commons.event;

import java.util.Stack;
import java.util.UUID;

import org.eclipse.kapua.event.ServiceEvent;

/**
 * Utility class to handle the thread context event stack.
 * 
 * @since 1.0
 *
 */
public class ServiceEventScope {

    private static ThreadLocal<Stack<ServiceEvent>> eventContextThdLocal = new ThreadLocal<>();

    private ServiceEventScope() {
    }

    /**
     * Append the Kapua event to the current thread context Kapua event stack (setting a new context id in the Kapua event)
     * 
     * @return
     */
    public static ServiceEvent begin() {

        // Is it the first call in the stack? Is there already a Stack?
        Stack<ServiceEvent> eventStack = eventContextThdLocal.get();
        if (eventStack == null) {
            eventStack = new Stack<>();
            eventContextThdLocal.set(eventStack);
        }

        // Is it the first call in the stack?
        String contextId = null;
        if (!eventStack.empty()) {
            ServiceEvent lastEvent = eventStack.peek();
            contextId = lastEvent.getContextId();
        } else {
            contextId = UUID.randomUUID().toString();
        }

        ServiceEvent newEvent = new ServiceEvent();
        newEvent.setContextId(contextId);
        eventStack.push(newEvent);
        return eventStack.peek();
    }

    /**
     * Create a new thread context Kapua event stack and set the Kapua event at the top
     * 
     * @param event
     */
    public static void set(ServiceEvent event) {
        Stack<ServiceEvent> eventStack = new Stack<>();
        eventStack.push(event);
        eventContextThdLocal.set(eventStack);
    }

    /**
     * Get the current Kapua event from the thread context Kapua event stack
     * 
     * @return
     */
    public static ServiceEvent get() {
        Stack<ServiceEvent> tmp = eventContextThdLocal.get();
        if (tmp != null && !tmp.empty()) {
            return tmp.peek();
        }
        return null;
    }

    /**
     * Clean up the current thread context Kapua event stack
     */
    public static void end() {
        Stack<ServiceEvent> eventStack = eventContextThdLocal.get();
        if (eventStack != null && !eventStack.empty()) {
            eventContextThdLocal.set(null);
        }

        eventStack.pop();
    }
}