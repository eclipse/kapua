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
package org.eclipse.kapua.app.console.module.device.client.device;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;

public class DeviceGridToolbar extends EntityCRUDToolbar<GwtDevice> {

    public DeviceGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new DeviceAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        DeviceEditDialog dialog = null;
        if (selectedEntity != null) {
            dialog = new DeviceEditDialog(currentSession, selectedEntity);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        DeviceDeleteDialog dialog = null;
        if (selectedEntity != null) {
            dialog = new DeviceDeleteDialog(selectedEntity);
        }
        return dialog;
    }
}
