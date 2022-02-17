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

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystore;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreKeypair;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

import java.util.List;

public class KeystoreItemAddKeypairDialog extends SimpleDialog {

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);

    private GwtDevice gwtDevice;

    private FormPanel formPanel;

    private SimpleComboBox<String> keystoreCombo;
    private KapuaTextField<String> aliasField;
    private KapuaTextField<String> algorithmField;
    private KapuaNumberField sizeField;
    private KapuaTextField<String> signatureAlgorithmField;
    private KapuaTextField<String> attributesField;

    public KeystoreItemAddKeypairDialog(GwtDevice gwtDevice) {
        this.gwtDevice = gwtDevice;

        DialogUtils.resizeDialog(this, 500, 300);
    }

    @Override
    public String getInfoMessage() {
        return "Provide the information of the keypair. Adding a new keypair may affect other device applications that are using keystores.";
    }

    @Override
    public String getHeaderMessage() {
        return "Add new keypair";
    }

    @Override
    public void createBody() {
        FormData formData = new FormData("-10");

        formPanel = new FormPanel(120);
        bodyPanel.add(formPanel);

        keystoreCombo = new SimpleComboBox<String>();
        keystoreCombo.setAllowBlank(false);
        keystoreCombo.setTypeAhead(false);
        keystoreCombo.setEditable(false);
        keystoreCombo.setFieldLabel("* Keystore");
        keystoreCombo.setLabelSeparator(":");
        keystoreCombo.setEmptyText("Loading keystores...");
        keystoreCombo.disable();
        formPanel.add(keystoreCombo, formData);

        aliasField = new KapuaTextField<String>();
        aliasField.setAllowBlank(false);
        aliasField.setFieldLabel("* Alias");
        aliasField.setLabelSeparator(":");
        formPanel.add(aliasField, formData);

        algorithmField = new KapuaTextField<String>();
        algorithmField.setAllowBlank(false);
        algorithmField.setFieldLabel("* Algorithm");
        algorithmField.setLabelSeparator(":");
        formPanel.add(algorithmField, formData);

        sizeField = new KapuaNumberField();
        sizeField.setAllowBlank(false);
        sizeField.setAllowDecimals(false);
        sizeField.setAllowNegative(false);
        sizeField.setFieldLabel("* Key Size");
        sizeField.setLabelSeparator(":");
        formPanel.add(sizeField, formData);

        signatureAlgorithmField = new KapuaTextField<String>();
        signatureAlgorithmField.setAllowBlank(false);
        signatureAlgorithmField.setFieldLabel("* Signature Algorithm");
        signatureAlgorithmField.setLabelSeparator(":");
        formPanel.add(signatureAlgorithmField, formData);

        attributesField = new KapuaTextField<String>();
        attributesField.setAllowBlank(false);
        attributesField.setFieldLabel("* Attributes");
        attributesField.setLabelSeparator(":");
        formPanel.add(attributesField, formData);

        loadKeystores();
    }

    @Override
    public void submit() {
        GwtDeviceKeystoreKeypair deviceKeystoreKeypair = new GwtDeviceKeystoreKeypair();

        deviceKeystoreKeypair.setKeystoreId(keystoreCombo.getSimpleValue());
        deviceKeystoreKeypair.setAlias(aliasField.getValue());
        deviceKeystoreKeypair.setAlgorithm(algorithmField.getValue());
        deviceKeystoreKeypair.setSize(sizeField.getValue().intValue());
        deviceKeystoreKeypair.setSignatureAlgorithm(signatureAlgorithmField.getValue());
        deviceKeystoreKeypair.setAttributes(attributesField.getValue());

        GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreKeypair(xsrfToken, gwtDevice.getScopeId(), gwtDevice.getId(), deviceKeystoreKeypair, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable t) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                ConsoleInfo.display("Error", "Error while creating keypair on device: " + t.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                ConsoleInfo.display("Success", "Keypair created!");
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

    private void loadKeystores() {
        GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.getKeystores(gwtDevice.getScopeId(), gwtDevice.getId(), new AsyncCallback<List<GwtDeviceKeystore>>() {
            @Override
            public void onFailure(Throwable t) {
                keystoreCombo.setEmptyText("Error while loading device keystores!");
                keystoreCombo.enable();

                ConsoleInfo.display("Error", "Error while loading keystore item details: " + t.getMessage());
            }

            @Override
            public void onSuccess(List<GwtDeviceKeystore> gwtKeystores) {
                keystoreCombo.setEmptyText("Choose a keystore...");
                keystoreCombo.enable();

                for (GwtDeviceKeystore gwtKeystore : gwtKeystores) {
                    keystoreCombo.add(gwtKeystore.getId());
                }
            }
        });
    }
}
