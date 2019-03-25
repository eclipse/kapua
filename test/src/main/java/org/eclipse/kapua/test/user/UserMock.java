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
package org.eclipse.kapua.test.user;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Properties;

public class UserMock implements User {

    private static final long serialVersionUID = -5488783213170204341L;

    private static long longId = 1;

    private KapuaEid id;
    private KapuaId scopeId;
    private String name;
    private String displayName;
    private String email;
    private String phoneNumber;
    private UserType userType;
    private String externalId;
    private Date expirationDate;

    public UserMock(KapuaId scopeId, String name) {
        this.id = new KapuaEid(BigInteger.valueOf(longId++));
        this.scopeId = scopeId;
        this.name = name;
    }

    @Override
    public KapuaId getId() {
        return this.id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = KapuaEid.parseKapuaId(id);
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    @Override
    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getModifiedOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getModifiedBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOptlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptlock(int optlock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getEntityAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityAttributes(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getEntityProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityProperties(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreatedOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getCreatedBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserStatus getStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(UserStatus status) {
        throw new UnsupportedOperationException();
    }
}
