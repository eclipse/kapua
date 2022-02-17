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
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreItem;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

public class KeystoreItemDetailsDialog extends SimpleDialog {

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);

    private GwtDevice gwtDevice;
    private GwtDeviceKeystoreItem gwtKeystoreItem;

    private FormPanel formPanel;

    private LabelField keystoreIdLabel;
    private LabelField aliasLabel;
    private LabelField itemTypeLabel;
    private LabelField algorithmLabel;
    private LabelField sizeLabel;
    private LabelField subjectDNLabel;
    private LabelField issuerLabel;
    private LabelField notBeforeLabel;
    private LabelField notAfterLabel;
    private LabelField certificateLabel;
    private TextArea certificateTextArea;

    public KeystoreItemDetailsDialog(GwtDevice gwtDevice, GwtDeviceKeystoreItem gwtKeystoreItem) {
        this.gwtDevice = gwtDevice;
        this.gwtKeystoreItem = gwtKeystoreItem;

        DialogUtils.resizeDialog(this, 500, 570);
    }

    @Override
    public String getInfoMessage() {
        return "Details of the selected keystore item";
    }

    @Override
    public String getHeaderMessage() {
        return "Keystore item: " + gwtKeystoreItem.getAlias();
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

        itemTypeLabel = new LabelField();
        itemTypeLabel.setFieldLabel("Item Type");
        itemTypeLabel.setLabelSeparator(":");
        formPanel.add(itemTypeLabel, formData);

        algorithmLabel = new LabelField();
        algorithmLabel.setFieldLabel("Algorithm");
        algorithmLabel.setLabelSeparator(":");
        formPanel.add(algorithmLabel, formData);

        sizeLabel = new LabelField();
        sizeLabel.setFieldLabel("Size");
        sizeLabel.setLabelSeparator(":");
        formPanel.add(sizeLabel, formData);

        subjectDNLabel = new LabelField();
        subjectDNLabel.setFieldLabel("Subject DN");
        subjectDNLabel.setLabelSeparator(":");
        formPanel.add(subjectDNLabel, formData);

        issuerLabel = new LabelField();
        issuerLabel.setFieldLabel("Issuer");
        issuerLabel.setLabelSeparator(":");
        formPanel.add(issuerLabel, formData);

        notBeforeLabel = new LabelField();
        notBeforeLabel.setFieldLabel("Not Before");
        notBeforeLabel.setLabelSeparator(":");
        formPanel.add(notBeforeLabel, formData);

        notAfterLabel = new LabelField();
        notAfterLabel.setFieldLabel("Not After");
        notAfterLabel.setLabelSeparator(":");
        formPanel.add(notAfterLabel, formData);

        certificateLabel = new LabelField();
        certificateLabel.setFieldLabel("Certificate");
        certificateLabel.setLabelSeparator(":");
        formPanel.add(certificateLabel, formData);

        certificateTextArea = new TextArea();
        certificateTextArea.setReadOnly(true);
        certificateTextArea.setWidth(460);
        certificateTextArea.setHeight(210);
        certificateTextArea.setStyleAttribute("margin-left", "10px");
        certificateTextArea.hide();
        bodyPanel.add(certificateTextArea);

        loadKeystoreItemDetails();
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

    private void loadKeystoreItemDetails() {
        bodyPanel.mask("Loading details...");

        GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.getKeystoreItem(gwtDevice.getScopeId(), gwtDevice.getId(), gwtKeystoreItem.getKeystoreId(), gwtKeystoreItem.getAlias(), new AsyncCallback<GwtDeviceKeystoreItem>() {
            @Override
            public void onFailure(Throwable t) {
                bodyPanel.unmask();
                ConsoleInfo.display("Error", "Error while loading keystore item details: " + t.getMessage());
            }

            @Override
            public void onSuccess(GwtDeviceKeystoreItem gwtKeystoreItem) {
                bodyPanel.unmask();

                keystoreIdLabel.setValue(gwtKeystoreItem.getKeystoreId() != null ? gwtKeystoreItem.getKeystoreId() : "N/A");
                aliasLabel.setValue(gwtKeystoreItem.getAlias() != null ? gwtKeystoreItem.getAlias() : "N/A");
                itemTypeLabel.setValue(gwtKeystoreItem.getItemType() != null ? gwtKeystoreItem.getItemType() : "N/A");
                algorithmLabel.setValue(gwtKeystoreItem.getAlgorithm() != null ? gwtKeystoreItem.getAlgorithm() : "N/A");
                sizeLabel.setValue(gwtKeystoreItem.getSize() != null ? gwtKeystoreItem.getSize() : "N/A");
                subjectDNLabel.setValue(gwtKeystoreItem.getSubjectDN() != null ? gwtKeystoreItem.getSubjectDN() : "N/A");
                issuerLabel.setValue(gwtKeystoreItem.getIssuer() != null ? gwtKeystoreItem.getIssuer() : "N/A");
                notBeforeLabel.setValue(gwtKeystoreItem.getNotBefore() != null ? gwtKeystoreItem.getNotBeforeFormatted() : "N/A");
                notAfterLabel.setValue(gwtKeystoreItem.getNotAfter() != null ? gwtKeystoreItem.getNotAfterFormatted() : "N/A");

                if (gwtKeystoreItem.getCertificate() != null) {
                    certificateTextArea.setValue(gwtKeystoreItem.getCertificate());
                    certificateTextArea.show();
                } else {
                    certificateLabel.setValue("N/A");
                    certificateTextArea.hide();
                }
            }
        });
    }
}
