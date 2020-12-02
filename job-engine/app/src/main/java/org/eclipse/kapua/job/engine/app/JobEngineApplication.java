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
package org.eclipse.kapua.job.engine.app;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.app.jaxb.JobEngineJAXBContextProvider;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.UriConnegFilter;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

public class JobEngineApplication extends ResourceConfig {

    public JobEngineApplication() {
        packages("org.eclipse.kapua.job.engine.app", "org.eclipse.kapua.app.api.core");

        // Bind media type to resource extension
        HashMap<String, MediaType> mappedMediaTypes = new HashMap<>();
        mappedMediaTypes.put("json", MediaType.APPLICATION_JSON_TYPE);

        property(ServerProperties.MEDIA_TYPE_MAPPINGS, mappedMediaTypes);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        register(UriConnegFilter.class);
        register(JacksonFeature.class);

        register(new ContainerLifecycleListener() {

            @Override
            public void onStartup(Container container) {
                ServiceLocator serviceLocator = container.getApplicationHandler().getServiceLocator();
                JobEngineJAXBContextProvider provider = serviceLocator.createAndInitialize(JobEngineJAXBContextProvider.class);
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
