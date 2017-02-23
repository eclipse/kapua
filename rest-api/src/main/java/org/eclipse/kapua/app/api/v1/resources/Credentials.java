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
 *
 *******************************************************************************/

package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialImpl;
import org.eclipse.kapua.service.user.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Credentials")
@Path("/credentials")
public class Credentials extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialService credentialService = locator.getService(CredentialService.class);
    private final CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

    /**
     * Returns the list of all the credentials for the given user.
     *
     * @param id
     *            The {@link Credential} id.
     * @return The list of requested Credential objects.
     */
    @ApiOperation(value = "Get the Credential for the given id", notes = "Returns the credential for the given id.", response = Credential.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{id}")
    public Credential getCredential(@PathParam("id") String id) {
        Credential credential = credentialFactory.newCredential();
        try {
            KapuaId credentialKapuaId = KapuaEid.parseCompactId(id);
            KapuaSession session = KapuaSecurityUtils.getSession();
            credential = credentialService.find(session.getScopeId(), credentialKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return credential;
    }

    /**
     * Returns the list of all the credentials for the given user.
     *
     * @param userId
     *            The {@link User} id.
     * @return The list of requested Credential objects.
     */
    @ApiOperation(value = "Get the Credentials for the given user", notes = "Returns the list of all the credentials for the given user.", response = Credential.class, responseContainer = "CredentialListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("list/{userId}")
    public CredentialListResult getCredentialsForUser(@PathParam("userId") String userId) {
        CredentialListResult credentialsListResult = credentialFactory.newCredentialListResult();
        try {
            KapuaId id = KapuaEid.parseCompactId(userId);
            KapuaSession session = KapuaSecurityUtils.getSession();
            credentialsListResult = (CredentialListResult) credentialService.findByUserId(session.getScopeId(), id);
        } catch (Throwable t) {
            handleException(t);
        }
        return credentialsListResult;
    }

    /**
     * Creates a new Credential based on the information provided in CredentialCreator parameter.
     *
     * @param credentialCreator
     *            Provides the information for the new Credential to be created.
     * @return The newly created Credential object.
     */
    @ApiOperation(value = "Create a Credential", notes = "Creates a new Credential based on the information provided in CredentialCreator parameter.", response = Credential.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Credential createCredential(
            @ApiParam(value = "Provides the information for the new Credential to be created", required = true) CredentialCreator credentialCreator) {

        Credential credential = null;
        try {
            credentialCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            credential = credentialService.create(credentialCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(credential);
    }

    /**
     * Deletes the Credential specified by the "credentialId" path parameter.
     *
     * @param credentialId
     *            The id of the Credential to be deleted.
     * @return Return HTTP 200 if the operation has completed successfully.
     */
    @ApiOperation(value = "Delete a Credential", notes = "Deletes a credential based on the information provided in credentialId parameter.")
    @DELETE
    @Path("{credentialId}")
    public Response deleteCredential(
            @ApiParam(value = "The id of the Credential to be deleted", required = true) @PathParam("credentialId") String credentialId) {
        try {
            KapuaId credentialKapuaId = KapuaEid.parseCompactId(credentialId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            credentialService.delete(scopeId, credentialKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Updates a credential based on the information provided in Credential parameter.
     *
     * @param credential
     *            Provides the information to update the credential.
     * @return The updated Credential object.
     */
    @ApiOperation(value = "Update a Credential", notes = "Updates a credential based on the information provided in Credential parameter.", response = Credential.class)
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Credential updateCredential(
            @ApiParam(value = "Provides the information to update the credential", required = true) Credential credential) {
        Credential updatedCredential = null;
        try {
            ((CredentialImpl) credential).setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            updatedCredential = credentialService.update(credential);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(updatedCredential);
    }
}
