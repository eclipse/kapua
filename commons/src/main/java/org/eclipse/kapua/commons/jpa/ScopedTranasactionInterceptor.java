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
package org.eclipse.kapua.commons.jpa;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.inject.Interceptor;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
@Interceptor(matchAnnotatedWith = Transactional.class, matchSubclassOf = KapuaService.class)
public class ScopedTranasactionInterceptor implements MethodInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(ScopedTranasactionInterceptor.class);
    
    @Inject private EntityManagerFactoryRegistry emfRegistry;
    @Inject private ScopedTransactionService persistenceService;
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        Transactional transactionAnnotation = invocation.getMethod().getAnnotation(Transactional.class);
        Class<? extends EntityManagerFactory> factoryClass = transactionAnnotation.factory();
        EntityManagerFactory factory = emfRegistry.getFactory(factoryClass);

        Object obj = null;
        try {
            
            persistenceService.begin(factory);
            
            // If the invocation is inside an "externally" started transaction scope,
            // forward the invocation and delegate the external transaction scope the
            // responsibility to manage possible exceptions thrown
            if (persistenceService.get().isTransactionActive()) {
                obj = invocation.proceed();
                return obj;
            }
            
            // Start a new transaction scope and hence manage possible exceptions
            // thrown by this invocation as well as nested invocations.
            persistenceService.get().beginTransaction();
            invocation.proceed();
            persistenceService.get().commit();
        } catch (Throwable t) {
            transactionAnnotation = invocation.getMethod().getAnnotation(Transactional.class);
            Class<?>[] exceptions = transactionAnnotation.rollbackOn();
            for(Class<?> exception:exceptions) {
                if (exception.isAssignableFrom(t.getClass())) {
                    persistenceService.get().rollback();
                    break;
                }
            }
        } finally {
            persistenceService.end();
        }
        
        return obj;
    }

}
