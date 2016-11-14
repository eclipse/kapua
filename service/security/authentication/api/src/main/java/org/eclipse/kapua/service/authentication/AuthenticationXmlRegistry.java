package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.locator.KapuaLocator;

public class AuthenticationXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UsernamePasswordTokenFactory usernamePasswordTokenFactory = locator.getFactory(UsernamePasswordTokenFactory.class);
    
    /**
     * Creates a new UsernamePasswordToken instance
     * 
     * @return
     */
    public UsernamePasswordToken newUsernamePasswordToken()
    {
        return usernamePasswordTokenFactory.newInstance(null, null);
    }
}
