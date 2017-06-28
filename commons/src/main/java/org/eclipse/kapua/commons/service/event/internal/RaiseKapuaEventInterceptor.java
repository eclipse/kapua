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
package org.eclipse.kapua.commons.service.event.internal;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.commons.event.EventBus;
import org.eclipse.kapua.commons.event.EventContextScope;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.inject.Interceptor;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.event.ActionPerformedOn;
import org.eclipse.kapua.service.event.RaiseKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event interceptor
 * 
 * @since 1.0
 */
@KapuaProvider
@Interceptor(matchSubclassOf=KapuaService.class, matchAnnotatedWith=RaiseKapuaEvent.class)
public class RaiseKapuaEventInterceptor implements MethodInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(RaiseKapuaEventInterceptor.class);

    @Inject EventContextScope eventCtxScope;
    @Inject EventBus eventBus;
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        Object returnObject = null;
        Throwable executionThrowable = null;

        try {
            eventCtxScope.begin();
            String contextId = eventCtxScope.get();

            // cannot be null since "matchAnnotatedWith=RaiseWorkflowEvents.class"
            RaiseKapuaEvent raiseEventAnnotation = invocation.getMethod().getAnnotation(RaiseKapuaEvent.class);
            logger.info("***** Intercepted method before execution {}.{}(...) - performed on: {}",
                    new Object[] { invocation.getMethod().getDeclaringClass(), invocation.getMethod().getName(), raiseEventAnnotation.eventActionPerformedOn() });

            if (ActionPerformedOn.BEFORE.equals(raiseEventAnnotation.eventActionPerformedOn())) {
                logger.debug("Send event before method call");
                sendEvent(contextId, returnObject, invocation);
            }

            try {
                returnObject = invocation.proceed();
            } catch (Throwable t) {
                executionThrowable = t;
            }

            if (ActionPerformedOn.AFTER.equals(raiseEventAnnotation.eventActionPerformedOn())) {
                logger.debug("Send event after method call");
                sendEvent(contextId, returnObject, invocation);
            } else if (ActionPerformedOn.AFTER_IF_FAIL.equals(raiseEventAnnotation.eventActionPerformedOn()) && executionThrowable != null) {
                logger.debug("Send event after method call failure");
                sendEvent(contextId, returnObject, invocation);
            } else if (ActionPerformedOn.AFTER_IF_SUCCESS.equals(raiseEventAnnotation.eventActionPerformedOn()) && executionThrowable == null) {
                logger.debug("Send event after method call success");
                sendEvent(contextId, returnObject, invocation);
            }

            logger.info("***** Intercepted method after {} execution {}.{}(...)",
                    (executionThrowable != null ? String.format("failed (with exception) [%s]", executionThrowable.getMessage()) : "successful"),
                    invocation.getMethod().getDeclaringClass(),
                    invocation.getMethod().getName());

            if (executionThrowable != null) {
                throw executionThrowable;
            }
            
            return returnObject;
            
        } finally {
            eventCtxScope.end();
        }
    }

    private void sendEvent(String contextId, Object returnedValue, MethodInvocation invocation) {
        
        // TODO 
        // Improve the retrieval of the info for event
        //
        
        KapuaEventImpl event = new KapuaEventImpl();
        event.setContextId(contextId);
        eventBus.publish(event);
    }

}
