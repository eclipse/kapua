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
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
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
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystore;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreCertificate;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

import java.util.List;

public class KeystoreItemAddCertificateRawDialog extends SimpleDialog {

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);

    private GwtDevice gwtDevice;

    private FormPanel formPanel;

    private SimpleComboBox<String> keystoreCombo;
    private KapuaTextField<String> aliasField;
    private LabelField certificateLabel;
    private TextArea certificateTextArea;

    public KeystoreItemAddCertificateRawDialog(GwtDevice gwtDevice) {
        this.gwtDevice = gwtDevice;
        DialogUtils.resizeDialog(this, 500, 580);
    }

    @Override
    public String getInfoMessage() {
        return "Provide the information of the certificate. Adding a new certificate may affect other device applications that are using keystores.";
    }

    @Override
    public String getHeaderMessage() {
        return "Add new certificate";
    }

    @Override
    public void createBody() {
        bodyPanel.setHeight(600);

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

        certificateLabel = new LabelField();
        certificateLabel.setFieldLabel("* Certificate");
        certificateLabel.setLabelSeparator(":");
        formPanel.add(certificateLabel, formData);

        certificateTextArea = new TextArea();
        certificateTextArea.setAllowBlank(false);
        certificateTextArea.setWidth(460);
        certificateTextArea.setHeight(370);
        certificateTextArea.setStyleAttribute("margin-left", "10px");
        bodyPanel.add(certificateTextArea);

        loadKeystores();
    }

    @Override
    public void submit() {
        GwtDeviceKeystoreCertificate gwtKeystoreCertificate = new GwtDeviceKeystoreCertificate();

        gwtKeystoreCertificate.setKeystoreId(keystoreCombo.getSimpleValue());
        gwtKeystoreCertificate.setAlias(aliasField.getValue());
        gwtKeystoreCertificate.setCertificate(certificateTextArea.getValue());

        GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreCertificateRaw(xsrfToken, gwtDevice.getScopeId(), gwtDevice.getId(), gwtKeystoreCertificate, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable t) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                ConsoleInfo.display("Error", "Error while creating certificate on device: " + t.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                ConsoleInfo.display("Success", "Certificate created!");
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
