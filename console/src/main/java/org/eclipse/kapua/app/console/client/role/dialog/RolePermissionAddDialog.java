package org.eclipse.kapua.app.console.client.role.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EnumComboBox;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermissionCreator;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class RolePermissionAddDialog extends EntityAddEditDialog {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    
    protected EnumComboBox<GwtDomain> domainCombo;
    protected EnumComboBox<GwtAction> actionCombo;
    protected GwtRole selectedRole;
    
    public RolePermissionAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }
    
    public void setSelectedRole(GwtRole selectedRole){
        this.selectedRole = selectedRole;
    }
    
    @Override
    public void createBody() {
        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        
        domainCombo = new EnumComboBox<GwtDomain>();
        domainCombo.add(GwtDomain.values());
        domainCombo.setFieldLabel("Domain");
        domainCombo.setForceSelection(true);
        domainCombo.setSimpleValue(GwtDomain.account);
        formPanel.add(domainCombo);
        
        actionCombo = new EnumComboBox<GwtAction>();
        actionCombo.add(GwtAction.values());
        actionCombo.setFieldLabel("Action");
        actionCombo.setForceSelection(true);
        actionCombo.setSimpleValue(GwtAction.read);
        formPanel.add(actionCombo);

        m_bodyPanel.add(formPanel);
    }

    @Override
    public void submit() {
        GwtPermission permission = new GwtPermission();
        
        permission.setDomain(domainCombo.getValue().getValue().toString());
        permission.setAction(actionCombo.getValue().getValue().toString());
        permission.setTargetScopeId(currentSession.getSelectedAccount().getId());
        GwtRolePermissionCreator rolePermission = new GwtRolePermissionCreator();
        rolePermission.setScopeId(currentSession.getSelectedAccount().getId());
        rolePermission.setRoleId(selectedRole.getId());
        
        GwtRoleServiceAsync roleService = GWT.create(GwtRoleService.class);
        roleService.addRolePermission(xsrfToken, rolePermission, permission, new AsyncCallback<GwtRolePermission>() {
            
            @Override
            public void onSuccess(GwtRolePermission rolePermission) {
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
        return MSGS.permissionAddDialogHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.permissionAddDialogMessage();
    }

}
