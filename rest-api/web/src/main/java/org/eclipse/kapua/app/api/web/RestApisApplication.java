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

import org.eclipse.kapua.app.api.core.KapuaSerializableBodyWriter;
import org.eclipse.kapua.app.api.core.ListBodyWriter;
import org.eclipse.kapua.app.api.core.MoxyJsonConfigContextResolver;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.UriConnegFilter;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import java.util.HashMap;

public class RestApisApplication extends ResourceConfig {

    public RestApisApplication() throws JAXBException {
        packages("org.eclipse.kapua.app.api",
                "org.eclipse.kapua.service");

        // Bind media type to resource extension
        HashMap<String, MediaType> mappedMediaTypes = new HashMap<>();
        mappedMediaTypes.put("xml", MediaType.APPLICATION_XML_TYPE);
        mappedMediaTypes.put("json", MediaType.APPLICATION_JSON_TYPE);

        property(ServerProperties.MEDIA_TYPE_MAPPINGS, mappedMediaTypes);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);

        // Manually adding MOXyJSONFeature
        register(MoxyJsonFeature.class);
        register(MoxyJsonConfigContextResolver.class);
        register(UriConnegFilter.class);
        register(JaxbContextResolver.class);
        register(RestApiJAXBContextProvider.class);
        register(KapuaSerializableBodyWriter.class);
        register(ListBodyWriter.class);

        register(new ContainerLifecycleListener() {

            @Override
            public void onStartup(Container container) {
                ServiceLocator serviceLocator = container.getApplicationHandler().getInjectionManager().getInstance(ServiceLocator.class);

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
