/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.web;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

public class RestApiJAXBContextProvider implements JAXBContextProvider {

    @Context
    Providers providers;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        if (providers == null) {
            throw KapuaException.internalError("Unable to find any provider.");
        }

        ContextResolver<JAXBContext> cr = providers.getContextResolver(JAXBContext.class,
                MediaType.APPLICATION_XML_TYPE);
        JAXBContext jaxbContext = cr.getContext(JAXBContext.class);
        if (jaxbContext == null) {
            throw KapuaException.internalError("Unable to get a JAXBContext.");
        }

        return jaxbContext;
    }

}
