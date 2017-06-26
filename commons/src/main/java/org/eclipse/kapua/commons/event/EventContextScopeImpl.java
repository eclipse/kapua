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

import java.util.UUID;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.locator.inject.Service;

@ComponentProvider
@Service(provides = { EventContext.class, EventContextScope.class })
public class EventContextScopeImpl implements EventContext, EventContextScope {

    ThreadLocal<EventContext> eventContextThdLocal = new ThreadLocal<>();
    ThreadLocal<Integer> referenceCountThdLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    public boolean isInProgress() {
        return referenceCountThdLocal.get() > 0;
    }
    
    @Override
    public void begin() {
        if (eventContextThdLocal.get() == null) {
            EventContextImpl impl = EventContextImpl.fromId(UUID.randomUUID().toString());
            eventContextThdLocal.set(impl);
        }
        referenceCountThdLocal.set(referenceCountThdLocal.get()+1);
        return;
    }
    
    @Override
    public void begin(EventContext eventCtx) throws KapuaException {
        if (eventContextThdLocal.get() == null) {
            eventContextThdLocal.set(eventCtx);
            referenceCountThdLocal.set(referenceCountThdLocal.get()+1);
            return;
        }
        throw KapuaException.internalError("");
    }

    @Override
    public String get() {
        return eventContextThdLocal.get().getId();
    }

    @Override
    public void end() {
        if (referenceCountThdLocal.get() > 0) {
            referenceCountThdLocal.set(referenceCountThdLocal.get()-1);
            if (referenceCountThdLocal.get() == 0) {
                if (eventContextThdLocal.get() != null) {
                    eventContextThdLocal.set(null);
                }
            }
        }
    }

    @Override
    public String getId() {
        return eventContextThdLocal.get().getId();
    }

}
