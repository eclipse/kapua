/*******************************************************************************
 * Copyright (c) 2017 Eurotech and others.
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

import java.util.Date;
import java.util.Properties;

public class UserCreatorMock implements UserCreator {

    private String name;
    private KapuaId scopeId;
    private String displayName;
    private String email;
    private String phoneNumber;
    private UserType userType;
    private Date expiarionDate;

    private UserStatus userStatus;

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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Date getExpirationDate() {
        return expiarionDate;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        this.expiarionDate = expirationDate;
    }

    @Override
    public UserStatus getUserStatus() {
        return userStatus;
    }

    @Override
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public Properties getEntityAttributes() throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityAttributes(Properties entityAttributes) throws KapuaException {
        // TODO Auto-generated method stub

    }
}
