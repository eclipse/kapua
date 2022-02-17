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
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceSessionPermission;
import org.eclipse.kapua.app.console.module.tag.client.TagToolbarGrid;

public class DeviceTagToolbar extends TagToolbarGrid {

    private GwtDevice selectedDevice;
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    public DeviceTagToolbar(GwtSession currentSession) {
        super(currentSession, true);

        setEditButtonVisible(false);
    }

    public void setSelectedDevice(GwtDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
        updateButtonEnablement();
    }

    @Override
    protected KapuaDialog getAddDialog() {
        DeviceTagAddDialog dialog = null;
        if (selectedDevice != null) {
            dialog = new DeviceTagAddDialog(currentSession, selectedDevice);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        DeviceTagDeleteDialog dialog = null;
        if (selectedEntity != null) {
            dialog = new DeviceTagDeleteDialog(selectedDevice, selectedEntity);
        }
        return dialog;
    }

    @Override
    protected void updateButtonEnablement() {
        if (addEntityButton != null) {
            addEntityButton.setEnabled(selectedDevice != null
                    && currentSession.hasPermission(DeviceSessionPermission.write()));
        }
        if (deleteEntityButton != null) {
            deleteEntityButton.setEnabled(selectedDevice != null && selectedEntity != null
                    && currentSession.hasPermission(DeviceSessionPermission.write()));
        }
        if (addEntityButton != null) {
            refreshEntityButton.setEnabled(selectedDevice != null);
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        setBorders(true);
        addEntityButton.setText(DEVICE_MSGS.tabTagsAddButton());
        deleteEntityButton.setText(DEVICE_MSGS.tabTagsDeleteButton());
        addEntityButton.setEnabled(selectedDevice != null
                && currentSession.hasPermission(DeviceSessionPermission.write()));
    }
}
