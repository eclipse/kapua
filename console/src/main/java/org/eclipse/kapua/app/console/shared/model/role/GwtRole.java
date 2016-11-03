package org.eclipse.kapua.app.console.shared.model.role;

import java.util.Set;

import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;

public class GwtRole extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 5857520420217101882L;

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
        return get("permissions");
    }

    public void setPermissions(Set<GwtRolePermission> rolePermissions) {
        set("permissions", rolePermissions);
    }
}