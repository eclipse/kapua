/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
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

        DialogUtils.resizeDialog(this, 400, 200);
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
                if (!isPermissionErrorMessage(cause)) {
                    exitMessage = MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
                }
                unmaskDialog();
                hide();
            }
        });

    }

    private void populateEditDialog(GwtRole gwtRole) {
        roleNameField.setValue(gwtRole.getName());
        roleNameField.setOriginalValue(roleNameField.getValue());
        roleDescriptionField.setValue(gwtRole.getUnescapedDescription());
    }

    @Override
    public void submit() {
        selectedRole.setName(roleNameField.getValue());
        selectedRole.setDescription(KapuaSafeHtmlUtils.htmlUnescape(roleDescriptionField.getValue()));

        GWT_ROLE_SERVICE.update(xsrfToken, selectedRole, new AsyncCallback<GwtRole>() {

            @Override
            public void onSuccess(GwtRole gwtRole) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditError(cause.getLocalizedMessage());
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (!isPermissionErrorMessage(cause)) {
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                            roleNameField.markInvalid(gwtCause.getMessage());
                        }
                    }
                    FailureHandler.handleFormException(formPanel, cause);
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
