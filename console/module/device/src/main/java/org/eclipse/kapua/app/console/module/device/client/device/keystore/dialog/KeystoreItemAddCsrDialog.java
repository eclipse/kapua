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

import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreCertificate;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreCsr;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreItem;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

public class KeystoreItemAddCsrDialog extends SimpleDialog {

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);

    private GwtDevice gwtDevice;
    private GwtDeviceKeystoreItem gwtKeystoreItem;

    private FormPanel formPanel;

    private LabelField keystoreIdLabel;
    private LabelField aliasLabel;
    private KapuaTextField<String> signatureAlgorithmField;
    private KapuaTextField<String> attributesField;

    public KeystoreItemAddCsrDialog(GwtDevice gwtDevice, GwtDeviceKeystoreItem gwtKeystoreItem) {
        this.gwtDevice = gwtDevice;
        this.gwtKeystoreItem = gwtKeystoreItem;

        DialogUtils.resizeDialog(this, 500, 350);
    }

    @Override
    public String getInfoMessage() {
        return "Provide the information for the certificate signing request.";
    }

    @Override
    public String getHeaderMessage() {
        return "Create certificate signing request";
    }

    @Override
    public void createBody() {
        bodyPanel.setHeight(600);

        FormData formData = new FormData("-10");

        formPanel = new FormPanel(120);
        bodyPanel.add(formPanel);

        keystoreIdLabel = new LabelField();
        keystoreIdLabel.setFieldLabel("Keystore Id");
        keystoreIdLabel.setLabelSeparator(":");
        formPanel.add(keystoreIdLabel, formData);

        aliasLabel = new LabelField();
        aliasLabel.setFieldLabel("Alias");
        aliasLabel.setLabelSeparator(":");
        formPanel.add(aliasLabel, formData);

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

        populateWithSelectedItem();
    }

    @Override
    public void submit() {
        GwtDeviceKeystoreCsr deviceKeystoreCsr = new GwtDeviceKeystoreCsr();

        deviceKeystoreCsr.setKeystoreId(keystoreIdLabel.getValue().toString());
        deviceKeystoreCsr.setAlias(aliasLabel.getValue().toString());
        deviceKeystoreCsr.setSignatureAlgorithm(signatureAlgorithmField.getValue());
        deviceKeystoreCsr.setAttributes(attributesField.getValue());

        GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreCsr(xsrfToken, gwtDevice.getScopeId(), gwtDevice.getId(), deviceKeystoreCsr, new AsyncCallback<GwtDeviceKeystoreCertificate>() {
            @Override
            public void onFailure(Throwable t) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                ConsoleInfo.display("Error", "Error while creating keypair on device: " + t.getMessage());
            }

            @Override
            public void onSuccess(GwtDeviceKeystoreCertificate result) {
                KeystoreItemAddCsrConfirmationDialog csrConfirmationDialog = new KeystoreItemAddCsrConfirmationDialog(result);
                csrConfirmationDialog.show();

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

    private void populateWithSelectedItem() {
        keystoreIdLabel.setValue(gwtKeystoreItem.getKeystoreId());
        aliasLabel.setValue(gwtKeystoreItem.getAlias());
    }
}
