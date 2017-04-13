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
 *******************************************************************************/
package org.eclipse.kapua.test.user;

import java.math.BigInteger;
import java.util.Date;
import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

public class UserMock implements User
{
    private static long longId = 1;
    private KapuaEid id;
    private KapuaId scopeId;
    private String name;
    private UserType userType;
    private String externalId;

    public UserMock(KapuaId scopeId, String name)
    {
        this.id = new KapuaEid(BigInteger.valueOf(longId++));
        this.scopeId = scopeId;
        this.name = name;
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Date getModifiedOn()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaId getModifiedBy()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getOptlock()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setOptlock(int optlock)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Properties getEntityAttributes()
        throws KapuaException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityAttributes(Properties props)
        throws KapuaException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Properties getEntityProperties()
        throws KapuaException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityProperties(Properties props)
        throws KapuaException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaId getId()
    {
        return this.id;
    }

    @Override
    public KapuaId getScopeId()
    {
        return this.scopeId;
    }

    @Override
    public Date getCreatedOn()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaId getCreatedBy()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserStatus getStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStatus(UserStatus status)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDisplayName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDisplayName(String displayName)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getEmail()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEmail(String email)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPhoneNumber()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPhoneNumber(String phoneNumber)
    {
        // TODO Auto-generated method stub

    }

	@Override
	public void setId(KapuaId id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public UserType getUserType() {
        return userType;
    }

    @Override
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
