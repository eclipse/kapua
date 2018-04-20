/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.group;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.GroupSessionPermission;

import com.google.gwt.user.client.Element;

public class GroupToolbarGrid extends EntityCRUDToolbar<GwtGroup> {

    public GroupToolbarGrid(GwtSession currentSession) {
        super(currentSession);

    }

    @Override
    protected KapuaDialog getAddDialog() {

        return new GroupAddDialog(currentSession);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getAddEntityButton().setEnabled(currentSession.hasPermission(GroupSessionPermission.write()));
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtGroup selectedGroup = gridSelectionModel.getSelectedItem();
        GroupEditDialog dialog = null;
        if (selectedGroup != null) {
            dialog = new GroupEditDialog(currentSession, selectedGroup);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtGroup selectedGroup = gridSelectionModel.getSelectedItem();
        GroupDeleteDialog dialog = null;
        if (selectedGroup != null) {
            dialog = new GroupDeleteDialog(selectedGroup);
        }
        return dialog;
    }

}
