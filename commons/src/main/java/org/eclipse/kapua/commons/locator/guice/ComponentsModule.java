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
package org.eclipse.kapua.commons.locator.guice;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.locator.inject.Interceptor;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.ManagedObjectPool;
import org.eclipse.kapua.locator.inject.MultiService;
import org.eclipse.kapua.locator.inject.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * @since 0.3.0
 *
 */
public class ComponentsModule extends PrivateModule {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsModule.class);
    
    private ManagedObjectPool managedObjectPool;
    private LocatorConfig locatorConfig;
    Map<Class<?>, Multibinder<Class<?>>> multibinders;

    public ComponentsModule(ManagedObjectPool managedObjectPool, LocatorConfig locatorConfig) {
        this.managedObjectPool = managedObjectPool;
        this.locatorConfig = locatorConfig;
    }
    
    @Override
    protected void configure() {

        try {
            multibinders = new HashMap<>();
            
            Set<Class<?>> componentProviders = locatorConfig.getAnnotatedWith(ComponentProvider.class);
            for(Class<?> componentProvider:componentProviders) {
                // Bind bundles
                MultiService multiServiceAnnotation = componentProvider.getAnnotation(MultiService.class);
                if (multiServiceAnnotation != null) {
                    Class<?> multiserviceClass = multiServiceAnnotation.provides();
                    ComponentResolver resolver = ComponentResolver.newInstance(multiserviceClass, componentProvider);
                    if (!multibinders.containsKey(resolver.getProvidedClass())) {
                        multibinders.put(resolver.getProvidedClass(), (Multibinder<Class<?>>) Multibinder.newSetBinder(binder(), resolver.getProvidedClass())); 
                    }
                    
                    if (resolver.getProvidedClass().isAssignableFrom(componentProvider)) {
                        multibinders.get(resolver.getProvidedClass()).addBinding().to(resolver.getImplementationClass());
                    }
                    continue;
                }

                // Bind interceptors
                Interceptor interceptorAnnotation = componentProvider.getAnnotation(Interceptor.class);
                if (interceptorAnnotation != null) {
                    if (MethodInterceptor.class.isAssignableFrom(componentProvider)) {
                        
                        Class<?> parentClazz = interceptorAnnotation.matchSubclassOf();
                        Class<? extends Annotation> methodAnnotation = interceptorAnnotation.matchAnnotatedWith();
                        bindInterceptor(Matchers.subclassesOf(parentClazz), Matchers.annotatedWith(methodAnnotation), (MethodInterceptor) componentProvider.newInstance());
                        logger.info("Bind service interceptor {} to subclasses of {} annotated with {}", componentProvider, parentClazz, methodAnnotation);
                        continue;
                    }
                }
                
                // Bind services
                Service serviceAnnotation = componentProvider.getAnnotation(Service.class);
                if (serviceAnnotation != null) {
                    Class<?>[] providedComponents  = serviceAnnotation.provides();
                    if (providedComponents.length <= 0) {
                        throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, String.format("Annotation %s does not provide any interface to bind to.", serviceAnnotation));
                    }

                    ComponentResolver resolver = null;
                    for (Class<?> providedComponent:providedComponents) {
                        resolver = ComponentResolver.newInstance(providedComponent, componentProvider);
                        bind(resolver.getProvidedClass()).to(resolver.getImplementationClass());
                        expose(resolver.getProvidedClass());
                    }
                    // Use the last resolver to make the implementation class a singleton scope
                    bind(resolver.getImplementationClass()).in(Singleton.class);
                }
            }

            // When a new insance object is created by the injector the  
            // following listener is invoked
            this.bindListener(Matchers.any(), new TypeListener() {
                
                @Override
                public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                    typeEncounter.register(new InjectionListener<I>() {

                        @Override
                        public void afterInjection(Object i) {
                            managedObjectPool.add(i);
                        }
                    });
                }
            });

        } catch (Exception e) {
            logger.error("Exception configuring module", e);
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + locatorConfig.getURL());
        }
    }
}
