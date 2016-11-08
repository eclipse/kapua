package org.eclipse.kapua.app.console.client.role.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    private GwtRole gwtRole;

    public RoleDeleteDialog(GwtRole gwtRole) {
        this.gwtRole = gwtRole;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteHeader(gwtRole.getName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();
    }

    @Override
    public void submit() {
        gwtRoleService.delete(xsrfToken, gwtRole.getScopeId(), gwtRole.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogDeleteConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogDeleteError(cause.getLocalizedMessage());
                hide();
            }
        });

    }

}
