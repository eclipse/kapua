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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.tabs.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;

public class UserTabAccessRoleToolbar extends EntityCRUDToolbar<GwtAccessRole> {

    private String userId;
    private static final ConsolePermissionMessages PERMISSION_MSGS = GWT.create(ConsolePermissionMessages.class);

    public UserTabAccessRoleToolbar(GwtSession currentSession) {
        super(currentSession, true);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtAccessRole selectedAccessRole = gridSelectionModel.getSelectedItem();
        AccessRoleDeleteDialog dialog = null;
        if (selectedAccessRole != null) {
            dialog = new AccessRoleDeleteDialog(selectedAccessRole);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getAddDialog() {
        AccessRoleAddDialog dialog = null;
        if (userId != null) {
            dialog = new AccessRoleAddDialog(currentSession, userId);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        addEntityButton.setText(PERMISSION_MSGS.dialogAddRoleButton());
        deleteEntityButton.setText(PERMISSION_MSGS.dialogDeleteRoleButton());
        addEntityButton.setEnabled(userId != null);
        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();
        addEntityButton.setEnabled(userId != null);
    }
}
