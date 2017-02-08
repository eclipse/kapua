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
package org.eclipse.kapua.app.console.client.user.tabs.permission;

import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermission;

public class UserTabPermissionToolbar extends EntityCRUDToolbar<GwtAccessPermission> {

    private String userId;
    
    public UserTabPermissionToolbar(GwtSession currentSession) {
        super(currentSession, false);
    }
      
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
        if (isRendered()) {
            if (userId == null) {
                addEntityButton.disable();
                refreshEntityButton.disable();
            } else {
                addEntityButton.enable();
                refreshEntityButton.enable();
            }
        } else {
            if (userId == null) {
                addEntityButtonEnabledOnRender = false;
                refreshEntityButtonEnabledOnRender = false;
            } else {
                addEntityButtonEnabledOnRender = true;
                refreshEntityButtonEnabledOnRender = true;
            }
        }
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
        return new PermissionAddDialog(currentSession, userId);
    }

}
