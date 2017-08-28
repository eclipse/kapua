/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.packages;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.TabbedDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageInstallRequest;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementServiceAsync;

public class PackageInstallDialog extends TabbedDialog {

    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);
    private static final GwtDeviceManagementServiceAsync GWT_DEVICE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceManagementService.class);

    private String scopeId;
    private String deviceId;

    private TabItem packageInfoTab;
    private TabItem operationOptionsTab;

    private FormPanel packageInfoForm;
    private Text dpInfoText;
    private TextField<String> dpURIField;
    private TextField<String> dpNameField;
    private TextField<String> dpVersionField;

    private FormPanel operationOptionsForm;
    private Text operationInfoText;
    private CheckBox operationRebootField;
    private NumberField operationRebootDelayField;

    public PackageInstallDialog(String scopeId, String deviceId) {
        super();

        this.scopeId = scopeId;
        this.deviceId = deviceId;

        DialogUtils.resizeDialog(this, 490, 250);
    }

    @Override
    public String getInfoMessage() {
        return null;
    }

    @Override
    public void createTabItems() {
        // Deployment package info tab content
        {
            FormData formData = new FormData("-15");

            FormLayout formLayout = new FormLayout();
            formLayout.setLabelWidth(FORM_LABEL_WIDTH);

            packageInfoForm = new FormPanel();
            packageInfoForm.setFrame(false);
            packageInfoForm.setHeaderVisible(false);
            packageInfoForm.setBodyBorder(false);
            packageInfoForm.setBorders(false);
            packageInfoForm.setLayout(formLayout);

            dpInfoText = new Text();
            dpInfoText.setText(DEVICE_MSGS.packageInstallDpInfo());
            dpInfoText.setStyleAttribute("margin-bottom", "10px");
            packageInfoForm.add(dpInfoText);

            dpURIField = new TextField<String>();
            dpURIField.setName("dpUri");
            dpURIField.setAllowBlank(false);
            dpURIField.setFieldLabel("* " + DEVICE_MSGS.packageInstallDpTabUri());
            packageInfoForm.add(dpURIField, formData);

            dpNameField = new TextField<String>();
            dpNameField.setName("dpName");
            dpNameField.setAllowBlank(false);
            dpNameField.setFieldLabel("* " + DEVICE_MSGS.packageInstallDpTabName());
            packageInfoForm.add(dpNameField, formData);

            dpVersionField = new TextField<String>();
            dpVersionField.setName("dpVersion");
            dpVersionField.setAllowBlank(false);
            dpVersionField.setFieldLabel("* " + DEVICE_MSGS.packageInstallDpTabVersion());
            packageInfoForm.add(dpVersionField, formData);
        }

        // Operation options tab content
        {
            FormData formData = new FormData("-10");

            FormLayout formLayout = new FormLayout();
            formLayout.setLabelWidth(FORM_LABEL_WIDTH);

            operationOptionsForm = new FormPanel();
            operationOptionsForm.setFrame(false);
            operationOptionsForm.setHeaderVisible(false);
            operationOptionsForm.setBodyBorder(false);
            operationOptionsForm.setBorders(false);
            operationOptionsForm.setLayout(formLayout);

            operationInfoText = new Text();
            operationInfoText.setText(DEVICE_MSGS.packageInstallOperationInfo());
            operationInfoText.setStyleAttribute("margin-bottom", "5px");
            operationOptionsForm.add(operationInfoText);

            operationRebootField = new CheckBox();
            operationRebootField.setName("reboot");
            operationRebootField.setFieldLabel(DEVICE_MSGS.packageInstallOperationTabReboot());
            operationRebootField.setBoxLabel("");

            operationRebootField.addListener(Events.Change, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    if (operationRebootField.getValue()) {
                        operationRebootDelayField.enable();
                    } else {
                        operationRebootDelayField.disable();
                    }
                }
            });

            operationOptionsForm.add(operationRebootField, formData);

            operationRebootDelayField = new NumberField();
            operationRebootDelayField.setName("installRebootDelay");
            operationRebootDelayField.setFieldLabel(DEVICE_MSGS.packageInstallOperationTabRebootDelay());
            operationRebootDelayField.setEmptyText("0");
            operationRebootDelayField.setAllowDecimals(false);
            operationRebootDelayField.setAllowNegative(false);
            operationOptionsForm.add(operationRebootDelayField, formData);
        }

        //
        // Tabs setup
        packageInfoTab = new TabItem(DEVICE_MSGS.packageInstallDpTabTitle());
        packageInfoTab.setBorders(false);
        packageInfoTab.setLayout(new FormLayout());
        packageInfoTab.add(packageInfoForm);
        tabsPanel.add(packageInfoTab);

        operationOptionsTab = new TabItem(DEVICE_MSGS.packageInstallOperationTabTitle());
        operationOptionsTab.setBorders(false);
        operationOptionsTab.setLayout(new FormLayout());
        operationOptionsTab.add(operationOptionsForm);
        tabsPanel.add(operationOptionsTab);
    }

    @Override
    public void preSubmit() {
        if (!packageInfoForm.isValid()) {
            tabsPanel.setSelection(packageInfoTab);
            return;
        }

        if (!operationOptionsForm.isValid()) {
            tabsPanel.setSelection(operationOptionsTab);
            return;
        }

        super.preSubmit();
    }

    @Override
    public void submit() {
        GwtPackageInstallRequest gwtPackageInstallRequest = new GwtPackageInstallRequest();

        // General info
        gwtPackageInstallRequest.setScopeId(scopeId);
        gwtPackageInstallRequest.setDeviceId(deviceId);

        // Operation configuration
        gwtPackageInstallRequest.setPackageURI(dpURIField.getValue());
        gwtPackageInstallRequest.setPackageName(dpNameField.getValue());
        gwtPackageInstallRequest.setPackageVersion(dpVersionField.getValue());

        gwtPackageInstallRequest.setReboot(operationRebootField.getValue());

        Number nValue = operationRebootDelayField.getValue();
        if (nValue != null) {
            gwtPackageInstallRequest.setRebootDelay(nValue.intValue() * 1000);
        }

        GWT_DEVICE_MANAGEMENT_SERVICE.installPackage(xsrfToken, gwtPackageInstallRequest, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                exitStatus = true;
                exitMessage = DEVICE_MSGS.packageInstallSuccess();
                hide();
            }

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                exitMessage = caught.getMessage();
                hide();
            }
        });
    }

    @Override
    protected void addListeners() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getHeaderMessage() {
        return DEVICE_MSGS.packageInstallNewPackage();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }
}
