package org.eclipse.kapua.test.user;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserType;

public class UserCreatorMock implements UserCreator
{
    
    private String name;
    private KapuaId scopeId;
    private String displayName;
    private String email;
    private String phoneNumber;
    private UserType userType;
    
    public UserType getUserType() {
        return userType;
    }

    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    
    public String getExternalId() {
        return externalId;
    }

    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    private String externalId;
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public KapuaId getScopeId()
    {
        return scopeId;
    }
    
    @Override
    public void setScopeId(KapuaId scopeId)
    {
        this.scopeId = scopeId;
    }
    
    @Override
    public String getDisplayName()
    {
        return displayName;
    }
    
    @Override
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    @Override
    public String getEmail()
    {
        return email;
    }
    
    @Override
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    @Override
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    @Override
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
