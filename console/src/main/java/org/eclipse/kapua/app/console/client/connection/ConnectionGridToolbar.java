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
package org.eclipse.kapua.app.console.client.connection;

import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection;

import com.google.gwt.user.client.Element;

public class ConnectionGridToolbar extends EntityCRUDToolbar<GwtDeviceConnection> {

    public ConnectionGridToolbar(GwtSession currentSession) {
        super(currentSession);
        setAddButtonVisible(false);
        setEditButtonVisible(true);
        setDeleteButtonVisible(false);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return null;
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtDeviceConnection selectedConnection = gridSelectionModel.getSelectedItem();
        ConnectionEditDialog dialog = null;
        if (selectedConnection != null) {
            dialog = new ConnectionEditDialog(currentSession, selectedConnection);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        return null;
    }

}
