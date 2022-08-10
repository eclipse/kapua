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
package org.eclipse.kapua.app.console.module.device.client.device.assets.settings;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.assets.GwtDeviceAssetStoreSettings;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceAssetService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceAssetServiceAsync;

import java.util.Arrays;

public class DeviceAssetStoreSettingsDialog extends SimpleDialog {

    private static final GwtDeviceAssetServiceAsync GWT_DEVICE_ASSET_SERVICE_ASYNC = GWT.create(GwtDeviceAssetService.class);

    private GwtDevice gwtDevice;

    private FormPanel formPanel;

    private SimpleComboBox<GwtDeviceAssetStoreSettings.GwtDeviceAssetStoreEnablementPolicy> assetStoreEnablement;

    public DeviceAssetStoreSettingsDialog(GwtDevice gwtDevice) {
        this.gwtDevice = gwtDevice;
        DialogUtils.resizeDialog(this, 500, 150);
    }

    @Override
    public String getInfoMessage() {
        return "Manage per-device settings of the Device Asset Store.";
    }

    @Override
    public String getHeaderMessage() {
        return "Device Asset Store Settings";
    }

    @Override
    public void createBody() {
        bodyPanel.setHeight(600);

        FormData formData = new FormData("-10");

        formPanel = new FormPanel(120);
        bodyPanel.add(formPanel);

        assetStoreEnablement = new SimpleComboBox<GwtDeviceAssetStoreSettings.GwtDeviceAssetStoreEnablementPolicy>();
        assetStoreEnablement.setTriggerAction(ComboBox.TriggerAction.ALL);
        assetStoreEnablement.setAllowBlank(false);
        assetStoreEnablement.setTypeAhead(false);
        assetStoreEnablement.setEditable(false);
        assetStoreEnablement.setFieldLabel("* Store Enablement");
        assetStoreEnablement.setLabelSeparator(":");
        assetStoreEnablement.add(Arrays.asList(GwtDeviceAssetStoreSettings.GwtDeviceAssetStoreEnablementPolicy.values()));
        formPanel.add(assetStoreEnablement, formData);

        loadSettings();
    }

    @Override
    public void submit() {
        GwtDeviceAssetStoreSettings gwtDeviceAssetStoreSettings = new GwtDeviceAssetStoreSettings();

        gwtDeviceAssetStoreSettings.setStoreEnablementPolicy(assetStoreEnablement.getSimpleValue());

        GWT_DEVICE_ASSET_SERVICE_ASYNC.setSettings(xsrfToken, gwtDevice.getScopeId(), gwtDevice.getId(), gwtDeviceAssetStoreSettings, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable t) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                ConsoleInfo.display("Error", "Error while saving settings: " + t.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                ConsoleInfo.display("Success", "Settings saved!");
                hide();
            }
        });
    }

    @Override
    protected void addListeners() {
        // Nothing to do
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }

    private void loadSettings() {
        GWT_DEVICE_ASSET_SERVICE_ASYNC.getSettings(gwtDevice.getScopeId(), gwtDevice.getId(), new AsyncCallback<GwtDeviceAssetStoreSettings>() {
            @Override
            public void onFailure(Throwable t) {
                assetStoreEnablement.setEmptyText("Error while loading settings!");

                ConsoleInfo.display("Error", "Error while loading settings: " + t.getMessage());
            }

            @Override
            public void onSuccess(GwtDeviceAssetStoreSettings gwtDeviceAssetStoreSettings) {
//                assetStoreEnablement.setSimpleValue(GwtDeviceAssetStoreSettings.GwtDeviceAssetStoreEnablementPolicy.ENABLED);
                assetStoreEnablement.setSimpleValue(gwtDeviceAssetStoreSettings.getStoreEnablementPolicyEnum());
            }
        });
    }
}
