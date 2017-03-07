/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.internal.UserImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Domains")
@Path("{scopeId}/domains")
public class Domains extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DomainService domainService = locator.getService(DomainService.class);
    private final DomainFactory domainFactory = locator.getFactory(DomainFactory.class);

    /**
     * Gets the {@link Domain} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link DomainListResult} of all the domains associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the Domain list in the scope", //
            notes = "Returns the list of all the domains associated to the current selected scope.", //
            response = Domain.class, //
            responseContainer = "DomainListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DomainListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
            @QueryParam("name") String name,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        DomainListResult domainListResult = domainFactory.newDomainListResult();
        try {
            DomainQuery query = domainFactory.newQuery();
            
            AndPredicate andPredicate = new AndPredicate();
            if (name != null) {
                andPredicate.and(new AttributePredicate<>(DomainPredicates.NAME, name));
            }
            query.setPredicate(andPredicate);
            
            query.setOffset(offset);
            query.setLimit(limit);
            
            domainListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return domainListResult;
    }
    
    /**
     * Queries the results with the given {@link DomainQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link DomainQuery} to used to filter results.
     * @return The {@link DomainListResult} of all the result matching the given {@link DomainQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public DomainListResult query(@PathParam("scopeId") ScopeId scopeId, DomainQuery query) {
        DomainListResult domainListResult = null;
        try {
            domainListResult = domainService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(domainListResult);
    }
    

    /**
     * Counts the results with the given {@link DomainQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link DomainQuery} to used to filter results.
     * @return The count of all the result matching the given {@link DomainQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, DomainQuery query) {
        CountResult countResult = null;
        try {
            countResult = new CountResult(domainService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Returns the Domain specified by the "domainId" path parameter.
     *
     * @param domainId
     *            The id of the requested Domain.
     * @return The requested Domain object.
     */
    @ApiOperation(value = "Get an Domain", notes = "Returns the Domain specified by the \"domainId\" path parameter.", response = Domain.class)
    @GET
    @Path("{domainId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Domain find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested Domain", required = true) @PathParam("domainId") EntityId domainId) {
        Domain domain = null;
        try {
            domain = domainService.find(scopeId, domainId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(domain);
    }
}
