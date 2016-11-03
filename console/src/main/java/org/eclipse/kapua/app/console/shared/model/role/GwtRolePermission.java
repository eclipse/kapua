package org.eclipse.kapua.app.console.shared.model.role;

import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;

public class GwtRolePermission extends GwtEntityModel {

    private static final long serialVersionUID = 6331197556606146242L;

    public GwtRolePermission() {
        super();
    }

    public GwtPermission getPermission() {
        return get("permission");
    }

    public void setPermission(GwtPermission permission) {
        set("permission", permission);
    }
}