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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.client.role.dialog.RolePermissionAddDialog;
import org.eclipse.kapua.app.console.module.authorization.client.role.dialog.RolePermissionDeleteDialog;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;

public class RolePermissionToolbar extends EntityCRUDToolbar<GwtRolePermission> {

    protected GwtRole selectedRole;
    protected GwtRolePermission selectedRolePermission;

    private RolePermissionAddDialog addDialog;
    private RolePermissionDeleteDialog deleteDialog;
    private RolePermissionGrid rolePermissionGrid;
    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);

    public RolePermissionToolbar(GwtSession currentSession, RolePermissionGrid rolePermissionGrid) {
        super(currentSession, true);
        super.setEditButtonVisible(false);
        this.rolePermissionGrid = rolePermissionGrid;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        setBorders(true);
        getDeleteEntityButton().setEnabled(false);
        checkAddButton();
    }

    public void setSelectedRole(GwtRole selectedRole) {
        this.selectedRole = selectedRole;
        checkAddButton();
        if (addDialog != null) {
            addDialog.setSelectedRole(selectedRole);
        }
    }

    private void checkAddButton() {
        if (getAddEntityButton() != null) {
            getAddEntityButton().setEnabled(selectedRole != null
                    && currentSession.hasPermission(RoleSessionPermission.write()));
        }
    }

    public void setSelectedRolePermission(GwtRolePermission selectedRolePermission) {
        this.selectedRolePermission = selectedRolePermission;
    }

    @Override
    protected KapuaDialog getAddDialog() {
        addDialog = new RolePermissionAddDialog(currentSession);
        addDialog.setSelectedRole(selectedRole);
        return addDialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        deleteDialog = new RolePermissionDeleteDialog(currentSession, rolePermissionGrid);
        return deleteDialog;
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();
        deleteEntityButton.setText(MSGS.gridRolePermissionToolbarDeleteButton());
    }
}
