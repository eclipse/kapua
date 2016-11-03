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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Set;

import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;

public class GwtSession extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -4511854889803351914L;

    // Console info
    private String m_version;
    private String m_buildVersion;
    private String m_buildNumber;

    // User info
    private GwtUser m_gwtUser;
    private GwtAccount m_gwtAccount;
    private GwtAccount m_rootAccount;
    private GwtAccount m_selectedAccount;
    private Set<String> m_permissions;

    // Static loaded permission
    private boolean hasAccountCreatePermission;
    private boolean hasAccountReadPermission;
    private boolean hasAccountUpdatePermission;
    private boolean hasAccountDeletePermission;
    private boolean hasAccountAllPermission;

    private boolean hasDeviceCreatePermission;
    private boolean hasDeviceReadPermission;
    private boolean hasDeviceUpdatePermission;
    private boolean hasDeviceDeletePermission;
    private boolean hasDeviceManagePermission;

    private boolean hasDataReadPermission;

    private boolean hasUserCreatePermission;
    private boolean hasUserReadPermission;
    private boolean hasUserUpdatePermission;
    private boolean hasUserDeletePermission;

    public GwtSession() {
    }

    public GwtUser getGwtUser() {
        return m_gwtUser;
    }

    public void setGwtUser(GwtUser gwtUser) {
        this.m_gwtUser = gwtUser;
    }

    public GwtAccount getGwtAccount() {
        return m_gwtAccount;
    }

    public void setGwtAccount(GwtAccount gwtAccount) {
        this.m_gwtAccount = gwtAccount;
    }

    public void setRootAccount(GwtAccount rootAccount) {
        this.m_rootAccount = rootAccount;
    }

    public GwtAccount getRootAccount() {
        return m_rootAccount;
    }

    public void setSelectedAccount(GwtAccount selectedAccount) {
        this.m_selectedAccount = selectedAccount;
    }

    public GwtAccount getSelectedAccount() {
        return m_selectedAccount;
    }

    public Set<String> getPermissions() {
        return m_permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.m_permissions = permissions;
    }

    public String getVersion() {
        return m_version;
    }

    public void setVersion(String version) {
        m_version = version;
    }

    public String getBuildVersion() {
        return m_buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        m_buildVersion = buildVersion;
    }

    public String getBuildNumber() {
        return m_buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        m_buildNumber = buildNumber;
    }

    public boolean hasAccountCreatePermission() {
        return hasAccountCreatePermission;
    }

    public void setAccountCreatePermission(boolean value) {
        this.hasAccountCreatePermission = value;
    }

    public boolean hasAccountReadPermission() {
        return hasAccountReadPermission;
    }

    public void setAccountReadPermission(boolean value) {
        this.hasAccountReadPermission = value;
    }

    public boolean hasAccountUpdatePermission() {
        return hasAccountUpdatePermission;
    }

    public void setAccountUpdatePermission(boolean value) {
        this.hasAccountUpdatePermission = value;
    }

    public boolean hasAccountDeletePermission() {
        return hasAccountDeletePermission;
    }

    public void setAccountDeletePermission(boolean value) {
        this.hasAccountDeletePermission = value;
    }

    public boolean hasAccountAllPermission() {
        return hasAccountAllPermission;
    }

    public void setAccountAllPermission(boolean hasAccountAll) {
        this.hasAccountAllPermission = hasAccountAll;
    }

    public boolean hasUserReadPermission() {
        return hasUserReadPermission;
    }

    public void setUserReadPermission(boolean value) {
        this.hasUserReadPermission = value;
    }

    public boolean hasDataReadPermission() {
        return hasDataReadPermission;
    }

    public void setDataReadPermission(boolean value) {
        this.hasDataReadPermission = value;
    }

    public boolean hasUserCreatePermission() {
        return hasUserCreatePermission;
    }

    public void setUserCreatePermission(boolean hasUserCreatePermission) {
        this.hasUserCreatePermission = hasUserCreatePermission;
    }

    public boolean hasUserUpdatePermission() {
        return hasUserUpdatePermission;
    }

    public void setUserUpdatePermission(boolean hasUserUpdatePermission) {
        this.hasUserUpdatePermission = hasUserUpdatePermission;
    }

    public boolean hasUserDeletePermission() {
        return hasUserDeletePermission;
    }

    public void setUserDeletePermission(boolean hasUserDeletePermission) {
        this.hasUserDeletePermission = hasUserDeletePermission;
    }

    public boolean hasDeviceCreatePermission() {
        return hasDeviceCreatePermission;
    }

    public void setDeviceCreatePermission(boolean hasDeviceCreatePermission) {
        this.hasDeviceCreatePermission = hasDeviceCreatePermission;
    }

    public boolean hasDeviceReadPermission() {
        return hasDeviceReadPermission;
    }

    public void setDeviceReadPermission(boolean hasDeviceReadPermission) {
        this.hasDeviceReadPermission = hasDeviceReadPermission;
    }

    public boolean hasDeviceUpdatePermission() {
        return hasDeviceUpdatePermission;
    }

    public void setDeviceUpdatePermission(boolean hasDeviceUpdatePermission) {
        this.hasDeviceUpdatePermission = hasDeviceUpdatePermission;
    }

    public boolean hasDeviceDeletePermission() {
        return hasDeviceDeletePermission;
    }

    public void setDeviceDeletePermission(boolean hasDeviceDeletePermission) {
        this.hasDeviceDeletePermission = hasDeviceDeletePermission;
    }

    public boolean hasDeviceManagePermission() {
        return hasDeviceManagePermission;
    }

    public void setDeviceManagePermission(boolean hasDeviceManagePermission) {
        this.hasDeviceManagePermission = hasDeviceManagePermission;
    }
}
