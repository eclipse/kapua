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
import com.google.inject.multibindings.Multibinder;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.ClassProvider;
import org.eclipse.kapua.commons.core.ServiceModuleConfiguration;
import org.eclipse.kapua.commons.core.ServiceModuleProvider;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProviderImpl;
import org.eclipse.kapua.commons.util.xml.XmlSerializableClassesProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.ArrayList;
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

        final LocatorConfig locatorConfig = LocatorConfig.fromURL(locatorConfigURL);
        ConfigurationPrinter configurationPrinter =
                ConfigurationPrinter
                        .create()
                        .withLogger(LOG)
                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                        .withTitle("Kapua Locator Configuration")
                        .addParameter("Resource Name", locatorConfigURL.getPath());
        //
        // Scan packages listed in to find KapuaModules
        final Reflections reflections = new Reflections(locatorConfig.getIncludedPackageNames());
        final Set<Class<? extends AbstractKapuaModule>> kapuaModuleClasses = reflections.getSubTypesOf(AbstractKapuaModule.class);

        //
        // Instantiate Kapua modules
        final Map<Boolean, List<Class<? extends AbstractKapuaModule>>> includedAndExcludedModules = kapuaModuleClasses
                .stream()
                .collect(Collectors.partitioningBy(moduleClazz -> !locatorConfig.getExcludedPackageNames().stream().anyMatch(pn -> moduleClazz.getName().startsWith(pn))));

        final List<AbstractKapuaModule> instantiatedKapuaModules = new ArrayList<>();
        for (Class<? extends AbstractKapuaModule> moduleClazz : includedAndExcludedModules.get(true)) {
            instantiatedKapuaModules.add(moduleClazz.newInstance());
        }

        // KapuaModule will be removed as soon as bindings will be moved to local modules
        instantiatedKapuaModules.add(new KapuaModule(locatorConfigName));

        //
        // Print loaded stuff
        configurationPrinter.logSections("Included packages", locatorConfig.getIncludedPackageNames());
        configurationPrinter.logSections("Excluded packages", locatorConfig.getExcludedPackageNames());

        // Loaded modules
        configurationPrinter.logSections("Loaded Kapua Modules", instantiatedKapuaModules, Comparator.comparing(a -> a.getClass().getName()));
        configurationPrinter.logSections("Excluded Kapua Modules", includedAndExcludedModules.get(false), Comparator.comparing(a -> a.getClass().getName()));

        // Print it!
        configurationPrinter.printLog();

        //Add JAXB custom module to the lot
        instantiatedKapuaModules.add(new AbstractKapuaModule() {
            @Override
            protected void configureModule() {
                final Multibinder<ClassProvider> classProviderBinder = Multibinder.newSetBinder(binder(), ClassProvider.class);
                classProviderBinder.addBinding()
                        .toInstance(
                                new XmlSerializableClassesProvider(
                                        configurationPrinter,
                                        locatorConfig.getIncludedPackageNames(),
                                        locatorConfig.getExcludedPackageNames()));
                bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class);
            }
        });
        //
        // Create injector
        injector = Guice.createInjector(instantiatedKapuaModules);
        XmlUtil.setContextProvider(injector.getInstance(JAXBContextProvider.class));
        ServiceModuleConfiguration.setConfigurationProvider(() -> injector.getInstance(ServiceModuleProvider.class));
    }

}
