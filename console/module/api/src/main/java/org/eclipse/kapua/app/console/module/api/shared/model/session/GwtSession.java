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
package org.eclipse.kapua.app.console.module.api.shared.model.session;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GwtSession extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -4511854889803351914L;

    // Console info
    private String version;
    private String buildVersion;
    private String buildNumber;

    // User info
    private String accountId;
    private String rootAccountId;
    private String rootAccountName;

    private String selectedAccountId;
    private String selectedAccountName;

    private String userId;
    private String userName;
    private String userDisplayName;

    private List<GwtSessionPermission> sessionPermissions = new ArrayList<GwtSessionPermission>();
    private Map<GwtSessionPermission, Boolean> checkedPermissionsCache = new HashMap<GwtSessionPermission, Boolean>();

    public GwtSession() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setRootAccountId(String rootAccountId) {
        this.rootAccountId = rootAccountId;
    }

    public String getRootAccountId() {
        return rootAccountId;
    }

    public void setSelectedAccountId(String selectedAccountId) {
        this.selectedAccountId = selectedAccountId;
    }

    public String getSelectedAccountId() {
        return selectedAccountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getRootAccountName() {
        return rootAccountName;
    }

    public void setRootAccountName(String rootAccountName) {
        this.rootAccountName = rootAccountName;
    }

    public String getSelectedAccountName() {
        return selectedAccountName;
    }

    public void setSelectedAccountName(String selectedAccountName) {
        this.selectedAccountName = selectedAccountName;
    }

    public List<GwtSessionPermission> getSessionPermissions() {
        if (sessionPermissions == null) {
            sessionPermissions = new ArrayList<GwtSessionPermission>();
        }

        return sessionPermissions;
    }

    public void setSessionPermissions(List<GwtSessionPermission> sessionPermissions) {
        this.sessionPermissions = sessionPermissions;
    }

    public void addSessionPermission(GwtSessionPermission permission) {
        getSessionPermissions().add(permission);
    }

    public boolean hasPermission(String domain, GwtSessionPermissionAction action, GwtSessionPermissionScope gwtSessionPermissionScope) {
        GwtSessionPermission permissionToCheck = new GwtSessionPermission(domain, action, gwtSessionPermissionScope);

        // Check cache
        Boolean cachedResult = checkedPermissionsCache.get(permissionToCheck);
        if (cachedResult != null) {
            return cachedResult.booleanValue();
        }

        // Check permission
        boolean permitted = isPermitted(permissionToCheck);

        // Set cached value
        checkedPermissionsCache.put(permissionToCheck, permitted);

        // Return it
        return permitted;
    }

    private boolean isPermitted(GwtSessionPermission permissionToCheck) {

        for (GwtSessionPermission gsp : getSessionPermissions()) {
            if (gsp.getDomain() == null || gsp.getDomain().equals(permissionToCheck.getDomain())) {
                if (gsp.getAction() == null || gsp.getAction().equals(permissionToCheck.getAction())) {

                    GwtSessionPermissionScope permissionToCheckScope = permissionToCheck.getPermissionScope();

                    boolean check = false;
                    switch (gsp.getPermissionScope()) {
                    case ALL:
                        check = true;
                        break;
                    case CHILDREN:
                        check = (GwtSessionPermissionScope.CHILDREN.equals(permissionToCheckScope) ||
                                GwtSessionPermissionScope.SELF.equals(permissionToCheckScope));
                        break;
                    case SELF:
                        check = GwtSessionPermissionScope.SELF.equals(permissionToCheckScope);
                        break;
                    }

                    if (check) {
                        return check;
                    }
                }
            }
        }

        return false;
    }
}
