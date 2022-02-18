/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.ServiceModuleConfiguration;
import org.eclipse.kapua.commons.core.ServiceModuleJaxbClassConfig;
import org.eclipse.kapua.commons.core.ServiceModuleProvider;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link Guice} {@link KapuaLocator} implementation.
 *
 * @since 1.0.0
 */
public class GuiceLocatorImpl extends KapuaLocator {

    private static final Logger LOG = LoggerFactory.getLogger(GuiceLocatorImpl.class);

    // Default service resource file from which the managed services are read
    private static final String SERVICE_RESOURCE = "locator.xml";

    private Injector injector;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public GuiceLocatorImpl() {
        this(SERVICE_RESOURCE);
    }

    public GuiceLocatorImpl(@NotNull String resourceName) {
        try {
            init(resourceName);
        } catch (Exception e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e);
        }
    }

    @Override
    public <S extends KapuaService> S getService(Class<S> serviceClass) {
        try {
            return injector.getInstance(serviceClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, e, serviceClass);
        }
    }

    @Override
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass) {
        try {
            return injector.getInstance(factoryClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, factoryClass);
        }
    }

    public <T> T getComponent(Class<T> componentClass) {
        try {
            return injector.getInstance(componentClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.COMPONENT_UNAVAILABLE, componentClass);
        }
    }

    @Override
    public List<KapuaService> getServices() {
        final List<KapuaService> servicesList = new ArrayList<>();
        final Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Binding<?> binding : bindings.values()) {
            final Class<?> clazz = binding.getKey().getTypeLiteral().getRawType();
            if (KapuaService.class.isAssignableFrom(clazz)) {
                servicesList.add(injector.getInstance(clazz.asSubclass(KapuaService.class)));
            }
        }
        return servicesList;
    }

    /**
     * Initializes the {@link KapuaLocator} with the given resource name configuration.
     *
     * @param locatorConfigName The resource name containing the configuration.
     * @throws Exception
     * @since 1.0.0
     */
    private void init(String locatorConfigName) throws Exception {
        //
        // Read configurations from locator file
        URL locatorConfigURL = ResourceUtils.getResource(locatorConfigName);
        if (locatorConfigURL == null) {
            throw new Exception("Locator configuration cannot be found: " + locatorConfigName);
        }

        LocatorConfig locatorConfig = LocatorConfig.fromURL(locatorConfigURL);

        //
        // Scan packages listed in to find KapuaModules
        Collection<String> packageNames = locatorConfig.getIncludedPackageNames();
        Reflections reflections = new Reflections(packageNames);
        Set<Class<? extends AbstractKapuaModule>> kapuaModuleClasses = reflections.getSubTypesOf(AbstractKapuaModule.class);

        //
        // Instantiate Kapua modules
        List<AbstractKapuaModule> kapuaModules = new ArrayList<>();
        List<Class<? extends AbstractKapuaModule>> excludedKapuaModules = new ArrayList<>();
        for (Class<? extends AbstractKapuaModule> moduleClazz : kapuaModuleClasses) {
            if (isExcluded(moduleClazz.getName(), locatorConfig.getExcludedPackageNames())) {
                excludedKapuaModules.add(moduleClazz);
                continue;
            }

            kapuaModules.add(moduleClazz.newInstance());
        }

        // KapuaModule will be removed as soon as bindings will be moved to local modules
        kapuaModules.add(new KapuaModule(locatorConfigName));

        //
        // Print loaded stuff
        printLoadedKapuaModuleConfiguration(locatorConfigURL, locatorConfig, kapuaModules, excludedKapuaModules);

        //
        // Scan XmlSerializable
        Set<Class<?>> xmlSerializableClasses = reflections.getTypesAnnotatedWith(XmlRootElement.class);
        List<Class<?>> loadedXmlSerializables = new ArrayList<>();
        List<Class<?>> excludedXmlSerializables = new ArrayList<>();
        for (Class<?> xmlSerializableClass : xmlSerializableClasses) {
            if (isExcluded(xmlSerializableClass.getName(), locatorConfig.getExcludedPackageNames()) ||
                    !xmlSerializableClass.isAnnotationPresent(XmlRootElement.class)) {
                excludedXmlSerializables.add(xmlSerializableClass);
                continue;
            }
            loadedXmlSerializables.add(xmlSerializableClass);
        }
        ServiceModuleJaxbClassConfig.setSerializables(loadedXmlSerializables);

        //
        // Print loaded stuff
        printLoadedXmlSerializableConfiguration(locatorConfigURL, locatorConfig, loadedXmlSerializables, excludedXmlSerializables);

        //
        // Create injector
        injector = Guice.createInjector(kapuaModules);
        ServiceModuleConfiguration.setConfigurationProvider(() -> injector.getInstance(ServiceModuleProvider.class));
    }

    /**
     * Checks whether the given {@link Class#getName()} matches one of the excluded {@link Class#getPackage()}
     *
     * @param className        The {@link Class#getName()} to check.
     * @param excludedPackages The {@link Collection} of excluded {@link Class#getPackage()}.
     * @return {@code true} if matched, {@code false otherwise}
     * @since 2.0.0
     */
    private boolean isExcluded(@NotNull String className, @NotNull Collection<String> excludedPackages) {
        for (String pkg : excludedPackages) {
            if (className.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the configuration of the loaded {@link KapuaModule}s.
     *
     * @param resourceNameURL The {@link KapuaLocator} configuration resource name.
     * @param locatorConfig   The loaded {@link LocatorConfig}.
     * @param kapuaModules    The laaded {@link KapuaModule}s
     * @since 2.0.0
     */
    private void printLoadedKapuaModuleConfiguration(@NotNull URL resourceNameURL, @NotNull LocatorConfig locatorConfig, @NotNull List<AbstractKapuaModule> kapuaModules, @NotNull List<Class<? extends AbstractKapuaModule>> excludedKapuaModules) {
        ConfigurationPrinter configurationPrinter =
                ConfigurationPrinter
                        .create()
                        .withLogger(LOG)
                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                        .withTitle("Kapua Locator Configuration")
                        .addParameter("Resource Name", resourceNameURL.getPath());

        // Packages
        addIncludedExcludedPackageConfig(configurationPrinter, locatorConfig);

        // Loaded modules
        configurationPrinter.openSection("Loaded Kapua Modules");
        if (!kapuaModules.isEmpty()) {
            for (AbstractKapuaModule kapuaModule : kapuaModules.stream().sorted(Comparator.comparing(a -> a.getClass().getName())).collect(Collectors.toList())) {
                configurationPrinter.addSimpleParameter(kapuaModule.getClass().getName());
            }
        } else {
            configurationPrinter.addSimpleParameter("None");
        }
        configurationPrinter.closeSection();

        // Loaded modules
        configurationPrinter.openSection("Excluded Kapua Modules");
        if (!excludedKapuaModules.isEmpty()) {
            for (Class<? extends AbstractKapuaModule> excludedKapuaModule : sortedClass(excludedKapuaModules)) {
                configurationPrinter.addSimpleParameter(excludedKapuaModule.getName());
            }
        } else {
            configurationPrinter.addSimpleParameter("None");
        }
        configurationPrinter.closeSection();

        // Print it!
        configurationPrinter.printLog();
    }

    /**
     * Prints the configuration of the loaded {@link KapuaModule}s.
     *
     * @param resourceNameURL       The {@link KapuaLocator} configuration resource name.
     * @param locatorConfig         The loaded {@link LocatorConfig}.
     * @param loadedXmlSerializable The laaded {@link KapuaModule}s
     * @since 2.0.0
     */
    private void printLoadedXmlSerializableConfiguration(@NotNull URL resourceNameURL, @NotNull LocatorConfig locatorConfig, @NotNull List<Class<?>> loadedXmlSerializable, @NotNull List<Class<?>> excludedXmlSerializable) {
        ConfigurationPrinter configurationPrinter =
                ConfigurationPrinter
                        .create()
                        .withLogger(LOG)
                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                        .withTitle("Kapua XmlSerializable Configuration")
                        .addParameter("Resource Name", resourceNameURL.getPath());

        // Packages
        addIncludedExcludedPackageConfig(configurationPrinter, locatorConfig);

        // Loaded modules
        if (LOG.isDebugEnabled()) {
            // Printing like this is highly verbose
            configurationPrinter.openSection("Loaded XmlSerializable Classes");
            if (!loadedXmlSerializable.isEmpty()) {
                for (Class<?> xmlSerializableClass : sortedClass(loadedXmlSerializable)) {
                    configurationPrinter.addSimpleParameter(xmlSerializableClass.getName());
                }
            } else {
                configurationPrinter.addSimpleParameter("None");
            }
            configurationPrinter.closeSection();
        } else {
            configurationPrinter.addParameter("Loaded XmlSerializable Classes", loadedXmlSerializable.size());
        }

        // Loaded modules
        if (LOG.isDebugEnabled()) {
            // Printing like this is highly verbose
            configurationPrinter.openSection("Excluded XmlSerializable Classes");
            if (!excludedXmlSerializable.isEmpty()) {
                for (Class<?> xmlSerializableClass : sortedClass(excludedXmlSerializable)) {
                    configurationPrinter.addSimpleParameter(xmlSerializableClass.getName());
                }
            } else {
                configurationPrinter.addSimpleParameter("None");
            }
            configurationPrinter.closeSection();
        } else {
            configurationPrinter.addParameter("Excluded XmlSerializable Classes", excludedXmlSerializable.size());
        }

        // Print it!
        configurationPrinter.printLog();
    }

    /**
     * Adds the given {@link ConfigurationPrinter} the configuration of included and excluded packages.
     *
     * @param configurationPrinter The {@link ConfigurationPrinter} to add the configuration to.
     * @param locatorConfig        The {@link LocatorConfig}.
     * @since 2.0.0
     */
    private void addIncludedExcludedPackageConfig(@NotNull ConfigurationPrinter configurationPrinter, @NotNull LocatorConfig locatorConfig) {
        // Inclusions
        configurationPrinter.openSection("Included packages");
        if (!locatorConfig.getIncludedPackageNames().isEmpty()) {
            for (String includedPackages : sortedComparable(locatorConfig.getIncludedPackageNames())) {
                configurationPrinter.addSimpleParameter(includedPackages);
            }
        } else {
            configurationPrinter.addSimpleParameter("None");
        }
        configurationPrinter.closeSection();

        // Exclusions
        configurationPrinter.openSection("Excluded packages");
        if (!locatorConfig.getExcludedPackageNames().isEmpty()) {
            for (String excludedPackages : sortedComparable(locatorConfig.getExcludedPackageNames())) {
                configurationPrinter.addSimpleParameter(excludedPackages);
            }
        } else {
            configurationPrinter.addSimpleParameter("None");
        }
        configurationPrinter.closeSection();
    }

    /**
     * Sorts the given {@link Collection} of {@link Comparable}s according to the natural order.
     *
     * @param strings The {@link Collection} to sort.
     * @param <C>     The type of {@link Comparable}.
     * @return The sorted {@link Collection}.
     * @since 2.0.0
     */
    private <C extends Comparable<?>> Collection<C> sortedComparable(Collection<C> strings) {
        return strings.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Sorts the given {@link Collection} of {@link Class}es according to the {@link Class#getName()}.
     *
     * @param classes The {@link Collection} to sort.
     * @param <T>     The type of {@link Class}.
     * @return The sorted {@link Collection}.
     * @since 2.0.0
     */
    private <T extends Class<?>> Collection<T> sortedClass(Collection<T> classes) {
        return classes.stream().sorted(Comparator.comparing(Class::getName)).collect(Collectors.toList());
    }
}
