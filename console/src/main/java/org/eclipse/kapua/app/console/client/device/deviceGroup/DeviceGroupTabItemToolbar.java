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
package org.eclipse.kapua.app.console.client.device.deviceGroup;

import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceGroup;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.google.gwt.user.client.Element;

public class DeviceGroupTabItemToolbar extends EntityCRUDToolbar<GwtDeviceGroup> {

    private GwtDevice gwtDevice;
    private GwtDeviceGroup gwtDeviceGroup;

    public DeviceGroupTabItemToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public void setSelectedDevice(GwtDevice selectedDevice) {
        gwtDevice = selectedDevice;
        refreshButtons();

    }

    @Override
    protected KapuaDialog getAddDialog() {
        DeviceGroupAddDialog addDialog = null;
        if (gwtDevice != null) {
            addDialog = new DeviceGroupAddDialog(currentSession, gwtDevice);
        }
        return addDialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtDeviceGroup selectedDeviceGroup = gridSelectionModel.getSelectedItem();
        DeviceGroupDeleteDialog deleteDialog = null;
        if (selectedDeviceGroup != null) {
            deleteDialog = new DeviceGroupDeleteDialog(selectedDeviceGroup);
        }
        return deleteDialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        editEntityButton.hide();
        deleteEntityButton.disable();
        if (gwtDevice != null) {
            addEntityButton.enable();
        }else{
            addEntityButton.disable();
        }
    }

    public void refreshButtons() {
        if (this.isRendered()) {
            if (gwtDevice != null) {
                addEntityButton.enable();
            } else {
                addEntityButton.disable();
            }

            if (gridSelectionModel.getSelectedItem() != null) {
                deleteEntityButton.enable();
            } else {
                deleteEntityButton.disable();
            }

        }
    }

}
