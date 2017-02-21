/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.locator;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface to load KapuaService instances in a given environment.<br>
 * Implementations of the KapuaServiceLocator can decide whether to return local instances or to acts as a proxy to remote instances.<br>
 * The locator is self initialized, it looks for the proper locator implementation class looking at {@link KapuaLocator#LOCATOR_CLASS_NAME_SYSTEM_PROPERTY} system property or falling back to the
 * {@link KapuaLocator#LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY} (if the previous property is not defined).
 * 
 * @since 1.0
 * 
 */
public abstract class KapuaLocator {

    private static Logger logger = LoggerFactory.getLogger(KapuaLocator.class);

    private final static KapuaLocator instance = createInstance();

    /**
     * {@link KapuaLocator} implementation classname specified via "System property" constants
     */
    public final static String LOCATOR_CLASS_NAME_SYSTEM_PROPERTY = "locator.class.impl";

    /**
     * {@link KapuaLocator} implementation classname specified via "Environment property" constants
     */
    public final static String LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY = "LOCATOR_CLASS_IMPL";

    // TODO do we need synchronization?
    /**
     * Creates the {@link KapuaLocator} instance,
     * 
     * @return
     */
    private static KapuaLocator createInstance() {
        logger.info("initializing Servicelocator instance... ");
        String locatorImplementation = locatorClassName();
        if (locatorImplementation != null && locatorImplementation.trim().length() > 0) { // FIXME: use more appropriate .isEmpty()
            try {
                return (KapuaLocator) Class.forName(locatorImplementation).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.info("An error occurred during Servicelocator initialization", e);
            }
        }
        // proceed with the default service locator instantiation if env variable is null or some error occurred during the specific service locator instantiation
        logger.info("initialize Servicelocator with the default instance... ");
        ServiceLoader<KapuaLocator> serviceLocatorLoaders = ServiceLoader.load(KapuaLocator.class);
        KapuaLocator kapuaServiceLocator = null;
        Iterator<KapuaLocator> serviceLocatorLoaderIterator = serviceLocatorLoaders.iterator();
        while (serviceLocatorLoaderIterator.hasNext()) {
            kapuaServiceLocator = serviceLocatorLoaderIterator.next();
            break;
        }
        if (kapuaServiceLocator == null) {
            throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.SERVICE_LOCATOR_UNAVAILABLE);
        }
        logger.info("initialize Servicelocator with the default instance... DONE");
        return kapuaServiceLocator;
    }

    /**
     * Return the {@link KapuaLocator} instance (singleton).
     * 
     * @return
     */
    public static KapuaLocator getInstance() {
        return instance;
    }

    /**
     * Get the locator classname implementation looking at the {@link KapuaLocator#LOCATOR_CLASS_NAME_SYSTEM_PROPERTY} system property or falling back to the
     * {@link KapuaLocator#LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY} environment variable.
     * 
     * @return
     */
    static String locatorClassName() {
        String locatorClass = System.getProperty(LOCATOR_CLASS_NAME_SYSTEM_PROPERTY);
        if (locatorClass != null) {
            return locatorClass;
        }

        locatorClass = System.getenv(LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY);
        if (locatorClass != null) {
            return locatorClass;
        }

        logger.debug("No service locator class resolved. Falling back to default.");
        return null;
    }

    /**
     * Returns an instance of a KapuaService implementing the provided KapuaService class.
     * 
     * @param serviceClass
     *            - class of the service whose instance is required.
     * @throws KapuaRuntimeException
     *             with KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE code if service is not available
     * @return service instance
     */
    public abstract <S extends KapuaService> S getService(Class<S> serviceClass);

    /**
     * Returns an instance of a KapuaEntityFactory implementing the provided KapuaFactory class.
     * 
     * @param factoryClass
     *            - class of the factory whose instance is required.
     * @return
     */
    public abstract <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass);

}
