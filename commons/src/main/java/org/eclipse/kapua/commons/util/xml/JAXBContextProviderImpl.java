/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.JaxbClassProvider;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class JAXBContextProviderImpl implements JAXBContextProvider {

    private final Logger logger = LoggerFactory.getLogger(JAXBContextProviderImpl.class);
    private final ConfigurationPrinter configurationPrinter;

    private JAXBContext context;
    private Set<JaxbClassProvider> providers;

    @Inject
    public JAXBContextProviderImpl(Set<JaxbClassProvider> providers) {
        logger.info("Initializing with {} providers", providers.size());
        this.providers = providers;
        this.configurationPrinter = ConfigurationPrinter
                .create()
                .withLogger(logger)
                .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                .withTitle("Kapua Jaxb Context Resolver");
    }

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                final Map<String, Object> properties = new HashMap<>();
                properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

                final Set<Class<?>> classes = providers
                        .stream()
                        .flatMap(p -> p.getClasses().stream())
                        .collect(Collectors.toSet());
                if (configurationPrinter.getParentLogger().isDebugEnabled()) {
                    // Printing like this is highly verbose
                    configurationPrinter.logSections("Loaded XmlSerializable Classes", classes
                            .stream()
                            .map(Class::getName)
                            .sorted()
                            .collect(Collectors.toList()));
                } else {
                    configurationPrinter.addParameter("Loaded XmlSerializable Classes", classes.size());
                }
                // Print it!
                configurationPrinter.printLog();
                context = JAXBContextFactory.createContext(classes.toArray(new Class<?>[] {}), properties);
                logger.info("Default JAXB context initialized with {} classes!", classes.size());
            }
            return context;
        } catch (JAXBException e) {
            throw KapuaException.internalError(e, "Error creating JAXBContext!");
        }
    }
}