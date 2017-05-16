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
package org.eclipse.kapua.commons.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
@InterceptorBind(matchSublclassOf=KapuaService.class, matchAnnotatedWith=RaiseWorkflowEvents.class)
public class RaiseLogEntry implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RaiseLogEntry.class);

    public RaiseLogEntry() {
        logger.info("***** Interceptor {} created !!", RaiseLogEntry.class.getName());        
    }
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        logger.info("***** Intercepted method before execution {}.{}(...)", invocation.getMethod().getDeclaringClass(), invocation.getMethod().getName());

        Object obj = null;
        Throwable throwable = null;
        try {
            obj = invocation.proceed();
        } catch (Throwable t) {
            throwable = t;
        }        

        String result = "succesful";
        if (throwable!=null) {
            result = "failed (with throw)";
        }
        
        logger.info("***** Intercepted method after {} execution {}.{}(...)", 
                result,
                invocation.getMethod().getDeclaringClass(), 
                invocation.getMethod().getName());

        if (throwable!=null) {
           throw throwable;
        }

        return obj;
    }
}
