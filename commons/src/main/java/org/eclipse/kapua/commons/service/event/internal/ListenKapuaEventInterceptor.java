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
package org.eclipse.kapua.commons.service.event.internal;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.event.EventContextImpl;
import org.eclipse.kapua.commons.event.EventContextService;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.inject.Interceptor;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.ListenKapuaEvent;

@KapuaProvider
@Interceptor(matchAnnotatedWith = ListenKapuaEvent.class, matchSubclassOf = KapuaService.class)
public class ListenKapuaEventInterceptor implements MethodInterceptor {

    @Inject private EventContextService eventContextService;
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        try {
            Object[] args = invocation.getArguments();
            if (args.length != 1) {
                throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, "Single argument expected.");
            }
            if (!KapuaEvent.class.isAssignableFrom(args[0].getClass())) {
                throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, "KapuaEvent expected, found " + args[0].getClass().getName());
            }
            
            if (!eventContextService.isInProgress()) {
                KapuaEvent kapuaEvent = (KapuaEvent) args[0];
                String contextId = kapuaEvent.getContextId();
                EventContextImpl eventCtx = EventContextImpl.fromId(contextId);
                eventContextService.begin(eventCtx);
            } else {
                eventContextService.begin();
            }
            return invocation.proceed();
        } finally {
            eventContextService.end();
        }
    }

}
