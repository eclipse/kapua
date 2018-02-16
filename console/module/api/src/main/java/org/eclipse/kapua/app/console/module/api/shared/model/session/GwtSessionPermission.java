/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

public class GwtSessionPermission extends KapuaBaseModel {

    private String domain;
    private GwtSessionPermissionAction action;
    private GwtSessionPermissionScope permissionScope;

    public GwtSessionPermission() {
        super();
    }

    public GwtSessionPermission(String domain, GwtSessionPermissionAction action, GwtSessionPermissionScope permissionScope) {
        this();

        this.domain = domain;
        this.action = action;
        this.permissionScope = permissionScope;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public GwtSessionPermissionAction getAction() {
        return action;
    }

    public void setAction(GwtSessionPermissionAction action) {
        this.action = action;
    }

    public GwtSessionPermissionScope getPermissionScope() {
        return permissionScope;
    }

    public void setPermissionScope(GwtSessionPermissionScope permissionScope) {
        this.permissionScope = permissionScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GwtSessionPermission that = (GwtSessionPermission) o;

        if (domain != null ? !domain.equals(that.domain) : that.domain != null) {
            return false;
        }
        if (action != that.action) {
            return false;
        }

        return permissionScope == that.permissionScope;
    }

    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (permissionScope != null ? permissionScope.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(domain != null ? domain : "*") //
                .append(":") //
                .append(action != null ? action.name() : "*") //
                .append(":") //
                .append(permissionScope != null ? permissionScope.name() : "null");

        return sb.toString();
    }
}
