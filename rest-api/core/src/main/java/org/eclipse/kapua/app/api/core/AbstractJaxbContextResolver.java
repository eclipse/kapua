/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.ClassProvider;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation classes should only add the @Provider and @Produces annotations
 * required by the specific use case.
 *
 */
public class AbstractJaxbContextResolver implements ContextResolver<JAXBContext> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJaxbContextResolver.class);

    private JAXBContext context;
    private ClassProvider[] providers;

    public AbstractJaxbContextResolver(ClassProvider ... providers) {
        this.providers = providers;
    }

    public JAXBContext getContext(Class<?> type) {
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
            throw KapuaRuntimeException.internalError(e, "Error creating JAXBContext!");
        }
    }
}
