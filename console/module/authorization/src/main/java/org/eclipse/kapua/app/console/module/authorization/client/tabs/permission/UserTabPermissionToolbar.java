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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.permission;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;

public class UserTabPermissionToolbar extends EntityCRUDToolbar<GwtAccessPermission> {

    private String userId;
    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);

    public UserTabPermissionToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtAccessPermission selectedAccessPermission = gridSelectionModel.getSelectedItem();
        PermissionDeleteDialog dialog = null;
        if (selectedAccessPermission != null) {
            dialog = new PermissionDeleteDialog(selectedAccessPermission);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getAddDialog() {
        PermissionAddDialog dialog = null;
        if (userId != null) {
            dialog = new PermissionAddDialog(currentSession, userId);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        addEntityButton.setText(MSGS.dialogAddTitle());
        deleteEntityButton.setText(MSGS.dialogDeleteTitle());
        addEntityButton.setEnabled(userId != null);
        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }

}
