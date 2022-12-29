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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ClassProvider;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class JAXBContextProviderImpl implements JAXBContextProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JAXBContext context;
    private final Set<ClassProvider> providers;

    @Inject
    public JAXBContextProviderImpl(Set<ClassProvider> providers) {
        logger.info("Initializing with {} providers", providers.size());
        this.providers = providers;
    }

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                final Map<String, Object> properties = new HashMap<>(1);
                properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
                final Set<Class<?>> classes = providers
                        .stream()
                        .flatMap(p -> p.getClasses().stream())
                        .collect(Collectors.toSet());
                context = JAXBContextFactory.createContext(classes.toArray(new Class<?>[]{}), properties);
                logger.info("Default JAXB context initialized with {} classes!", classes.size());
            }
            return context;
        } catch (JAXBException e) {
            throw KapuaException.internalError(e, "Error creating JAXBContext!");
        }
    }
}