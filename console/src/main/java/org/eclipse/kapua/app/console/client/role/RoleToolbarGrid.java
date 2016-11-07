package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.role.dialog.RoleAddDialog;
import org.eclipse.kapua.app.console.client.role.dialog.RoleDeleteDialogConfirmation;
import org.eclipse.kapua.app.console.client.role.dialog.RoleEditDialog;
import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.KapuaEntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;

public class RoleToolbarGrid extends KapuaEntityCRUDToolbar<GwtRole> {

    @Override
    protected KapuaDialog getAddDialog() {
        return new RoleAddDialog();
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtRole selectedRole = gridSelectionModel.getSelectedItem();
        RoleEditDialog dialog = null;
        if (selectedRole != null) {
            dialog = new RoleEditDialog(selectedRole);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtRole selectedRole = gridSelectionModel.getSelectedItem();
        RoleDeleteDialogConfirmation dialog = null;
        if (selectedRole != null) {
            dialog = new RoleDeleteDialogConfirmation(selectedRole);
        }
        return dialog;
    }
}
