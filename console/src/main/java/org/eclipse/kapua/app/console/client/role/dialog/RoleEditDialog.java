package org.eclipse.kapua.app.console.client.role.dialog;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleEditDialog extends RoleAddDialog {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private final static GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    private GwtRole selectedRole;

    public RoleEditDialog(GwtSession currentSession, GwtRole selectedRole) {
        super(currentSession);
        this.selectedRole = selectedRole;

        DialogUtils.resizeDialog(this, 500, 400);
    }

    @Override
    public void createBody() {
        super.createBody();

        //
        // Compile edit form
        loadRole();
    }

    private void loadRole() {
        maskDialog();
        gwtRoleService.find(selectedRole.getScopeId(), selectedRole.getId(), new AsyncCallback<GwtRole>() {

            @Override
            public void onSuccess(GwtRole gwtRole) {
                unmaskDialog();
                populateEditDialog(gwtRole);
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
                unmaskDialog();
                hide();
            }
        });

    }

    private void populateEditDialog(GwtRole gwtRole) {
        roleNameField.setValue(gwtRole.getName());
        rolePermissionsGrid.getStore().add(new ArrayList<GwtRolePermission>(gwtRole.getPermissions()));
    }

    @Override
    public void submit() {
        selectedRole.setName(roleNameField.getValue());
        selectedRole.setPermissions(new HashSet<GwtRolePermission>(rolePermissionsGrid.getModels()));

        gwtRoleService.update(xsrfToken, selectedRole, new AsyncCallback<GwtRole>() {

            @Override
            public void onSuccess(GwtRole arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogEditError(cause.getLocalizedMessage());
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedRole.getName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    @Override
    protected RolePermissionNewGridField getRolePermissionNewGridField(GwtSession currentSession) {
        RolePermissionEditGridField rolePermissionsGrid = new RolePermissionEditGridField(currentSession);
        rolePermissionsGrid.setFieldLabel(MSGS.dialogEditFieldRolePermissions());
        rolePermissionsGrid.setToolTip(MSGS.dialogEditFieldRolePermissionsTooltip());
        return rolePermissionsGrid;
    }
}
