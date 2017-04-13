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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;

import com.google.common.base.Strings;

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
     * @param name    The {@link Domain} name in which to search results.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link DomainListResult} of all the domains associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the Domain list in the scope",
            notes = "Returns the list of all the domains associated to the current selected scope.",
            response = Domain.class,
            responseContainer = "DomainListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DomainListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The domain name to filter results.") @QueryParam("name") String name,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) {
        DomainListResult domainListResult = domainFactory.newListResult();
        try {
            DomainQuery query = domainFactory.newQuery(null);

            AndPredicate andPredicate = new AndPredicate();
            if (!Strings.isNullOrEmpty(name)) {
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
     * @param query   The {@link DomainQuery} to use to filter results.
     * @return The {@link DomainListResult} of all the result matching the given {@link DomainQuery} parameter.
     * @since 1.0.0
     */
    @ApiOperation(value = "Queries the Domains",
            notes = "Queries the Domains with the given DomainQuery parameter returning all matching Domains",
            response = Domain.class,
            responseContainer = "DomainListResult")
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public DomainListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The DomainQuery to use to filter results.", required = true) DomainQuery query) {
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
     * @param query   The {@link DomainQuery} to use to filter results.
     * @return The count of all the result matching the given {@link DomainQuery} parameter.
     * @since 1.0.0
     */
    @ApiOperation(value = "Counts the Domains",
            notes = "Counts the Domains with the given DomainQuery parameter returning the number of matching Domains",
            response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The DomainQuery to use to filter count results", required = true) DomainQuery query) {
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
     * @param scopeId  The {@link ScopeId} of the requested {@link Domain}.
     * @param domainId The id of the requested {@link Domain}.
     * @return The requested Domain object.
     */
    @ApiOperation(value = "Get a Domain", notes = "Returns the Domain specified by the \"domainId\" path parameter.", response = Domain.class)
    @GET
    @Path("{domainId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Domain find(
            @ApiParam(value = "The ScopeId of the requested Domain.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
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
