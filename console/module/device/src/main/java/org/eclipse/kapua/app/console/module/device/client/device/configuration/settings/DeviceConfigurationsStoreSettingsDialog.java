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
package org.eclipse.kapua.app.console.module.device.client.device.configuration.settings;

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
import org.eclipse.kapua.app.console.module.device.shared.model.management.configurations.GwtDeviceConfigurationStoreSettings;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementServiceAsync;

import java.util.Arrays;

public class DeviceConfigurationsStoreSettingsDialog extends SimpleDialog {

    private static final GwtDeviceManagementServiceAsync GWT_DEVICE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceManagementService.class);

    private GwtDevice gwtDevice;

    private FormPanel formPanel;

    private SimpleComboBox<GwtDeviceConfigurationStoreSettings.GwtDeviceConfigurationStoreEnablementPolicy> configurationStoreEnablement;

    public DeviceConfigurationsStoreSettingsDialog(GwtDevice gwtDevice) {
        this.gwtDevice = gwtDevice;
        DialogUtils.resizeDialog(this, 500, 150);
    }

    @Override
    public String getInfoMessage() {
        return "Manage per-device settings of the Device Configuration Store.";
    }

    @Override
    public String getHeaderMessage() {
        return "Device Configuration Store Settings";
    }

    @Override
    public void createBody() {
        bodyPanel.setHeight(600);

        FormData formData = new FormData("-10");

        formPanel = new FormPanel(120);
        bodyPanel.add(formPanel);

        configurationStoreEnablement = new SimpleComboBox<GwtDeviceConfigurationStoreSettings.GwtDeviceConfigurationStoreEnablementPolicy>();
        configurationStoreEnablement.setTriggerAction(ComboBox.TriggerAction.ALL);
        configurationStoreEnablement.setAllowBlank(false);
        configurationStoreEnablement.setTypeAhead(false);
        configurationStoreEnablement.setEditable(false);
        configurationStoreEnablement.setFieldLabel("* Store Enablement");
        configurationStoreEnablement.setLabelSeparator(":");
        configurationStoreEnablement.add(Arrays.asList(GwtDeviceConfigurationStoreSettings.GwtDeviceConfigurationStoreEnablementPolicy.values()));
        formPanel.add(configurationStoreEnablement, formData);

        loadSettings();
    }

    @Override
    public void submit() {
        GwtDeviceConfigurationStoreSettings gwtDeviceConfigurationStoreSettings = new GwtDeviceConfigurationStoreSettings();

        gwtDeviceConfigurationStoreSettings.setStoreEnablementPolicy(configurationStoreEnablement.getSimpleValue());

        GWT_DEVICE_MANAGEMENT_SERVICE.setDeviceConfigurationSettings(xsrfToken, gwtDevice.getScopeId(), gwtDevice.getId(), gwtDeviceConfigurationStoreSettings, new AsyncCallback<Void>() {
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
        GWT_DEVICE_MANAGEMENT_SERVICE.getDeviceConfigurationSettings(gwtDevice.getScopeId(), gwtDevice.getId(), new AsyncCallback<GwtDeviceConfigurationStoreSettings>() {
            @Override
            public void onFailure(Throwable t) {
                configurationStoreEnablement.setEmptyText("Error while loading settings!");

                ConsoleInfo.display("Error", "Error while loading settings: " + t.getMessage());
            }

            @Override
            public void onSuccess(GwtDeviceConfigurationStoreSettings gwtDeviceConfigurationStoreSettings) {
                configurationStoreEnablement.setSimpleValue(gwtDeviceConfigurationStoreSettings.getStoreEnablementPolicyEnum());
            }
        });
    }
}
