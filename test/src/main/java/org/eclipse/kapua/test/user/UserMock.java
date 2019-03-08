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
    private UserType userType;
    private String externalId;
    private Date expirationDate;

    public UserMock(KapuaId scopeId, String name) {
        this.id = new KapuaEid(BigInteger.valueOf(longId++));
        this.scopeId = scopeId;
        this.name = name;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        // Not used
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
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
        // Not used
    }

    @Override
    public Date getModifiedOn() {
        return null;
    }

    @Override
    public KapuaId getModifiedBy() {
        return null;
    }

    @Override
    public int getOptlock() {
        return 0;
    }

    @Override
    public void setOptlock(int optlock) {
        // Not used
    }

    @Override
    public Properties getEntityAttributes() {
        return null;
    }

    @Override
    public void setEntityAttributes(Properties props) {
        // Not used
    }

    @Override
    public Properties getEntityProperties() {
        return null;
    }

    @Override
    public void setEntityProperties(Properties props) {
        // Not used
    }

    @Override
    public KapuaId getId() {
        return this.id;
    }

    @Override
    public void setId(KapuaId id) {
        // Not used
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public Date getCreatedOn() {
        return null;
    }

    @Override
    public KapuaId getCreatedBy() {
        return null;
    }

    @Override
    public UserStatus getStatus() {
        return null;
    }

    @Override
    public void setStatus(UserStatus status) {
        // Not used
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {
        // Not used
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) {
        // Not used
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        // Not used
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
}
