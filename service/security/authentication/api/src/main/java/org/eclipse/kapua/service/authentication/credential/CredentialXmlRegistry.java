package org.eclipse.kapua.service.authentication.credential;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class CredentialXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final CredentialFactory factory = locator.getFactory(CredentialFactory.class);
    
    /**
     * Creates a new credential instance
     * 
     * @return
     */
    public Credential newCredential()
    {
        return factory.newCredential();
    }
    
    /**
     * Creates a new credential list result instance
     * 
     * @return
     */
    public CredentialListResult newCredentialListResult()
    {
        return factory.newCredentialListResult();
    }
    
    /**
     * Creates a new credential creator instance
     * 
     * @return
     */
    public CredentialCreator newCredentialCreator()
    {
        return factory.newCreator(null, null, null, null);
    }
}
