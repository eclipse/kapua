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
package org.eclipse.kapua.locator;

import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface to load KapuaService instances in a given environment.<br>
 * Implementations of the KapuaServiceLocator can decide whether to return local instances or to acts as a proxy to remote instances.<br>
 * The locator is self initialized, it looks for the proper locator implementation class looking at {@link KapuaLocator#LOCATOR_CLASS_NAME_SYSTEM_PROPERTY} system property or falling back to the
 * {@link KapuaLocator#LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY} (if the previous property is not defined).
 *
 * @since 1.0
 */
public abstract class KapuaLocator implements KapuaServiceLoader {

    private static final Logger logger = LoggerFactory.getLogger(KapuaLocator.class);

    private static KapuaLocator instance = createInstance();

    /**
     * {@link KapuaLocator} implementation classname specified via "System property" constants
     */
    public static final String LOCATOR_CLASS_NAME_SYSTEM_PROPERTY = "locator.class.impl";

    /**
     * {@link KapuaLocator} implementation classname specified via "Environment property" constants
     */
    public static final String LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY = "LOCATOR_CLASS_IMPL";

    // TODO do we need synchronization?

    /**
     * Creates the {@link KapuaLocator} instance,
     *
     * @return
     */
    private static KapuaLocator createInstance() {
        try {
            logger.info("initializing Servicelocator instance... ");
            String locatorImplementation = locatorClassName();
            if (locatorImplementation != null && !locatorImplementation.trim().isEmpty()) {
                try {
                    return (KapuaLocator) Class.forName(locatorImplementation).newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    logger.info("An error occurred during Servicelocator initialization", e);
                }
            }

            // proceed with the default service locator instantiation if env variable is null or some error occurred during the specific service locator instantiation

            logger.info("initialize Servicelocator with the default instance... ");
            ServiceLoader<KapuaLocator> serviceLocatorLoaders = ServiceLoader.load(KapuaLocator.class);
            for (KapuaLocator locator : serviceLocatorLoaders) {
                // simply return the first
                logger.info("initialize Servicelocator with the default instance... DONE");
                return locator;
            }
        } catch (Exception e) {
            logger.error("Error initializing locator...", e);
            throw e;
        }
        // none returned

        throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.SERVICE_LOCATOR_UNAVAILABLE);
    }

    /**
     * Return the {@link KapuaLocator} instance (singleton).
     *
     * @return
     */
    public static KapuaLocator getInstance() {
        return instance;
    }

    public static void clearInstance() {
        instance = createInstance();
    }

    /**
     * Get the locator classname implementation looking at the {@link KapuaLocator#LOCATOR_CLASS_NAME_SYSTEM_PROPERTY} system property or falling back to the
     * {@link KapuaLocator#LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY} environment variable.
     *
     * @return
     */
    static String locatorClassName() {
        String locatorClass = System.getProperty(LOCATOR_CLASS_NAME_SYSTEM_PROPERTY);
        if (locatorClass != null && !locatorClass.isEmpty()) {
            return locatorClass;
        }

        locatorClass = System.getenv(LOCATOR_CLASS_NAME_ENVIRONMENT_PROPERTY);
        if (locatorClass != null && !locatorClass.isEmpty()) {
            return locatorClass;
        }

        logger.debug("No service locator class resolved. Falling back to default.");
        return null;
    }
}
