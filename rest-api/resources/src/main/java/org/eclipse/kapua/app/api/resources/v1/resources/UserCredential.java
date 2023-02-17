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
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.user.PasswordChangeRequest;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.service.authentication.user.UserCredentialsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("{scopeId}/user/credentials")
public class UserCredential {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UserCredentialsService userCredentialsService = locator.getService(UserCredentialsService.class);


    /**
     * Change the user password
     *
     * @param scopeId               The {@link ScopeId} to use in the request.
     * @param passwordChangeRequest The {@link PasswordChangeRequest} represents the changing
     * @return The updated {@link Credential}
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     */
    @Path("/password")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Credential newPassword(@PathParam("scopeId") ScopeId scopeId, PasswordChangeRequest passwordChangeRequest) throws KapuaException {
        return userCredentialsService.changePasswordRequest(passwordChangeRequest);
    }


    /**
     * Reset the password of a {@link Credential}.
     *
     * @param scopeId              The {@link ScopeId} of the {@link Credential} to reset.
     * @param credentialId         The id of the Credential to reset the password.
     * @param passwordResetRequest Request for resetting credential password
     * @return The updated credential.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 2.0.0
     */
    @POST
    @Path("{credentialId}/_reset")
    public Credential unlockCredential(
        @PathParam("scopeId") ScopeId scopeId,
        @PathParam("credentialId") EntityId credentialId,
        PasswordResetRequest passwordResetRequest) throws KapuaException {
        return userCredentialsService.resetPassword(scopeId, credentialId, passwordResetRequest);
    }
}
