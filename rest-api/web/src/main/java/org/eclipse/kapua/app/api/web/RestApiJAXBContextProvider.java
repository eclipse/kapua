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
 *******************************************************************************/
package org.eclipse.kapua.app.api.web;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;

public class RestApiJAXBContextProvider implements JAXBContextProvider {

    @Context
    Providers providers;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        if (providers == null) {
            throw KapuaException.internalError("Unable to find any provider.");
        }

        ContextResolver<JAXBContext> cr = providers.getContextResolver(JAXBContext.class, MediaType.APPLICATION_XML_TYPE);
        JAXBContext jaxbContext = cr.getContext(JAXBContext.class);
        if (jaxbContext == null) {
            throw KapuaException.internalError("Unable to get a JAXBContext.");
        }

        return jaxbContext;
    }

}
