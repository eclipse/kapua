package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Authentication")
@Path("/authentication")
public class Authentication extends AbstractKapuaResource{
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
    
    /**
     * Authenticates an user with username and password and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @return The authentication token
     */
    @ApiOperation(value = "Authenticate an user",
            notes = "Authenticates an user with username and password and returns " +
                    "the authentication token to be used in subsequent REST API calls.",
            response = AccessToken.class)
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessToken getAccounts(UsernamePasswordCredentials authenticationCredentials) {
        AccessToken accessToken = null;
        try {
            accessToken = authenticationService.login(authenticationCredentials);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessToken;
    }
}
