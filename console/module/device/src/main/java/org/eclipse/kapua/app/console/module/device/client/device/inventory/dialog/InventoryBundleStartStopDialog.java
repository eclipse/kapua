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
package org.eclipse.kapua.app.console.module.device.client.device.inventory.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.inventory.GwtInventoryBundle;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceInventoryManagementServiceAsync;

public class InventoryBundleStartStopDialog extends EntityDeleteDialog {

    private static final GwtDeviceInventoryManagementServiceAsync GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE = GWT.create(GwtDeviceInventoryManagementService.class);

    private GwtDevice gwtDevice;
    private GwtInventoryBundle gwtInventoryBundle;
    private boolean startOrStop;

    public InventoryBundleStartStopDialog(GwtDevice gwtDevice, GwtInventoryBundle gwtInventoryBundle, boolean startOrStop) {
        this.gwtDevice = gwtDevice;
        this.gwtInventoryBundle = gwtInventoryBundle;
        this.startOrStop = startOrStop;

        DialogUtils.resizeDialog(this, 300, 150);
    }

    @Override
    public String getHeaderMessage() {
        if (startOrStop) {
            return "Start bundle: " + gwtInventoryBundle.getName();
        } else {
            return "Stop bundle: " + gwtInventoryBundle.getName();
        }
    }

    @Override
    public String getInfoMessage() {
        if (startOrStop) {
            return "Are you sure to start the bundle?";
        } else {
            return "Are you sure to stop the bundle? Stopping a bundle might have unexpected effects on the stability of the framework. If in doubt, select \"No\"";
        }
    }

    @Override
    public void submit() {
        GWT_DEVICE_INVENTORY_MANAGEMENT_SERVICE.execDeviceBundle(xsrfToken,
                gwtDevice.getScopeId(),
                gwtDevice.getId(),
                gwtInventoryBundle,
                startOrStop,
                new AsyncCallback<Void>() {

                    @Override
                    public void onSuccess(Void arg0) {
                        exitStatus = true;
                        exitMessage = "Bundle " + (startOrStop ? "started" : "stopped") + "!";
                        hide();
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        exitStatus = false;
                        exitMessage = "Error while " + (startOrStop ? "starting" : "stopping") + " the bundle! Error: " + cause.getMessage();
                        hide();
                    }
                });
    }

}
