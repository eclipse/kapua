/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserAddDialog;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserDeleteDialog;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserEditDialog;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

import com.google.gwt.user.client.Element;


public class UserGridToolbar extends EntityCRUDToolbar<GwtUser> {

    public UserGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new UserAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserEditDialog dialog = null;
        if (selectedUser!= null) {
            dialog = new UserEditDialog(currentSession, selectedUser);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserDeleteDialog dialog = null;
        if (selectedUser!= null) {
            dialog = new UserDeleteDialog(selectedUser);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
    }

}
