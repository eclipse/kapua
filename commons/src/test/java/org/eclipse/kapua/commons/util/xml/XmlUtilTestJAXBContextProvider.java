/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.persistence.jaxb.MarshallerProperties;

import javax.xml.bind.JAXBContext;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link JAXBContextProvider} implementation to be used in {@link XmlUtilTest}.
 *
 * @since 1.6.0
 */
public class XmlUtilTestJAXBContextProvider implements JAXBContextProvider {

    private JAXBContext jaxbContext;

    @Override
    public JAXBContext getJAXBContext() {
        try {
            if (jaxbContext == null) {
                Map<String, Object> properties = new HashMap<>(1);
                properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

                jaxbContext = JAXBContextFactory.createContext(new Class<?>[]{
                        XmlUtilTestObject.class,
                }, properties);
            }
            return jaxbContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
