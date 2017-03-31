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

import org.eclipse.kapua.app.console.client.messages.ConsoleDeviceGroupMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceGroup;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceGroupDeleteDialog extends EntityDeleteDialog {

    private static final GwtDeviceGroupServiceAsync service = GWT
            .create(GwtDeviceGroupService.class);
    private GwtDeviceGroup gwtDeviceGroup;
    private ConsoleDeviceGroupMessages MSGS = GWT.create(ConsoleDeviceGroupMessages.class);

    public DeviceGroupDeleteDialog(GwtDeviceGroup gwtDevice) {
        this.gwtDeviceGroup = gwtDevice;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        service.delete(gwtDeviceGroup.getScopeId(), gwtDeviceGroup.getId(),
                new AsyncCallback<Void>() {

                    @Override
                    public void onSuccess(Void arg0) {
                        m_exitStatus = true;
                        m_exitMessage = MSGS.dialogDeleteConfirmation();
                        hide();

                    }

                    @Override
                    public void onFailure(Throwable arg0) {

                        m_exitStatus = false;
                        m_exitMessage = MSGS.dialogDeleteError(arg0.getLocalizedMessage());
                        hide();

                    }
                });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteHeader();

    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();

    }

}
