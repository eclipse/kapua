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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;

public class GwtRolePermission extends GwtEntityModel {

    private static final long serialVersionUID = 6331197556606146242L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("actionEnum".equals(property)) {
            return (X) (GwtAction.valueOf(getAction()));
        } else {
            return super.get(property);
        }
    }

    public GwtRolePermission() {
        super();
    }

    public String getRoleId() {
        return get("roleId");
    }

    public void setRoleId(String roleId) {
        set("roleId", roleId);
    }

    /**
     * @return the domain of this permission
     * @since 1.0.0
     */
    public String getDomain() {
        return get("domain");
    }

    public void setDomain(String domain) {
        set("domain", domain == null ? "ALL" : domain);
    }

    /**
     * @return the action of this permission
     * @since 1.0.0
     */
    public String getAction() {
        return get("action");
    }

    public GwtAction getActionEnum() {
        return get("actionEnum");
    }

    public void setAction(String action) {
        set("action", action == null ? "ALL" : action);
    }

    /**
     * @return the target scope id of this permission
     * @since 1.0.0
     */
    public String getTargetScopeId() {
        return get("targetScopeId");
    }

    public void setTargetScopeId(String targetScopeId) {
        set("targetScopeId", targetScopeId == null ? "ALL" : targetScopeId);
    }

    public String getTargetScopeIdByName() {
        return get("targetScopeIdByName");
    }

    public void setTargetScopeIdByName(String targetScopeIdByName) {
        set("targetScopeIdByName", targetScopeIdByName == null ? "ALL" : targetScopeIdByName);
    }

    /**
     * @return the group id of this permission
     * @since 1.0.0
     */
    public String getGroupId() {
        return get("groupId");
    }

    public void setGroupId(String groupId) {
        set("groupId", groupId == null ? "ALL" : groupId);
    }

    public boolean getForwardable() {
        return get("forwardable");
    }

    public void setForwardable(boolean forwardable) {
        set("forwardable", forwardable);
    }

}
