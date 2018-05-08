/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.role.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

public class RoleEditDialog extends RoleAddDialog {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync GWT_ROLE_SERVICE = GWT.create(GwtRoleService.class);

    private GwtRole selectedRole;

    public RoleEditDialog(GwtSession currentSession, GwtRole selectedRole) {
        super(currentSession);
        this.selectedRole = selectedRole;

        DialogUtils.resizeDialog(this, 400, 150);
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
        GWT_ROLE_SERVICE.find(selectedRole.getScopeId(), selectedRole.getId(), new AsyncCallback<GwtRole>() {

            @Override
            public void onSuccess(GwtRole gwtRole) {
                unmaskDialog();
                populateEditDialog(gwtRole);
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
                unmaskDialog();
                hide();
            }
        });

    }

    private void populateEditDialog(GwtRole gwtRole) {
        roleNameField.setValue(gwtRole.getName());
    }

    @Override
    public void submit() {
        selectedRole.setName(roleNameField.getValue());

        GWT_ROLE_SERVICE.update(xsrfToken, selectedRole, new AsyncCallback<GwtRole>() {

            @Override
            public void onSuccess(GwtRole arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditError(cause.getLocalizedMessage());
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (cause instanceof GwtKapuaException) {
                    GwtKapuaException gwtCause = (GwtKapuaException) cause;
                    if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                        roleNameField.markInvalid(gwtCause.getMessage());
                    }
                }
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

}
