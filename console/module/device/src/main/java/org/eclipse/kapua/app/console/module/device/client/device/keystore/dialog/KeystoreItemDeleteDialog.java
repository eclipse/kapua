/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreItem;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

public class KeystoreItemDeleteDialog extends EntityDeleteDialog {

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);

    private GwtDevice gwtDevice;
    private GwtDeviceKeystoreItem gwtDeviceKeystoreItem;

    public KeystoreItemDeleteDialog(GwtDevice gwtDevice, GwtDeviceKeystoreItem gwtDeviceKeystoreItem) {
        this.gwtDevice = gwtDevice;
        this.gwtDeviceKeystoreItem = gwtDeviceKeystoreItem;

        DialogUtils.resizeDialog(this, 300, 120);
    }

    @Override
    public String getHeaderMessage() {
        return "Delete keystore item: " + gwtDeviceKeystoreItem.getAlias();
    }

    @Override
    public String getInfoMessage() {
        return "Are you sure to delete keystore item? Removing a keystore item may affect other device applications that are using them.";
    }

    @Override
    public void submit() {
        GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.deleteKeystoreItem(xsrfToken,
                gwtDevice.getScopeId(),
                gwtDevice.getId(),
                gwtDeviceKeystoreItem.getKeystoreId(),
                gwtDeviceKeystoreItem.getAlias(),
                new AsyncCallback<Void>() {

                    @Override
                    public void onSuccess(Void arg0) {
                        exitStatus = true;
                        exitMessage = "Keystore item deleted!";
                        hide();
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        exitStatus = false;
                        exitMessage = "Error while deleting keystore item: " + cause.getMessage();
                        hide();
                    }
                });
    }

}
