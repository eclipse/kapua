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
package org.eclipse.kapua.app.web.commons.xml;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;

public abstract class JaxRSJAXBContextProvider implements JAXBContextProvider {

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        return getJAXBContext(Object.class);
    }

    @Override
    public JAXBContext getJAXBContext(Class<?> clazz) throws KapuaException {
        if (getProviders() == null) {
            throw KapuaException.internalError("Unable to find any provider.");
        }

        ContextResolver<JAXBContext> contextResolver = getProviders().getContextResolver(JAXBContext.class, getMediaType());
        JAXBContext jaxbContext = contextResolver.getContext(clazz);
        if (jaxbContext == null) {
            throw KapuaException.internalError("Unable to get a JAXBContext.");
        }

        return jaxbContext;
    }

    protected abstract Providers getProviders();

    protected abstract MediaType getMediaType();
}
