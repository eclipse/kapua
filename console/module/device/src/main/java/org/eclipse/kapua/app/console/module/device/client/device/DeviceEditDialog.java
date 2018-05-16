/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.GroupSessionPermission;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;

public class DeviceEditDialog extends DeviceAddDialog {

    private GwtDevice selectedDevice;

    public DeviceEditDialog(GwtSession currentSession, GwtDevice selectedDevice) {
        super(currentSession);
        this.selectedDevice = selectedDevice;
    }

    @Override
    public void createBody() {
        generateBody();

        // hide fields used for add
        clientIdField.hide();
        // this protects against odd issue https://github.com/eclipse/kapua/issues/1631
        groupCombo.setAllowBlank(true);

        loadDevice();
    }

    private void loadDevice() {
        maskDialog();
        gwtDeviceService.findDevice(selectedDevice.getScopeId(), selectedDevice.getId(), new AsyncCallback<GwtDevice>() {

            @Override
            public void onSuccess(GwtDevice gwtDevice) {
                unmaskDialog();
                populateDialog(gwtDevice);
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = DEVICE_MSGS.dialogFormEditLoadFailed(cause.getLocalizedMessage());
                unmaskDialog();
            }
        });

    }

    @Override
    public void submit() {
        // General info
        selectedDevice.setDisplayName(KapuaSafeHtmlUtils.htmlUnescape(displayNameField.getValue()));
        selectedDevice.setGwtDeviceStatus(statusCombo.getSimpleValue().name());

        if (currentSession.hasPermission(GroupSessionPermission.read())) {
            selectedDevice.setGroupId(groupCombo.getValue().getId());
        }

        // Custom attributes
        selectedDevice.setCustomAttribute1(KapuaSafeHtmlUtils.htmlUnescape(customAttribute1Field.getValue()));
        selectedDevice.setCustomAttribute2(KapuaSafeHtmlUtils.htmlUnescape(customAttribute2Field.getValue()));
        selectedDevice.setCustomAttribute3(KapuaSafeHtmlUtils.htmlUnescape(customAttribute3Field.getValue()));
        selectedDevice.setCustomAttribute4(KapuaSafeHtmlUtils.htmlUnescape(customAttribute4Field.getValue()));
        selectedDevice.setCustomAttribute5(KapuaSafeHtmlUtils.htmlUnescape(customAttribute5Field.getValue()));

        // Optlock
        selectedDevice.setOptlock(optlock.getValue().intValue());

        //
        // Submit
        gwtDeviceService.updateAttributes(xsrfToken, selectedDevice, new AsyncCallback<GwtDevice>() {

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                exitMessage = DEVICE_MSGS.deviceFormEditError(caught.getLocalizedMessage());
                hide();
            }

            @Override
            public void onSuccess(GwtDevice gwtDevice) {
                exitStatus = true;
                exitMessage = DEVICE_MSGS.deviceUpdateSuccess();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return DEVICE_MSGS.deviceFormHeadingEdit(this.selectedDevice.getDisplayName() != null ? this.selectedDevice.getDisplayName() : this.selectedDevice.getClientId());
    }

    @Override
    public String getInfoMessage() {
         return DEVICE_MSGS.dialogEditInfo();
    }

    private void populateDialog(GwtDevice device) {
        if (device != null) {
            // General info data
            clientIdField.setValue(device.getClientId());
            clientIdLabel.setText(device.getClientId());
            displayNameField.setValue(device.getUnescapedDisplayName());
            statusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceStatus.valueOf(device.getGwtDeviceStatus()));

            if (currentSession.hasPermission(GroupSessionPermission.read())) {
                if (device.getGroupId() != null) {
                    gwtGroupService.find(currentSession.getSelectedAccountId(), device.getGroupId(), new AsyncCallback<GwtGroup>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            FailureHandler.handle(caught);
                        }

                        @Override
                        public void onSuccess(GwtGroup result) {
                            groupCombo.setValue(result);
                        }
                    });
                } else {
                    groupCombo.setValue(NO_GROUP);
                }
            }

            // // Custom attributes data
            customAttribute1Field.setValue(device.getUnescapedCustomAttribute1());
            customAttribute2Field.setValue(device.getUnescapedCustomAttribute2());
            customAttribute4Field.setValue(device.getUnescapedCustomAttribute4());
            customAttribute3Field.setValue(device.getUnescapedCustomAttribute3());
            customAttribute5Field.setValue(device.getUnescapedCustomAttribute5());

            // Other data
            optlock.setValue(device.getOptlock());
        }
    }
}
