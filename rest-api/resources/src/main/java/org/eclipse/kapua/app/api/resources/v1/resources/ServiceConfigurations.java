/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.resources.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.commons.configuration.metatype.EmptyTocd;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.config.ServiceComponentConfiguration;
import org.eclipse.kapua.service.config.ServiceConfiguration;
import org.eclipse.kapua.service.config.ServiceConfigurationFactory;

@Path("{scopeId}/serviceConfigurations")
public class ServiceConfigurations extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final ServiceConfigurationFactory serviceConfigurationFactory = locator.getFactory(ServiceConfigurationFactory.class);

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ServiceConfiguration get(@PathParam("scopeId") ScopeId scopeId) throws KapuaException {
        List<KapuaConfigurableService> configurableServices = locator.getServices().stream().filter(service -> service instanceof KapuaConfigurableService).map(kapuaService -> (KapuaConfigurableService)kapuaService).collect(Collectors.toList());
        ServiceConfiguration serviceConfiguration = serviceConfigurationFactory.newConfigurationInstance();
        for (KapuaConfigurableService configurableService : configurableServices) {
            KapuaTocd metadata = configurableService.getConfigMetadata(scopeId);
            Map<String, Object> values = configurableService.getConfigValues(scopeId);
            if (metadata != null && !(metadata instanceof EmptyTocd)) {
                ServiceComponentConfiguration serviceComponentConfiguration = serviceConfigurationFactory.newComponentConfigurationInstance(metadata.getId());
                serviceComponentConfiguration.setDefinition(metadata);
                serviceComponentConfiguration.setName(metadata.getName());
                serviceComponentConfiguration.setProperties(values);
                serviceConfiguration.getComponentConfigurations().add(serviceComponentConfiguration);
            }
        }
        Collections.sort(serviceConfiguration.getComponentConfigurations(), Comparator.comparing(ServiceComponentConfiguration::getName));

        return serviceConfiguration;
    }

    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response update(
            @PathParam("scopeId") ScopeId scopeId,
            ServiceConfiguration serviceConfiguration
    ) throws KapuaException, ClassNotFoundException {
        for (ServiceComponentConfiguration serviceComponentConfiguration : serviceConfiguration.getComponentConfigurations()) {
            Class<KapuaService> configurableServiceClass = (Class<KapuaService>) Class.forName(serviceComponentConfiguration.getId()).asSubclass(KapuaService.class);
            if (KapuaConfigurableService.class.isAssignableFrom(configurableServiceClass)) {
                KapuaConfigurableService configurableService = (KapuaConfigurableService) locator.getService(configurableServiceClass);
                Account account = accountService.find(scopeId);
                configurableService.setConfigValues(scopeId, account.getScopeId(), serviceComponentConfiguration.getProperties());
            }
        }
        return Response.noContent().build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{serviceId}")
    public ServiceComponentConfiguration getComponent(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("serviceId") String serviceId
    ) throws KapuaException, ClassNotFoundException {
        Class<KapuaService> configurableServiceClass = (Class<KapuaService>) Class.forName(serviceId).asSubclass(KapuaService.class);
        if (KapuaConfigurableService.class.isAssignableFrom(configurableServiceClass)) {
            KapuaConfigurableService configurableService = (KapuaConfigurableService)locator.getService(configurableServiceClass);
            KapuaTocd metadata = configurableService.getConfigMetadata(scopeId);
            Map<String, Object> values = configurableService.getConfigValues(scopeId);
            if (metadata != null && !(metadata instanceof EmptyTocd)) {
                ServiceComponentConfiguration serviceComponentConfiguration = serviceConfigurationFactory.newComponentConfigurationInstance(metadata.getId());
                serviceComponentConfiguration.setDefinition(metadata);
                serviceComponentConfiguration.setName(metadata.getName());
                serviceComponentConfiguration.setProperties(values);
                return serviceComponentConfiguration;
            }
        }
        return null;
    }

    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{serviceId}")
    public Response updateComponent(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("serviceId") String serviceId,
            ServiceComponentConfiguration serviceComponentConfiguration
    ) throws KapuaException, ClassNotFoundException {
        Class<KapuaService> configurableServiceClass = (Class<KapuaService>) Class.forName(serviceId).asSubclass(KapuaService.class);
        if (KapuaConfigurableService.class.isAssignableFrom(configurableServiceClass)) {
            KapuaConfigurableService configurableService = (KapuaConfigurableService) locator.getService(configurableServiceClass);
            Account account = accountService.find(scopeId);
            configurableService.setConfigValues(scopeId, account.getScopeId(), serviceComponentConfiguration.getProperties());
        }
        return Response.noContent().build();
    }

}
