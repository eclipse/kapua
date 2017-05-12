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

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.kapua.commons.core.KapuaPlugin;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class LocatorConfig {

    private static final Logger logger = LoggerFactory.getLogger(LocatorConfig.class);

    private static final String SERVICE_RESOURCE_INTERFACES = "provided.api";
    private static final String SERVICE_RESOURCE_PACKAGES = "packages.package";

    private final URL url;
    private final List<String> packageNames;
    private final List<String> providedInterfaceNames;

    private LocatorConfig(final URL url, final List<String> packageNames, final List<String> providedInterfaceNames) {
        this.url = url;
        this.packageNames = packageNames;
        this.providedInterfaceNames = providedInterfaceNames;
    }

    public static LocatorConfig fromURL(final URL url) throws KapuaLocatorException {

        if (url == null) {
            throw new IllegalArgumentException("'url' must not be null");
        }

        final List<String> packageNames = new ArrayList<>();
        final List<String> providedInterfaceNames = new ArrayList<>();

        final XMLConfiguration xmlConfig;
        try {
            xmlConfig = new XMLConfiguration(url);
        } catch (ConfigurationException e) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e);
        }

        Object props = xmlConfig.getProperty(SERVICE_RESOURCE_PACKAGES);
        if (props instanceof Collection<?>) {
            addAllStrings(packageNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            packageNames.add((String) props);
        }

        props = xmlConfig.getProperty(SERVICE_RESOURCE_INTERFACES);
        if (props instanceof Collection<?>) {
            addAllStrings(providedInterfaceNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            providedInterfaceNames.add((String) props);
        }

        return new LocatorConfig(url, Collections.unmodifiableList(packageNames), Collections.unmodifiableList(providedInterfaceNames));
    }

    private static void addAllStrings(final List<String> list, Collection<?> other) {
        for (Object entry : other) {
            if (entry instanceof String) {
                list.add((String) entry);
            }
        }
    }

    public URL getURL() {
        return url;
    }

    public Collection<String> getPackageNames() {
        return packageNames;
    }

    public Collection<String> getProvidedInterfaceNames() {
        return providedInterfaceNames;
    }
    
    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
    
    public Set<Class<?>> getProvidersInfo() throws KapuaLocatorException, IOException, ClassNotFoundException {

        // Packages are supposed to contain service implementations
        Collection<String> packageNames = this.getPackageNames();

        ClassLoader classLoader = this.getClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);
        boolean initialize = true;

        // Among all the classes in the configured packages, retain only the ones
        // annotated with @KapuaProvider annotation
        Set<Class<?>> extendedClassInfo = new HashSet<>();
        for (String packageName : packageNames) {
            // Use the class loader of this (module) class
            ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packageName);
            for (ClassInfo classInfo : classInfos) {
                logger.trace("CLASS: {}", classInfo.getName());
                Class<?> theClass = Class.forName(classInfo.getName(), !initialize, classLoader);
                KapuaProvider serviceProvider = theClass.getAnnotation(KapuaProvider.class);
                if (serviceProvider != null) {
                    extendedClassInfo.add(theClass);
                }
            }
        }

        return extendedClassInfo;
    }
    
    public Set<Class<?>> getPluginsInfo() throws KapuaLocatorException, IOException, ClassNotFoundException {

        // Packages are supposed to contain service implementations
        Collection<String> packageNames = this.getPackageNames();

        ClassLoader classLoader = this.getClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);
        boolean initialize = true;

        // Among all the classes in the configured packages, retain only the ones
        // implements the KapuaPlugin interface

        // annotated with @KapuaProvider annotation
        Set<Class<?>> extendedClassInfo = new HashSet<>();
        for (String packageName : packageNames) {
            // Use the class loader of this (module) class
            ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packageName);
            for (ClassInfo classInfo : classInfos) {
                logger.trace("CLASS: {}", classInfo.getName());
                Class<?> classToCheck = Class.forName(classInfo.getName(), !initialize, classLoader);
                // for performance reason which is the best way?
                // KapuaPlugin plugin = classToCheck.getAnnotation(KapuaPlugin.class);
                // first way
                // if (KapuaPlugin.class.isAssignableFrom(classToCheck) && !classToCheck.isInterface() && !Modifier.isAbstract(classToCheck.getModifiers()) {
                // extendedClassInfo.add(classToCheck);
                // }

                // second way
                if (isPlugin(classToCheck)) {
                    extendedClassInfo.add(classToCheck);
                }
            }
        }

        return extendedClassInfo;
    }

    /**
     * Check if the class is a {@link KapuaPlugin} representation, so if:<br>
     * <br>
     * <ul>
     * <li>it implements {@link KapuaPlugin}</li>
     * <li>it is not an interface</li>
     * <li>it is not abstract</li>
     * </ul>
     * 
     * @param clazz
     * @return
     */
    private boolean isPlugin(Class<?> clazz) {
        Class<?>[] implementedInterfaces = clazz.getInterfaces();
        if (implementedInterfaces != null && implementedInterfaces.length > 0) {
            for (Class<?> implementedInterface : implementedInterfaces) {
                if (KapuaPlugin.class.equals(implementedInterface) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    return true;
                }
            }
        }
        return false;
    }

}