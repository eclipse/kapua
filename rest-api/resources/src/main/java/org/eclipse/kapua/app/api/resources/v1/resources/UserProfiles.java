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
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.service.user.profile.UserProfile;
import org.eclipse.kapua.service.user.profile.UserProfileService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 @deprecated
 accidentally exposed under:
 /{scopeId}/user/....
 Where the scopeId has no meaning of the current user (the one from the session will always be used)
 Remove the match with /{scopeId}/... in the next release
 */
@Path("{scopeId: (\\w+)?}{path:|/}user/profile")
public class UserProfiles extends AbstractKapuaResource {

    @Inject
    public UserProfileService userProfileService;

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeUserProfile(UserProfile userProfile) throws KapuaException {
        userProfileService.changeUserProfile(userProfile);
        return returnOk();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfile getUserProfile() throws KapuaException {
        return userProfileService.getUserProfile();
    }
}

