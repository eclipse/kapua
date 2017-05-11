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
import org.eclipse.kapua.commons.core.InterceptorBind;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

public class KapuaModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(KapuaModule.class);

    /**
     * Service resource file from which the managed services are read
     */
    private static final String SERVICE_RESOURCE = "locator.xml";

    private LocatorConfig locatorConfig;
    
    public KapuaModule(LocatorConfig locatorConfig) {
        this.locatorConfig = locatorConfig;
    }
    
    @Override
    protected void configure() {
        try {
            ClassLoader classLoader = locatorConfig.getClassLoader();
            boolean initialize = true;
            
            // Packages are supposed to contain service implementations
            Set<Class<?>> providers = locatorConfig.getProvidersInfo();

            // Provided names are the objects provided by the module (services or factories
            Collection<String> providedInterfaceNames = locatorConfig.getProvidedInterfaceNames();
            
            logger.info("Binding interceptors ..");
            
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
                            @SuppressWarnings("unchecked")
                            ServiceResolver<KapuaService, ?> resolver = ServiceResolver.newInstance(kapuaObject, clazz);
                            bind(resolver.getServiceClass()).to(resolver.getImplementationClass()).in(Singleton.class);
                            logger.info("Bind Kapua service {} to {}", kapuaObject, clazz);
                            isClassBound = true;
                            break;
                        }
                    }

                    if (isClassBound) {
                        continue;
                    }
                }

                // When the provided object is a factory ...
                // ... add binding with a matching implementation
                if (KapuaObjectFactory.class.isAssignableFrom(kapuaObject)) {
                    for (Class<?> clazz : providers) {
                        if (kapuaObject.isAssignableFrom(clazz)) {
                            @SuppressWarnings("unchecked")
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
