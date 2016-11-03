package org.eclipse.kapua.app.console.shared.model.authorization;

import java.util.Set;

import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;

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