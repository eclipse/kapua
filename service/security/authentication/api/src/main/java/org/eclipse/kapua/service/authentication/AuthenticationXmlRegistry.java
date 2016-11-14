package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.locator.KapuaLocator;

public class AuthenticationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UsernamePasswordCredentialsFactory usernamePasswordCredentialsFactory = locator.getFactory(UsernamePasswordCredentialsFactory.class);

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance
     * 
     * @return
     */
    public UsernamePasswordCredentials newUsernamePasswordCredentials() {
        return usernamePasswordCredentialsFactory.newInstance(null, null);
    }
}
