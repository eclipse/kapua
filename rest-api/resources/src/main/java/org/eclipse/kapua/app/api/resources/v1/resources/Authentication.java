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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.LoginInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
public class Authentication extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AuthenticationService authenticationService = locator.getService(AuthenticationService.class);

    /**
     * Authenticates an user with username and password and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @param authenticationCredentials The username and password authentication credential of a user.
     * @return The authentication token
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("user")
    public AccessToken loginUsernamePassword(UsernamePasswordCredentials authenticationCredentials) throws Exception {
        return login(authenticationCredentials);
    }

    /**
     * Authenticates an user with a api key and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @param authenticationCredentials The API KEY authentication credential of a user.
     * @return The authentication token
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("apikey")
    public AccessToken loginApiKey(ApiKeyCredentials authenticationCredentials) throws Exception {
        return login(authenticationCredentials);
    }

    /**
     * Authenticates an user with JWT and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @param authenticationCredentials The JWT authentication credential of a user.
     * @return The authentication token
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("jwt")
    public AccessToken loginJwt(JwtCredentials authenticationCredentials) throws Exception {
        return login(authenticationCredentials);
    }

    /**
     * Invalidates the AccessToken related to this session.
     * All subsequent calls will end up with a HTTP 401.
     * A new login is required after this call to make other requests.
     *
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("logout")

    public Response logout() throws Exception {
        authenticationService.logout();

        return returnOk();
    }

    /**
     * Refreshes an expired {@link AccessToken}. Both the current AccessToken and the Refresh token will be invalidated.
     * If also the Refresh token is expired, the user will have to restart with a new login.
     *
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("refresh")
    public AccessToken refresh(RefreshTokenCredentials refreshTokenCredentials) throws Exception {
        return authenticationService.refreshAccessToken(refreshTokenCredentials.getTokenId(), refreshTokenCredentials.getRefreshToken());
    }

    /**
     * Authenticates the given {@link LoginCredentials}.
     *
     * @param loginCredentials The {@link LoginCredentials} to validate.
     * @return The Authentication token
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.1.0
     */
    private AccessToken login(LoginCredentials loginCredentials) throws Exception {
        return authenticationService.login(loginCredentials);
    }

    /**
     * Gets a {@link LoginInfo} object
     *
     * @return A {@link LoginInfo} object containing all the permissions and the {@link AccessToken} for the current session
     * @throws KapuaException
     */

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("info")
    public LoginInfo loginInfo() throws KapuaException {
        return authenticationService.getLoginInfo();
    }
}
