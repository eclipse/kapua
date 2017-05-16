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
package org.eclipse.kapua.commons.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.commons.core.RaiseWorkflowEvents.ACTION_PERFORMED_ON;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Workflow event interceptor
 * 
 * @since 1.0
 */
@KapuaProvider
@InterceptorBind(matchSublclassOf=KapuaService.class, matchAnnotatedWith=RaiseWorkflowEvents.class)
public class RaiseWorkflowInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RaiseWorkflowInterceptor.class);

    public RaiseWorkflowInterceptor() {
        logger.info("***** Interceptor {} created !!", RaiseWorkflowInterceptor.class.getName());
    }
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // cannot be null since "matchAnnotatedWith=RaiseWorkflowEvents.class"
        RaiseWorkflowEvents raiseWorkflowEvents = invocation.getMethod().getAnnotation(RaiseWorkflowEvents.class);
        logger.info("***** Intercepted method before execution {}.{}(...) - performed on: {}",
                new Object[] { invocation.getMethod().getDeclaringClass(), invocation.getMethod().getName(), raiseWorkflowEvents.eventActionPerformedOn() });
        
        Object returnObject = null;
        Throwable executionThrowable = null;

        if (ACTION_PERFORMED_ON.BEFORE.equals(raiseWorkflowEvents.eventActionPerformedOn())) {
            logger.debug("Send event before method call");
            sendEvent(returnObject, raiseWorkflowEvents, invocation.getArguments());
        }

        try {
            returnObject = invocation.proceed();
        } catch (Throwable t) {
            executionThrowable = t;
        }

        if (ACTION_PERFORMED_ON.AFTER.equals(raiseWorkflowEvents.eventActionPerformedOn())) {
            logger.debug("Send event after method call");
            sendEvent(returnObject, raiseWorkflowEvents, invocation.getArguments());
        } else if (ACTION_PERFORMED_ON.AFTER_IF_FAIL.equals(raiseWorkflowEvents.eventActionPerformedOn()) && executionThrowable != null) {
            logger.debug("Send event after method call failure");
            sendEvent(returnObject, raiseWorkflowEvents, invocation.getArguments());
        } else if (ACTION_PERFORMED_ON.AFTER_IF_SUCCESS.equals(raiseWorkflowEvents.eventActionPerformedOn()) && executionThrowable == null) {
            logger.debug("Send event after method call success");
            sendEvent(returnObject, raiseWorkflowEvents, invocation.getArguments());
        }

        logger.info("***** Intercepted method after {} execution {}.{}(...)", 
                (executionThrowable != null ? String.format("failed (with exception) [%s]", executionThrowable.getMessage()) : "successful"),
                invocation.getMethod().getDeclaringClass(), 
                invocation.getMethod().getName());

        if (executionThrowable != null) {
            throw executionThrowable;
        }

        return returnObject;
    }

    private void sendEvent(Object returnedValue, RaiseWorkflowEvents event, Object[] arguments) {

    }

}
