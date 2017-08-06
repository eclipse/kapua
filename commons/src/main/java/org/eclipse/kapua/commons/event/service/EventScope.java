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
package org.eclipse.kapua.commons.event.service;

import java.util.Stack;
import java.util.UUID;

import org.eclipse.kapua.commons.event.service.internal.KapuaEventImpl;
import org.eclipse.kapua.service.event.KapuaEvent;

public class EventScope {

    private static ThreadLocal<Stack<KapuaEvent>> eventContextThdLocal = new ThreadLocal<>();

    private EventScope() {
    }

    public static KapuaEvent begin() {

        // Is it the first call in the stack? Is there already a Stack?
        Stack<KapuaEvent> eventStack = eventContextThdLocal.get();
        if (eventStack == null) {
            eventStack = new Stack<>();
            eventContextThdLocal.set(eventStack);
        }

        // Is it the first call in the stack?
        String contextId = null;
        if (!eventStack.empty()) {
            KapuaEvent lastKapuaEvent = eventStack.peek();
            contextId = lastKapuaEvent.getContextId();
        } else {
            contextId = UUID.randomUUID().toString();
        }

        KapuaEventImpl newKapuaEvent = new KapuaEventImpl();
        newKapuaEvent.setContextId(contextId);
        eventStack.push(newKapuaEvent);
        return eventStack.peek();
    }

    public static KapuaEvent get() {
        Stack<KapuaEvent> tmp = eventContextThdLocal.get();
        if (tmp != null && !tmp.empty()) {
            return tmp.peek();
        }
        return null;
    }

    public static void end() {
        Stack<KapuaEvent> eventStack = eventContextThdLocal.get();
        if (eventStack != null && !eventStack.empty()) {
            eventContextThdLocal.set(null);
        }

        eventStack.pop();
    }
}