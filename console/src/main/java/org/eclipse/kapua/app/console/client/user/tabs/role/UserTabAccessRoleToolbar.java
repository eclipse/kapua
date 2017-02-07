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
package org.eclipse.kapua.app.console.client.user.tabs.role;

import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRole;

public class UserTabAccessRoleToolbar extends EntityCRUDToolbar<GwtAccessRole> {

    private String userId;
    
    public UserTabAccessRoleToolbar(GwtSession currentSession) {
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
                editEntityButtonEnabledOnRender = false;
                refreshEntityButtonEnabledOnRender = false;
            } else {
                editEntityButtonEnabledOnRender = true;
                refreshEntityButtonEnabledOnRender = true;
            }
        }
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
        return new AccessRoleAddDialog(currentSession, userId);
    }

}
