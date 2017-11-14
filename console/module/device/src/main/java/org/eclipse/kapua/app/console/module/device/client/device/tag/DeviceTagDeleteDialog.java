/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;

public class DeviceTagDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

    private GwtDevice selectedDevice;
    private GwtTag selectedTag;

    public DeviceTagDeleteDialog(GwtDevice selectedDevice, GwtTag selectedTag) {
        this.selectedDevice = selectedDevice;
        this.selectedTag = selectedTag;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeviceTagDeleteHeader(selectedTag.getTagName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeviceTagDeleteInfo(selectedTag.getTagName(), selectedDevice.getClientId());
    }

    @Override
    public void submit() {
        GWT_DEVICE_SERVICE.deleteDeviceTag(xsrfToken, selectedDevice.getScopeId(), selectedDevice.getId(), selectedTag.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogDeviceTagDeleteConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogDeviceTagDeleteError(cause.getLocalizedMessage());
                hide();
            }
        });

    }
}
