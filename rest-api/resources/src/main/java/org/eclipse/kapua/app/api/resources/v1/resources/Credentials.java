/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialAttributes;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;

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

@Path("{scopeId}/credentials")
public class Credentials extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialService credentialService = locator.getService(CredentialService.class);
    private final CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

    /**
     * Gets the {@link Credential} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link CredentialListResult} of all the credentials associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public CredentialListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("userId") EntityId userId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        CredentialQuery query = credentialFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (userId != null) {
            andPredicate.and(query.attributePredicate(CredentialAttributes.USER_ID, userId));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link CredentialQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link CredentialQuery} to use to filter results.
     * @return The {@link CredentialListResult} of all the result matching the given {@link CredentialQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CredentialListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            CredentialQuery query) throws Exception {
        query.setScopeId(scopeId);

        return credentialService.query(query);
    }

    /**
     * Counts the results with the given {@link CredentialQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link CredentialQuery} to use to filter results.
     * @return The count of all the result matching the given {@link CredentialQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            CredentialQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(credentialService.count(query));
    }

    /**
     * Creates a new Credential based on the information provided in CredentialCreator
     * parameter.
     *
     * @param scopeId           The {@link ScopeId} in which to create the {@link Credential}
     * @param credentialCreator Provides the information for the new Credential to be created.
     * @return The newly created Credential object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credential create(
            @PathParam("scopeId") ScopeId scopeId,
            CredentialCreator credentialCreator) throws Exception {
        credentialCreator.setScopeId(scopeId);

        return credentialService.create(credentialCreator);
    }

    /**
     * Returns the Credential specified by the "credentialId" path parameter.
     *
     * @param scopeId      The {@link ScopeId} of the requested {@link Credential}.
     * @param credentialId The id of the requested Credential.
     * @return The requested Credential object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{credentialId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Credential find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("credentialId") EntityId credentialId) throws Exception {
        Credential credential = credentialService.find(scopeId, credentialId);

        if (credential == null) {
            throw new KapuaEntityNotFoundException(Credential.TYPE, credentialId);
        }

        return credential;
    }

    /**
     * Updates the Credential based on the information provided in the Credential parameter.
     *
     * @param credential The modified Credential whose attributed need to be updated.
     * @return The updated credential.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Path("{credentialId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credential update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("credentialId") EntityId credentialId,
            Credential credential) throws Exception {
        credential.setScopeId(scopeId);
        credential.setId(credentialId);

        return credentialService.update(credential);
    }

    /**
     * Deletes the Credential specified by the "credentialId" path parameter.
     *
     * @param credentialId The id of the Credential to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{credentialId}")
    public Response deleteCredential(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("credentialId") EntityId credentialId) throws Exception {
        credentialService.delete(scopeId, credentialId);

        return returnOk();
    }

    /**
     * Unlocks a {@link Credential} that has been locked due to a lockout policy
     *
     * @param credentialId The id of the Credential to be unlocked.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("{credentialId}/unlock")
    public Response unlockCredential(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("credentialId") EntityId credentialId) throws Exception {
        credentialService.unlock(scopeId, credentialId);

        return returnOk();
    }

}
