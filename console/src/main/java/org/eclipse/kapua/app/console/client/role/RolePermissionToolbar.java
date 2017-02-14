package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.role.dialog.RolePermissionAddDialog;
import org.eclipse.kapua.app.console.client.role.dialog.RolePermissionDeleteDialog;
import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;

public class RolePermissionToolbar extends EntityCRUDToolbar<GwtRolePermission> {

    protected GwtRole selectedRole;
    protected GwtRolePermission selectedRolePermission;

    private RolePermissionAddDialog addDialog;
    private RolePermissionDeleteDialog deleteDialog;
    private RolePermissionGrid rolePermissionGrid;
    
    public RolePermissionToolbar(GwtSession currentSession, RolePermissionGrid rolePermissionGrid) {
        super(currentSession);
        super.setEditButtonVisible(false);
        this.rolePermissionGrid = rolePermissionGrid;
    }

    public void setSelectedRole(GwtRole selectedRole) {
        this.selectedRole = selectedRole;
        if (addDialog != null) {
            addDialog.setSelectedRole(selectedRole);
        }
    }

    public void setSelectedRolePermission(GwtRolePermission selectedRolePermission) {
        this.selectedRolePermission = selectedRolePermission;
    }

    @Override
    protected KapuaDialog getAddDialog() {
        addDialog = new RolePermissionAddDialog(currentSession);
        addDialog.setSelectedRole(selectedRole);
        return addDialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        deleteDialog = new RolePermissionDeleteDialog(currentSession, rolePermissionGrid);
        return deleteDialog;
    }

}
