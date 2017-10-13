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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.tabs.permission.PermissionEditDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionServiceAsync;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserTabPermissionToolbar extends EntityCRUDToolbar<GwtAccessPermission> {

    private String userId;
    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);
    private final static GwtAccessPermissionServiceAsync GWT_ACCESS_PERMISSION_SERVICE = GWT.create(GwtAccessPermissionService.class);
    private List<GwtAccessPermission> listPermissions;

    public UserTabPermissionToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected KapuaDialog getEditDialog() {
        PermissionEditDialog editDialog = null;
        if (userId != null) {
            editDialog = new PermissionEditDialog(currentSession, userId);
            editDialog.setCheckedPermissionsList(listPermissions);
        }
        return editDialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        addEntityButton.setText(MSGS.dialogAddTitle());
        deleteEntityButton.setText(MSGS.dialogDeleteTitle());
        addEntityButton.hide();
        editEntityButton.setEnabled(userId != null);
        deleteEntityButton.hide();
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }

    public void getAccessPermissions() {
        listPermissions = new ArrayList<GwtAccessPermission>();
        GWT_ACCESS_PERMISSION_SERVICE.findByUserId(null, currentSession.getSelectedAccountId(), userId, new AsyncCallback<PagingLoadResult<GwtAccessPermission>>() {

            @Override
            public void onSuccess(PagingLoadResult<GwtAccessPermission> result) {
                listPermissions = result.getData();

            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }
        });
    }
}
