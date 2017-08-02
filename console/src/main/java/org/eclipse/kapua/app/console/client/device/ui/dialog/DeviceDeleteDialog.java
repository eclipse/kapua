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
package org.eclipse.kapua.app.console.client.device.ui.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

    private GwtDevice gwtDevice;

    public DeviceDeleteDialog(GwtDevice gwtUser) {
        this.gwtDevice = gwtUser;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        // return MSGS.dialogDeleteHeader(gwtDevice.getClientId());
        return MSGS.confirm();
    }

    @Override
    public String getInfoMessage() {
        // return MSGS.dialogDeleteInfo();
        return MSGS.deviceDeleteConfirmation(gwtDevice.getClientId());
    }

    @Override
    public void submit() {
        final GwtDevice toDeleteDevice = gwtDevice;

        //
        // Getting XSRF token
        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

            @Override
            public void onFailure(Throwable ex) {
                FailureHandler.handle(ex);
            }

            @Override
            public void onSuccess(GwtXSRFToken token) {
                GWT_DEVICE_SERVICE.deleteDevice(token,
                        toDeleteDevice.getScopeId(),
                        toDeleteDevice.getClientId(),
                        new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                exitStatus = false;
                                // TODO
                                // exitMessage = MSGS.dialogDeleteError(caught.getLocalizedMessage());
                                hide();
                            }

                            public void onSuccess(Void arg0) {
                                // refresh(filterPredicates);
                                exitStatus = true;
                                // TODO
                                // exitMessage = MSGS.dialogDeleteConfirmation();
                                hide();
                            }
                        });
            }
        });
    }

}
