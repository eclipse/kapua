package org.eclipse.kapua.app.console.client.role.dialog;

import org.eclipse.kapua.app.console.client.ui.widget.EntityGridFieldToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;

public class RolePermissionGridFieldToolbar extends EntityGridFieldToolbar<GwtRolePermission> {

    public RolePermissionGridFieldToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public GwtRolePermission getNewModel() {
        GwtRolePermission gwtRolePermission = new GwtRolePermission();
        gwtRolePermission.setTargetScopeId(currentSession.getSelectedAccount().getId());
        return gwtRolePermission;
    }

}
