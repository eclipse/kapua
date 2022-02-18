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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ClassProvider;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBContextProviderBase implements JAXBContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JAXBContextProviderBase.class);

    private JAXBContext context;
    private ClassProvider[] providers;

    public JAXBContextProviderBase(ClassProvider ... providers) {
        this.providers = providers;
    }

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                Map<String, Object> properties = new HashMap<>(1);
                properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

                List<Class<?>> classes = new ArrayList<>();
                for (ClassProvider provider:providers) {
                    classes.addAll(provider.getClasses());
                }
                context = JAXBContextFactory.createContext(classes.toArray(new Class<?>[] {}), properties);
                LOG.debug("Default JAXB context initialized!");
            }
            return context;
        } catch (JAXBException e) {
            throw KapuaException.internalError(e, "Error creating JAXBContext!");
        }
    }
}