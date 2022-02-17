/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client.cors;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.permission.EndpointSessionPermission;

public class CorsToolbarGrid extends EntityCRUDToolbar<GwtEndpoint> {

    public CorsToolbarGrid(GwtSession currentSession) {
        super(currentSession);
    }

    public CorsToolbarGrid(GwtSession currentSession, boolean slaveEntity) {
        super(currentSession, slaveEntity);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new CorsAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtEndpoint selectedEndpoint = gridSelectionModel.getSelectedItem();
        CorsEditDialog dialog = null;
        if (selectedEndpoint != null) {
            dialog = new CorsEditDialog(currentSession, selectedEndpoint);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtEndpoint selectedEndpoint = gridSelectionModel.getSelectedItem();
        CorsDeleteDialog dialog = null;
        if (selectedEndpoint != null) {
            dialog = new CorsDeleteDialog(selectedEndpoint);
        }
        return dialog;
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();
        //
        // Force disabled if entity is inherited from parent scopes
        addEntityButton.setEnabled(currentSession.hasPermission(EndpointSessionPermission.write()));
        editEntityButton.setEnabled(selectedEntity != null && currentSession.hasPermission(EndpointSessionPermission.write()));
        deleteEntityButton.setEnabled(selectedEntity != null && currentSession.hasPermission(EndpointSessionPermission.delete()));
    }
}
