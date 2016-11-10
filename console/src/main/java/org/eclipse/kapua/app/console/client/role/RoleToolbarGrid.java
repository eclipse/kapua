package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.role.dialog.RoleAddDialog;
import org.eclipse.kapua.app.console.client.role.dialog.RoleDeleteDialog;
import org.eclipse.kapua.app.console.client.role.dialog.RoleEditDialog;
import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;

public class RoleToolbarGrid extends EntityCRUDToolbar<GwtRole> {

    public RoleToolbarGrid(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new RoleAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtRole selectedRole = gridSelectionModel.getSelectedItem();
        RoleEditDialog dialog = null;
        if (selectedRole != null) {
            dialog = new RoleEditDialog(currentSession, selectedRole);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtRole selectedRole = gridSelectionModel.getSelectedItem();
        RoleDeleteDialog dialog = null;
        if (selectedRole != null) {
            dialog = new RoleDeleteDialog(selectedRole);
        }
        return dialog;
    }
}
