/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.role.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.role.RolePermissionGrid;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
            roleService.deleteRolePermission(xsrfToken, session.getSelectedAccount().getId(), selectedRolePermission.getId(), new AsyncCallback<Void>() {

                @Override
                public void onSuccess(Void arg0) {
                    m_exitStatus = true;
                    m_exitMessage = MSGS.dialogDeleteConfirmation();
                    rolePermissionGrid.refresh();
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
        hide();
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
