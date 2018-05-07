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
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtAccessPermission extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 1330881042880793119L;

    public String getAccessInfoId() {
        return get("accessInfoId");
    }

    public void setAccessInfoId(String accessInfoId) {
        set("accessInfoId", accessInfoId);
    }

    public String getPermissionDomain() {
        return get("permissionDomain");
    }

    public void setPermissionDomain(String permissionDomain) {
        set("permissionDomain", permissionDomain == null ? "ALL" : permissionDomain);
    }

    public String getPermissionAction() {
        return get("permissionAction");
    }

    public void setPermissionAction(String permissionAction) {
        set("permissionAction", permissionAction == null ? "ALL" : permissionAction);
    }

    public String getPermissionTargetScopeId() {
        return get("permissionTargetScopeId");
    }

    public void setPermissionTargetScopeId(String permissionTargetScopeId) {
        set("permissionTargetScopeId", permissionTargetScopeId == null ? "ALL" : permissionTargetScopeId);
    }

    public String getPermissionTargetScopeIdByName() {
        return get("permissionTargetScopeIdByName");
    }

    public void setPermissionTargetScopeIdByName(String permissionTargetScopeIdByName) {
        set("permissionTargetScopeIdByName", permissionTargetScopeIdByName == null ? "ALL" : permissionTargetScopeIdByName);
    }

    public String getPermissionGroupId() {
        return get("permissionGroupId");
    }

    public void setPermissionGroupId(String permissionGroupId) {
        set("permissionGroupId", permissionGroupId == null ? "ALL" : permissionGroupId);
    }

    public boolean getPermissionForwardable() {
        return get("permissionForwardable");
    }

    public void setPermissionForwardable(boolean forwardable) {
        set("permissionForwardable", forwardable);
    }

    /**
     * Returns the string representation for this {@link GwtPermission} in the following format:
     * <p>
     * {domain}[:{action}[:{targetScopeId]]
     * </p>
     *
     * @return the formatted string representation of this {@link GwtPermission}
     * @since 1.0.0
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getPermissionDomain());

        sb.append(":");
        if (getPermissionAction() != null) {
            sb.append(getPermissionAction());
        } else {
            sb.append("*");
        }

        sb.append(":");
        if (getPermissionTargetScopeId() != null) {
            sb.append(getPermissionTargetScopeId());
        } else {
            sb.append("*");
        }

        sb.append(":");
        if (getPermissionGroupId() != null) {
            sb.append(getPermissionGroupId());
        } else {
            sb.append("*");
        }

        return sb.toString();
    }
}
