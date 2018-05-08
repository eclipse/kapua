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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.permission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.DomainSessionPermission;

public class UserTabPermissionToolbar extends EntityCRUDToolbar<GwtAccessPermission> {

    private String userId;
    private static final ConsolePermissionMessages PERMISSION_MSGS = GWT.create(ConsolePermissionMessages.class);

    public UserTabPermissionToolbar(GwtSession currentSession) {
        super(currentSession, true);
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
        addEntityButton.setText(PERMISSION_MSGS.dialogAddPermissionButton());
        deleteEntityButton.setText(PERMISSION_MSGS.dialogDeletePermissionButton());
        addEntityButton.setEnabled(userId != null && currentSession.hasPermission(AccessInfoSessionPermission.write()) &&
                currentSession.hasPermission(DomainSessionPermission.read()) && currentSession.hasPermission(DomainSessionPermission.write()));
        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();
        addEntityButton.setEnabled(userId != null && currentSession.hasPermission(AccessInfoSessionPermission.write()) &&
                currentSession.hasPermission(DomainSessionPermission.read()) && currentSession.hasPermission(DomainSessionPermission.write()));
    }
}
