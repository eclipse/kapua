/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.shared.model.user;

import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser.GwtUserStatus;

import java.io.Serializable;
import java.util.Date;

public class GwtUserCreator implements Serializable {

    private static final long serialVersionUID = -7786687388389046845L;

    private String scopeId;
    private String username;
    private String password;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String permissions;
    private Date expirationDate;
    private GwtUserStatus userStatus;

    private boolean isAdministrator;

    public GwtUserCreator() {
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String accountId) {
        this.scopeId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public GwtUserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(GwtUserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserStatus(String userStatus) {
        setUserStatus(GwtUserStatus.valueOf(userStatus));
    }
}
