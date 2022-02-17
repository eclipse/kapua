/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.connection.toolbar;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.connection.ConnectionEditDialog;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection;

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
