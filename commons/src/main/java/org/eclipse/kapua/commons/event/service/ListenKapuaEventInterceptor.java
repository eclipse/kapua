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


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.event.ListenKapuaEvent;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
@InterceptorBind(matchSubclassOf = KapuaService.class, matchAnnotatedWith = ListenKapuaEvent.class)
public class ListenKapuaEventInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenKapuaEventInterceptor.class);

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

            return invocation.proceed();
        } finally {
        }
    }

    public void onInternalEvent(KapuaEvent event) {
        if (event.getOperation().equals("check-unprocessed")) {
            LOGGER.info(String.format("Received upstream event %s - %s - %s", event.getService(), event.getOperation(), event.getEntityType()));
        } else {
            LOGGER.info(String.format("Marked event %s as processed", event.getContextId()));
        }
    }

}