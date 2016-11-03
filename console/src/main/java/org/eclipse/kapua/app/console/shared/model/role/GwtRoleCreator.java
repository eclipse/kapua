package org.eclipse.kapua.app.console.shared.model.role;

import java.util.List;

import org.eclipse.kapua.app.console.shared.model.GwtEntityCreator;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;

public class GwtRoleCreator extends GwtEntityCreator {

    private static final long serialVersionUID = -1333808048669893906L;

    private String name;

    private List<GwtPermission> permissions;

    public GwtRoleCreator() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GwtPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<GwtPermission> permissions) {
        this.permissions = permissions;
    }
}