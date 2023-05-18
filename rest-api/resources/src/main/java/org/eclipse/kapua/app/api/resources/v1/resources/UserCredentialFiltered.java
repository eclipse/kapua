/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.model.CountResult;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/{scopeId}/user/{userId}/credentials")
public class UserCredentialFiltered extends AbstractKapuaResource {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialService credentialService = locator.getService(CredentialService.class);
    private final CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);


    /**
     * Gets the {@link Credential} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param userId  The {@link EntityId} for which search results.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link CredentialListResult} of all the credentials associated to the current selected scope.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public CredentialListResult getAll(
        @PathParam("scopeId") ScopeId scopeId,
        @PathParam("userId") EntityId userId,
        @QueryParam("offset") @DefaultValue("0") int offset,
        @QueryParam("limit") @DefaultValue("50") int limit) throws KapuaException {
        CredentialQuery query = credentialFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(CredentialAttributes.USER_ID, userId));
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return credentialService.query(query);
    }


    /**
     * Counts the results with the given {@link CredentialQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to count results.
     * @param userId  The {@link EntityId} for which count results.
     * @param query   The {@link CredentialQuery} to use to filter results.
     * @return The count of all the result matching the given {@link CredentialQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
        @PathParam("scopeId") ScopeId scopeId,
        @PathParam("userId") EntityId userId,
        CredentialQuery query) throws KapuaException {
        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(CredentialAttributes.USER_ID, userId));
        query.setPredicate(andPredicate);

        return new CountResult(credentialService.count(query));
    }


    /**
     * Creates a new Credential based on the information provided in CredentialCreator
     * parameter.
     *
     * @param scopeId           The {@link ScopeId} in which to create the {@link Credential}.
     * @param userId            The {@link EntityId} for which create the {@link Credential}.
     * @param credentialCreator Provides the information for the new Credential to be created.
     * @return The newly created Credential object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(
        @PathParam("scopeId") ScopeId scopeId,
        @PathParam("userId") EntityId userId,
        CredentialCreator credentialCreator) throws KapuaException {
        credentialCreator.setScopeId(scopeId);
        credentialCreator.setUserId(userId);

        return returnCreated(credentialService.create(credentialCreator));
    }
}
