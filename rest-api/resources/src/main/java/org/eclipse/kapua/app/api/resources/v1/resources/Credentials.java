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
package org.eclipse.kapua.app.api.resources.v1.resources;

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

import io.swagger.annotations.Authorization;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialPredicates;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Credentials", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/credentials")
public class Credentials extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialService credentialService = locator.getService(CredentialService.class);
    private final CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

    /**
     * Gets the {@link Credential} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link CredentialListResult} of all the credentials associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialSimpleQuery", value = "Gets the Credential list in the scope", notes = "Returns the list of all the credentials associated to the current selected scope.", response = CredentialListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public CredentialListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The optional id to filter results.") @QueryParam("userId") EntityId userId,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50", required = true) @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        CredentialQuery query = credentialFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        if (userId != null) {
            andPredicate.and(new AttributePredicate<>(CredentialPredicates.USER_ID, userId));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link CredentialQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link CredentialQuery} to use to filter results.
     * @return The {@link CredentialListResult} of all the result matching the given {@link CredentialQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialQuery", value = "Queries the Credentials", notes = "Queries the Credentials with the given CredentialQuery parameter returning all matching Credentials", response = CredentialListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CredentialListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The CredentialQuery to use to filter results.", required = true) CredentialQuery query) throws Exception {
        query.setScopeId(scopeId);

        return credentialService.query(query);
    }

    /**
     * Counts the results with the given {@link CredentialQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link CredentialQuery} to use to filter results.
     * @return The count of all the result matching the given {@link CredentialQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialCount", value = "Counts the Credentials", notes = "Counts the Credentials with the given CredentialQuery parameter returning the number of matching Credentials", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The CredentialQuery to use to filter count results", required = true) CredentialQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(credentialService.count(query));
    }

    /**
     * Creates a new Credential based on the information provided in CredentialCreator
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the {@link Credential}
     * @param credentialCreator
     *            Provides the information for the new Credential to be created.
     * @return The newly created Credential object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialCreate", value = "Create a Credential", notes = "Creates a new Credential based on the information provided in CredentialCreator parameter.", response = Credential.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Credential create(
            @ApiParam(value = "The ScopeId in which to create the Credential", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new Credential to be created", required = true) CredentialCreator credentialCreator) throws Exception {
        credentialCreator.setScopeId(scopeId);

        return credentialService.create(credentialCreator);
    }

    /**
     * Returns the Credential specified by the "credentialId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link Credential}.
     * @param credentialId
     *            The id of the requested Credential.
     * @return The requested Credential object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialFind", value = "Get a Credential", notes = "Returns the Credential specified by the \"credentialId\" path parameter.", response = Credential.class)
    @GET
    @Path("{credentialId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Credential find(
            @ApiParam(value = "The ScopeId of the requested Credential.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Credential", required = true) @PathParam("credentialId") EntityId credentialId) throws Exception {
        Credential credential = credentialService.find(scopeId, credentialId);

        if (credential == null) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
        }

        return credential;
    }

    /**
     * Updates the Credential based on the information provided in the Credential parameter.
     *
     * @param credential
     *            The modified Credential whose attributed need to be updated.
     * @return The updated credential.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialUpdate", value = "Update an Credential", notes = "Updates a new Credential based on the information provided in the Credential parameter.", response = Credential.class)
    @PUT
    @Path("{credentialId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Credential update(
            @ApiParam(value = "The ScopeId of the requested Credential.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Credential", required = true) @PathParam("credentialId") EntityId credentialId,
            @ApiParam(value = "The modified Credential whose attributed need to be updated", required = true) Credential credential) throws Exception {
        credential.setScopeId(scopeId);
        credential.setId(credentialId);

        return credentialService.update(credential);
    }

    /**
     * Deletes the Credential specified by the "credentialId" path parameter.
     *
     * @param credentialId
     *            The id of the Credential to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "credentialDelete", value = "Delete a Credential", notes = "Deletes the Credential specified by the \"credentialId\" path parameter.")
    @DELETE
    @Path("{credentialId}")
    public Response deleteCredential(
            @ApiParam(value = "The ScopeId of the Credential to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Credential to be deleted", required = true) @PathParam("credentialId") EntityId credentialId) throws Exception {
        credentialService.delete(scopeId, credentialId);

        return returnOk();
    }

}
