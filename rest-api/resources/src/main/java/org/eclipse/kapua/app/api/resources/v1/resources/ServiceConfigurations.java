/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

@Api(value = "Service Configurations", authorizations = {@Authorization(value = "kapuaAccessToken")})
@Path("{scopeId}/configurations")
public class ServiceConfigurations extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final ServiceConfigurationFactory serviceConfigurationFactory = locator.getFactory(ServiceConfigurationFactory.class);

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "serviceConfigurationGet", value = "Gets multiple services configurations", notes = "Returns the current configuration of multiple services from an account")
    public ServiceConfiguration get(@ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId) throws Exception {
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
        return serviceConfiguration;
    }

    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "serviceConfigurationUpdate", value = "Updates multiple services configuration", notes = "Updates the configuration of multiple services in an account")
    public Response update(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The values for the configurations", required = true) ServiceConfiguration serviceConfiguration
    ) throws Exception {
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
    @ApiOperation(nickname = "serviceConfigurationComponentGet", value = "Gets the configuration of a service on an account", notes = "Returns the configuration of a single service in an account")
    public ServiceComponentConfiguration getComponent(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The full class name of the service.", required = true) @PathParam("serviceId") String serviceId
    ) throws Exception {
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
    @ApiOperation(nickname = "serviceConfigurationComponentUpdate", value = "Updates the configuration of a service on an account", notes = "Updates a service component configuration")
    public Response updateComponent(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The full class name of the service.", required = true) @PathParam("serviceId") String serviceId,
            @ApiParam(value = "The values for the configurations", required = true) ServiceComponentConfiguration serviceComponentConfiguration
    ) throws Exception {
        Class<KapuaService> configurableServiceClass = (Class<KapuaService>) Class.forName(serviceId).asSubclass(KapuaService.class);
        if (KapuaConfigurableService.class.isAssignableFrom(configurableServiceClass)) {
            KapuaConfigurableService configurableService = (KapuaConfigurableService) locator.getService(configurableServiceClass);
            Account account = accountService.find(scopeId);
            configurableService.setConfigValues(scopeId, account.getScopeId(), serviceComponentConfiguration.getProperties());
        }
        return Response.noContent().build();
    }

}
