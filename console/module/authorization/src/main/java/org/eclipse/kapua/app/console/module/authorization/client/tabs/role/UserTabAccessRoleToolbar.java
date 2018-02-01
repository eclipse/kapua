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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.authorization.client.tabs.role.AccessRoleEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;

public class UserTabAccessRoleToolbar extends EntityCRUDToolbar<GwtAccessRole> {

    private String userId;
    private final static GwtAccessRoleServiceAsync GWT_ACCESS_ROLE_SERVICE = GWT.create(GwtAccessRoleService.class);
    private List<GwtAccessRole> checkedRolesList;
    private List<GwtRole> rolesList;
    private final static GwtRoleServiceAsync GWT_ROLE_SERVICE = GWT.create(GwtRoleService.class);
    private static final ConsolePermissionMessages PERMISSION_MSGS = GWT.create(ConsolePermissionMessages.class);

    public UserTabAccessRoleToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected KapuaDialog getEditDialog() {
        AccessRoleEditDialog editDialog = null;
        if (userId != null) {
            editDialog = new AccessRoleEditDialog(currentSession, userId);
            editDialog.setCheckedRolesList(checkedRolesList);
            editDialog.setAllRoles(rolesList);
        }
        return editDialog;
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
        addEntityButton.hide();
        deleteEntityButton.hide();
        editEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }

    public void getAccessRoles() {
        checkedRolesList = new ArrayList<GwtAccessRole>();
        rolesList = new ArrayList<GwtRole>();
        GWT_ROLE_SERVICE.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<ListLoadResult<GwtRole>>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(ListLoadResult<GwtRole> result) {
                rolesList = result.getData();

            }
        });

        GWT_ACCESS_ROLE_SERVICE.findByUserId(null, currentSession.getSelectedAccountId(), userId, new AsyncCallback<PagingLoadResult<GwtAccessRole>>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(PagingLoadResult<GwtAccessRole> result) {
                checkedRolesList = result.getData();

            }
        });
    }
}
