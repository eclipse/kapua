/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.authorization.permission.Permission;

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
    private String accountPath;
    private String rootAccountId;
    private String rootAccountName;

    private String selectedAccountId;
    private String selectedAccountName;
    private String selectedAccountPath;

    private String userId;
    private String userName;
    private String userDisplayName;

    private List<GwtSessionPermission> sessionPermissions = new ArrayList<GwtSessionPermission>();
    private Map<GwtSessionPermission, Boolean> checkedPermissionsCache = new HashMap<GwtSessionPermission, Boolean>();

    /**
     * Is UI form dirty and needs save.
     */
    private boolean formDirty;

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

    public String getAccountPath() {
        return accountPath;
    }

    public void setAccountPath(String accountPath) {
        this.accountPath = accountPath;
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

    public String getSelectedAccountPath() {
        return selectedAccountPath;
    }

    public void setSelectedAccountPath(String selectedAccountPath) {
        this.selectedAccountPath = selectedAccountPath;
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

    /**
     * Checks that the current {@link GwtSession} has the given session.
     * This methods uses {@link #hasPermission(GwtSessionPermission)} instantiating the actual {@link GwtSessionPermission}.
     *
     * @param domain      The domain to check
     * @param action      The {@link GwtSessionPermissionAction} to check
     * @param targetScope The {@link GwtSessionPermissionScope} to check
     * @return {@code true} if the current {@link GwtSession} has the permission, {@false} otherwise
     * @since 1.0.0
     */
    public boolean hasPermission(String domain, GwtSessionPermissionAction action, GwtSessionPermissionScope targetScope) {
        return hasPermission(new GwtSessionPermission(domain, action, targetScope));
    }

    /**
     * Checks that the current {@link GwtSession} has the given {@link GwtSessionPermission}.
     * <p>
     * This check is done simulating the permission check performed by the {@link org.eclipse.kapua.service.authorization.AuthorizationService#isPermitted(Permission)}.
     * This does not introduces any security risk, since it will only allow to see/have access to certain elements of the UI while service access check is still performed on each call.
     * <p>
     * After the check, the result is cached to allow faster check for subsequent check for the same permission.
     * </p>
     *
     * @param permissionToCheck The {@link GwtSessionPermission} to check
     * @return {@code true} if the current {@link GwtSession} has the permission, {@false} otherwise
     * @since 1.0.0
     */
    public boolean hasPermission(GwtSessionPermission permissionToCheck) {
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

    /**
     * Is UI form dirty and needs confirmation to switch menu.
     *
     * @return true if user needs to confirm menu change.
     */
    public boolean isFormDirty() {
        return formDirty;
    }

    /**
     * Set user interface into dirty state.
     *
     * @param formDirty true if user will need to confirm menu change.
     */
    public void setFormDirty(boolean formDirty) {
        this.formDirty = formDirty;
    }

    /**
     * This methods simulates the check that is performed by the {@link org.eclipse.kapua.service.authorization.AuthorizationService#isPermitted(Permission)}.
     * {@link Permission#getForwardable()} property is supported in a different way, but produces the same results.
     *
     * @param permissionToCheck The {@link GwtSessionPermission} to check
     * @return {@code true} if the current {@link GwtSession} has the permission, {@false} otherwise
     * @since 1.0.0
     */
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
