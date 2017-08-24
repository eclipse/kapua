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

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;

public class UserTabAccessRoleToolbar extends EntityCRUDToolbar<GwtAccessRole> {

    private String userId;

    public UserTabAccessRoleToolbar(GwtSession currentSession) {
        super(currentSession);
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
        addEntityButton.setEnabled(userId != null);
        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }
}
