/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util.xml;

import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A default {@link JAXBContextProvider} implementation.
 * <p>
 * This simplifies the creation of a specific {@link JAXBContextProvider} asking only to provide the {@link #getClasses()}
 *
 * @since 2.0.0
 */
public abstract class DefaultJAXBContextProvider implements JAXBContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultJAXBContextProvider.class);

    protected final JAXBContext jaxbContext;

    /**
     * Constructor.
     * <p>
     * It initializes the {@link JAXBContext} with {@link #getClasses()} and the {@link #getJaxbContextProperties()}
     *
     * @since 2.0.0
     */
    public DefaultJAXBContextProvider() {
        try {
            this.jaxbContext = JAXBContextFactory.createContext(
                    getClasses().toArray(new Class[0]),
                    getJaxbContextProperties()
            );
        } catch (JAXBException e) {
            LOG.error("Cannot instantiate JaxbContextProvider!", e);
            throw new RuntimeException("Cannot instantiate JaxbContextProvider!", e);
        }

        printJAXBContextConfigurations();
    }

    @Override
    public JAXBContext getJAXBContext() {
        return getJAXBContext(Object.class);
    }

    @Override
    public JAXBContext getJAXBContext(Class<?> clazz) {
        return getContext();
    }

    /**
     * Gets the {@link JAXBContext} initialized when instantiating the {@link JAXBContextProvider}.
     *
     * @return The {@link JAXBContext}.
     * @since 2.0.0
     */
    protected JAXBContext getContext() {
        return jaxbContext;
    }

    /**
     * Gets the set of default {@link JAXBContext} properties to use.
     *
     * @return Tet of default {@link JAXBContext} properties to use.
     * @since 2.0.0
     */
    protected Map<String, Object> getJaxbContextProperties() {
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

        return properties;
    }

    /**
     * Gets the {@link List} of {@link Class} to bind to the {@link JAXBContext}.
     *
     * @return The {@link List} of {@link Class} to bind to the {@link JAXBContext}.
     * @since 2.0.0
     */
    protected abstract List<Class<?>> getClasses();

    /**
     * Pretty prints the {@link JAXBContext} configuration using the {@link ConfigurationPrinter}.
     *
     * @since 2.0.0
     */
    private void printJAXBContextConfigurations() {
        ConfigurationPrinter contextProviderConfigPrinter =
                ConfigurationPrinter
                        .create()
                        .withLogger(LOG)
                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                        .withTitle("JAXB Context Configuration");

        // Classes
        contextProviderConfigPrinter.openSection("Bound Classes")
                .addParameter("Total", getClasses().size());

        if (!LOG.isDebugEnabled()) {
            contextProviderConfigPrinter.openSection("List");
            getClasses().forEach(contextProviderConfigPrinter::addSimpleParameter);
            contextProviderConfigPrinter.closeSection();
        }
        contextProviderConfigPrinter.closeSection();

        // Properties
        contextProviderConfigPrinter.openSection("Context Properties");
        for (Map.Entry<String, Object> property : getJaxbContextProperties().entrySet()) {
            contextProviderConfigPrinter.addParameter(property.getKey(), String.valueOf(property.getValue()));
        }
        contextProviderConfigPrinter.closeSection();

        contextProviderConfigPrinter.printLog();
    }
}
