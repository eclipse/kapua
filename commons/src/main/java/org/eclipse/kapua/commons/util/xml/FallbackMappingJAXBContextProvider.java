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

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fallback mapping {@link JAXBContextProvider} implementation.
 * <p>
 * It extends the behaviour of {@link DefaultJAXBContextProvider} when requested for a specific {@link Class} which is not present on the {@link JAXBContext}.
 * If not present it will create a {@link JAXBContext} on the fly to avoid {@link JAXBException} for missing mapping.
 * It will also print a {@link Logger#warn(String)} so developers are notified about the missing mapping.
 *
 * @since 2.0.0
 */
public abstract class FallbackMappingJAXBContextProvider extends DefaultJAXBContextProvider implements JAXBContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackMappingJAXBContextProvider.class);

    @Override
    public JAXBContext getJAXBContext(Class<?> clazz) {
        if (!clazz.equals(Object.class) &&
                !getClasses().contains(clazz)) {
            LOG.warn("The given class {} is not listed in the JaxbContextProvider. Creating it on the fly to avoid JAXBException. " +
                    "Please consider to add the given class to the list to fix this!", clazz);

            try {
                List<Class<?>> classes = new ArrayList<>(getClasses());
                classes.add(clazz);

                return JAXBContextFactory.createContext(
                        classes.toArray(new Class[0]),
                        getJaxbContextProperties()
                );
            } catch (JAXBException e) {
                LOG.error("Cannot instantiate fallback JAXBContextProvider for class {}!", clazz, e);
                throw new RuntimeException("Cannot instantiate fallback JAXBContextProvider for class: " + clazz + "!", e);
            }
        }

        return getContext();
    }
}
