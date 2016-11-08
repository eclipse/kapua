package org.eclipse.kapua.app.console.client.role.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleAddDialog extends EntityAddDialog {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    private final static GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    private TextField<String> roleNameField;

    private RolePermissionGridField rolePermissionsGrid;

    public RoleAddDialog(GwtSession currentSession) {
        super(currentSession);

        DialogUtils.resizeDialog(this, 400, 400);
    }

    @Override
    public void submit() {
        GwtRoleCreator gwtRoleCreator = new GwtRoleCreator();

        gwtRoleCreator.setScopeId(currentSession.getSelectedAccount().getId());
        gwtRoleCreator.setName(roleNameField.getValue());

        List<GwtRolePermission> newRolePermissions = rolePermissionsGrid.getModels();
        List<GwtPermission> newPermissions = new ArrayList<GwtPermission>();
        for (GwtRolePermission grp : newRolePermissions) {
            newPermissions.add(new GwtPermission(grp.getDomainEnum(), grp.getActionEnum(), grp.getTargetScopeId()));
        }
        gwtRoleCreator.setPermissions(newPermissions);

        gwtRoleService.create(xsrfToken, gwtRoleCreator, new AsyncCallback<GwtRole>() {

            @Override
            public void onSuccess(GwtRole arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(cause.getLocalizedMessage());
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }

    @Override
    public void createBody() {
        FormPanel roleFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        //
        // Name
        roleNameField = new TextField<String>();
        roleNameField.setAllowBlank(false);
        roleNameField.setFieldLabel("* " + MSGS.dialogAddFieldName());
        roleNameField.setToolTip(MSGS.dialogAddFieldNameTooltip());
        roleFormPanel.add(roleNameField);

        //
        // Permissions
        rolePermissionsGrid = new RolePermissionGridField(currentSession);
        rolePermissionsGrid.setFieldLabel(MSGS.dialogAddFieldRolePermissions());
        rolePermissionsGrid.setToolTip(MSGS.dialogAddFieldRolePermissionsTooltip());
        roleFormPanel.add(rolePermissionsGrid);

        //
        // Add form panel to body
        m_bodyPanel.add(roleFormPanel);
    }

}
