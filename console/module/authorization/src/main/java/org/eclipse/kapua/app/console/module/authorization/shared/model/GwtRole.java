/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import java.util.Set;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtRole extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 5857520420217101882L;

    private static final String DESCRIPTION = "description";

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

    public String getDescription() {
        return get(DESCRIPTION);
    }

    public String getUnescapedDescription() {
        return (String) getUnescaped(DESCRIPTION);
    }

    public void setDescription(String description) {
        set(DESCRIPTION, description);
    }

    public Set<GwtRolePermission> getPermissions() {
        return rolePermissions;
    }

    public void setPermissions(Set<GwtRolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

}
