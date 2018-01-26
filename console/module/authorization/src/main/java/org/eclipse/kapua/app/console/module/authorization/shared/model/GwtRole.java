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

import java.util.Set;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtRole extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 5857520420217101882L;

    Set<GwtRolePermission> rolePermissions;

    public GwtRole() {
        super();
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public Set<GwtRolePermission> getPermissions() {
        return rolePermissions;
    }

    public void setPermissions(Set<GwtRolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

}
