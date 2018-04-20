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

import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.app.api.core.CORSResponseFilter;
import org.eclipse.kapua.app.api.core.KapuaSerializableBodyWriter;
import org.eclipse.kapua.app.api.core.ListBodyWriter;
import org.eclipse.kapua.app.api.core.MoxyJsonConfigContextResolver;
import org.eclipse.kapua.app.api.core.SwaggerDefinition;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.UriConnegFilter;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

public class RestApisApplication extends ResourceConfig {

    public RestApisApplication() throws JAXBException {
        packages("org.eclipse.kapua.app.api",
                "org.eclipse.kapua.service.account",
                "org.eclipse.kapua.service.account.internal",
                "org.eclipse.kapua.service.user",
                "org.eclipse.kapua.service.user.internal");

        // Bind media type to resource extension
        HashMap<String, MediaType> mappedMediaTypes = new HashMap<>();
        mappedMediaTypes.put("xml", MediaType.APPLICATION_XML_TYPE);
        mappedMediaTypes.put("json", MediaType.APPLICATION_JSON_TYPE);

        property(ServerProperties.MEDIA_TYPE_MAPPINGS, mappedMediaTypes);
        register(UriConnegFilter.class);
        register(JaxbContextResolver.class);
        register(RestApiJAXBContextProvider.class);
        register(KapuaSerializableBodyWriter.class);
        register(ListBodyWriter.class);
        register(CORSResponseFilter.class);
        register(SwaggerDefinition.class);
        register(MoxyJsonConfigContextResolver.class);

        register(new ContainerLifecycleListener() {

            @Override
            public void onStartup(Container container) {
                ServiceLocator serviceLocator = container.getApplicationHandler().getServiceLocator();

                RestApiJAXBContextProvider provider = serviceLocator.createAndInitialize(RestApiJAXBContextProvider.class);
                XmlUtil.setContextProvider(provider);
            }

            @Override
            /**
             * Nothing to do
             */
            public void onReload(Container container) {
            }

            @Override
            /**
             * Nothing to do
             */
            public void onShutdown(Container container) {
            }
        });
    }
}
