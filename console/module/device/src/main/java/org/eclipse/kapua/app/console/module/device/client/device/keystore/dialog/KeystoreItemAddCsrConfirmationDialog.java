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
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreCertificate;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

public class KeystoreItemAddCsrConfirmationDialog extends SimpleDialog {

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);

    private GwtDeviceKeystoreCertificate gwtKeystoreCertificate;

    private FormPanel formPanel;

    private LabelField keystoreIdLabel;
    private LabelField aliasLabel;
    private LabelField certificateLabel;
    private TextArea certificateTextArea;

    public KeystoreItemAddCsrConfirmationDialog(GwtDeviceKeystoreCertificate gwtKeystoreCertificate) {
        DialogUtils.resizeDialog(this, 500, 500);

        this.gwtKeystoreCertificate = gwtKeystoreCertificate;
    }

    @Override
    public String getInfoMessage() {
        return "Certificate signing request from the device";
    }

    @Override
    public String getHeaderMessage() {
        return "Device Certificate Signing Request";
    }

    @Override
    public void createBody() {
        bodyPanel.setHeight(600);

        FormData formData = new FormData("-10");

        formPanel = new FormPanel(120);
        bodyPanel.add(formPanel);

        certificateLabel = new LabelField();
        certificateLabel.setFieldLabel("Certificate");
        certificateLabel.setLabelSeparator(":");
        formPanel.add(certificateLabel, formData);

        keystoreIdLabel = new LabelField();
        keystoreIdLabel.setFieldLabel("Keystore Id");
        keystoreIdLabel.setLabelSeparator(":");
        formPanel.add(keystoreIdLabel, formData);

        aliasLabel = new LabelField();
        aliasLabel.setFieldLabel("Alias");
        aliasLabel.setLabelSeparator(":");
        formPanel.add(aliasLabel, formData);

        certificateTextArea = new TextArea();
        certificateTextArea.setReadOnly(true);
        certificateTextArea.setWidth(460);
        certificateTextArea.setHeight(300);
        certificateTextArea.setStyleAttribute("margin-left", "10px");
        bodyPanel.add(certificateTextArea);

        populateWithSigningRequestCertificate();
    }

    @Override
    public void submit() {
        hide();
    }

    @Override
    protected void addListeners() {
        // Nothing to do
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }

    private void populateWithSigningRequestCertificate() {
        keystoreIdLabel.setValue(gwtKeystoreCertificate.getKeystoreId());
        aliasLabel.setValue(gwtKeystoreCertificate.getAlias());
        certificateTextArea.setValue(gwtKeystoreCertificate.getCertificate());
    }
}
