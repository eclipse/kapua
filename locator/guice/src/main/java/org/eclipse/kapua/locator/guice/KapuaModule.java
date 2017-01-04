/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class KapuaModule extends AbstractModule {
	
    private static final Logger logger = LoggerFactory.getLogger(KapuaModule.class);
	
    /**
     * Service resource file from which the managed services are read
     */
	private static final String SERVICE_RESOURCE = "locator.xml";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void configure() 
	{
		try {
			// Find locator configuration file
			List<URL> locatorConfigurations = Arrays.asList(ResourceUtils.getResource(SERVICE_RESOURCE));
			if (locatorConfigurations == null || locatorConfigurations.size() == 0)
				return;
			
			// Read configurations from resource files
			URL locatorConfigURL = locatorConfigurations.get(0);
			LocatorConfig locatorConfig = LocatorConfig.fromURL(locatorConfigURL);

			// Packages are supposed to contain service implementations
			Collection<String> packageNames = locatorConfig.getPackageNames();
			
			ClassLoader classLoader = this.getClass().getClassLoader();
			ClassPath classPath = ClassPath.from(classLoader);
			boolean initialize = true;
			
			// Among all the classes in the configured packages, retain only the ones 
			// annotated with @KapuaProvider annotation
			HashSet<Class> extendedClassInfo = new HashSet<Class>();
			for(String packageName:packageNames) {
				// Use the class loader of this (module) class
				ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packageName);
				for(ClassInfo classInfo:classInfos) {
					logger.trace("CLASS: {}", classInfo.getName());
					Class<?> theClass = Class.forName(classInfo.getName(), !initialize, classLoader);
					KapuaProvider serviceProvider = theClass.getAnnotation(KapuaProvider.class);
					if (serviceProvider != null) {
						extendedClassInfo.add(theClass);
					}
				}
			}			
			
			// Provided names are the objects provided by the module (services or factories
			Collection<String> providedInterfaceNames = locatorConfig.getProvidedInterfaceNames();
			
			String trimmedServiceLine = null;
			for (String providedName:providedInterfaceNames) {
				
				boolean isClassBound = false;

				trimmedServiceLine = providedName.trim();
				Class<?> kapuaObject = Class.forName(trimmedServiceLine, !initialize, classLoader);
				
				// When the provided object is a service ... 
				// ... add binding with a matching implementation
				if (KapuaService.class.isAssignableFrom(kapuaObject)) {
					for(Class<?> clazz:extendedClassInfo) {
						if (kapuaObject.isAssignableFrom(clazz)) {
							ServiceResolver resolver = ServiceResolver.newInstance(kapuaObject, clazz);
							bind(resolver.getServiceClass()).to(resolver.getImplementationClass()).in(Singleton.class);
							logger.info("Bind Kapua service {} to {}",kapuaObject, clazz);
							isClassBound = true;
							break;
						}
					}
					
					if (isClassBound)
						continue;
				}
				
				// When the provided object is a factory ... 
				// ... add binding with a matching implementation
				if (KapuaObjectFactory.class.isAssignableFrom(kapuaObject)) {
					for(Class clazz:extendedClassInfo) {
						if (kapuaObject.isAssignableFrom(clazz)) {
							FactoryResolver resolver = FactoryResolver.newInstance(kapuaObject, clazz);
							bind(resolver.getFactoryClass()).to(resolver.getImplementationClass()).in(Singleton.class);
							logger.info("Bind Kapua factory {} to {}",kapuaObject, clazz);
							isClassBound = true;
							break;
						}
					}
					
					if (isClassBound)
						continue;
				}
				
				logger.warn("No provider found for {}", kapuaObject);
			}
			
			logger.trace("Binding completed.");
			
		} catch (Exception e) {
            logger.error("Exeption configuring module: {}", e.getMessage(), e);
			throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, 
											"Cannot load "+SERVICE_RESOURCE, 
											e);
		}
	}

}
