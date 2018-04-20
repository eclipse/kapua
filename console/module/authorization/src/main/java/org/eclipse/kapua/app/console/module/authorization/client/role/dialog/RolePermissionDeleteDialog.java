/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.client.role.RolePermissionGrid;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

public class RolePermissionDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    protected GwtSession session;
    protected GwtRolePermission selectedRolePermission;
    RolePermissionGrid rolePermissionGrid;

    public RolePermissionDeleteDialog(GwtSession session, RolePermissionGrid rolePermissionGrid) {
        this.session = session;
        DialogUtils.resizeDialog(this, 400, 150);
        this.rolePermissionGrid = rolePermissionGrid;
    }

    @Override
    public void submit() {
        getSelectedRolePermission();
        if (selectedRolePermission != null) {
            GwtRoleServiceAsync roleService = GWT.create(GwtRoleService.class);
            roleService.deleteRolePermission(xsrfToken, session.getSelectedAccountId(), selectedRolePermission.getId(), new AsyncCallback<Void>() {

                @Override
                public void onSuccess(Void arg0) {
                    exitStatus = true;
                    exitMessage = MSGS.dialogDeleteConfirmation();
                    rolePermissionGrid.refresh();
                    hide();
                }

                @Override
                public void onFailure(Throwable cause) {
                    exitStatus = false;
                    exitMessage = MSGS.dialogDeleteError(cause.getLocalizedMessage());
                    hide();
                }
            });
        }

    }

    private void getSelectedRolePermission() {
        if (selectedRolePermission == null) {
            selectedRolePermission = rolePermissionGrid.getSelectionModel().getSelectedItem();
        }
    }

    @Override
    public String getHeaderMessage() {
        getSelectedRolePermission();
        return MSGS.permissionDeleteDialogHeader(selectedRolePermission.getDomain() + " " + selectedRolePermission.getAction());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.permissionDeleteDialogMessage();
    }

}
