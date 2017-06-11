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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.inject.SyntheticMethodMatcher;
import org.eclipse.kapua.locator.inject.InterceptorBind;
import org.eclipse.kapua.locator.inject.LocatorConfig;
import org.eclipse.kapua.locator.inject.PoolListener;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class KapuaModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(KapuaModule.class);

    /**
     * Service resource file from which the managed services are read
     */
    private static final String SERVICE_RESOURCE = "locator.xml";

    private LocatorConfig locatorConfig;
    private PoolListener injectorListener;
    
    public KapuaModule(LocatorConfig locatorConfig) {
        this.locatorConfig = locatorConfig;
    }
    
    public void setInjectorListener(PoolListener injectorListener) {
        this.injectorListener = injectorListener;
    }
    
    private PoolListener getInjectorListener() {
        return this.injectorListener;
    }
   
    @Override
    protected void configure() {
        try {
            ClassLoader classLoader = locatorConfig.getClassLoader();
            boolean initialize = true;
            
            // Packages are supposed to contain service implementations
            Set<Class<?>> providers = locatorConfig.getAnnotatedWith(KapuaProvider.class);

            // Provided names are the objects provided by the module (services or factories
            Collection<String> providedInterfaceNames = locatorConfig.getProvidedInterfaceNames();
            
            logger.info("Binding service apis and factories...");
            for (String providedName : providedInterfaceNames) {

                boolean isClassBound = false;

                final String trimmedServiceLine = providedName.trim();
                Class<?> kapuaObject = Class.forName(trimmedServiceLine, !initialize, classLoader);

                // When the provided object is a service ...
                // ... add binding with a matching implementation
                if (KapuaService.class.isAssignableFrom(kapuaObject)) {
                    for (Class<?> clazz : providers) {
                        if (kapuaObject.isAssignableFrom(clazz)) {
                            ServiceResolver<KapuaService, ?> resolver = ServiceResolver.newInstance(kapuaObject, clazz);
                            bind(resolver.getServiceClass()).to(resolver.getImplementationClass()).in(Singleton.class);
                            
                            // This further EXPLICIT bind is necessary to let the service implementation be visible to 
                            // Guice when an interceptor binding has to be applied later on at runtime.
                            bind(clazz).in(Singleton.class);
                            //
                            //////
                            
                            logger.info("Bind Kapua service {} to {}", kapuaObject, clazz);
                            isClassBound = true;
                            break;
                        }
                    }

                    if (isClassBound) {
                        continue;
                    }
                }
                
                logger.info("Binding interceptors ..");

                // When the provided object is a factory ...
                // ... add binding with a matching implementation
                if (KapuaObjectFactory.class.isAssignableFrom(kapuaObject)) {
                    for (Class<?> clazz : providers) {
                        if (kapuaObject.isAssignableFrom(clazz)) {
                            FactoryResolver<KapuaObjectFactory, ?> resolver = FactoryResolver.newInstance(kapuaObject, clazz);
                            bind(resolver.getFactoryClass()).to(resolver.getImplementationClass()).in(Singleton.class);
                            logger.info("Bind Kapua factory {} to {}", kapuaObject, clazz);
                            isClassBound = true;
                            break;
                        }
                    }

                    if (isClassBound) {
                        continue;
                    }
                }

                logger.warn("No provider found for {}", kapuaObject);
            }
            
            // Bind interceptors
            for (Class<?> clazz : providers) {
                if (MethodInterceptor.class.isAssignableFrom(clazz)) {
                    InterceptorBind annotation = clazz.getAnnotation(InterceptorBind.class);
                    Class<?> parentClazz = annotation.matchSublclassOf();
                    Class<? extends Annotation> methodAnnotation = annotation.matchAnnotatedWith();
                    bindInterceptor(Matchers.subclassesOf(parentClazz), Matchers.annotatedWith(methodAnnotation), (MethodInterceptor) clazz.newInstance());
                    logger.info("Bind service interceptor {} to subclasses of {} annotated with {}", clazz, parentClazz, methodAnnotation);
                }
            }
            
            
            logger.info("Binding lifecycle listeners ..");
            final KapuaModule thisModule = this;
            // When an implementation of a service is created the listener is invoked
            if (this.getInjectorListener() != null) {
                this.bindListener(Matchers.any(), new TypeListener() {
                    
                    @Override
                    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                        typeEncounter.register(new InjectionListener<I>() {

                            @Override
                            public void afterInjection(Object i) {
                                thisModule.getInjectorListener().onObjectAdded(i);
                            }
                        });
                    }
                });
            }
            
            logger.trace("Binding completed");

        } catch (Exception e) {
            logger.error("Exeption configuring module", e);
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + SERVICE_RESOURCE);
        }
    }
    
    @Override
    protected void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
        super.bindInterceptor(classMatcher, Matchers.not(SyntheticMethodMatcher.getInstance()).and(methodMatcher), interceptors);
    }
}
