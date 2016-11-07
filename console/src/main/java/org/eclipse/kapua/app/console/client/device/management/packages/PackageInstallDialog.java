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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device.management.packages;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.dialog.TabbedDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.device.management.packages.GwtPackageInstallRequest;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementServiceAsync;

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
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PackageInstallDialog extends TabbedDialog {

    private static final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT.create(GwtDeviceManagementService.class);

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
            dpInfoText.setText(MSGS.packageInstallDpInfo());
            dpInfoText.setStyleAttribute("margin-bottom", "10px");
            packageInfoForm.add(dpInfoText);

            dpURIField = new TextField<String>();
            dpURIField.setName("dpUri");
            dpURIField.setAllowBlank(false);
            dpURIField.setFieldLabel("* " + MSGS.packageInstallDpTabUri());
            packageInfoForm.add(dpURIField, formData);

            dpNameField = new TextField<String>();
            dpNameField.setName("dpName");
            dpNameField.setAllowBlank(false);
            dpNameField.setFieldLabel("* " + MSGS.packageInstallDpTabName());
            packageInfoForm.add(dpNameField, formData);

            dpVersionField = new TextField<String>();
            dpVersionField.setName("dpVersion");
            dpVersionField.setAllowBlank(false);
            dpVersionField.setFieldLabel("* " + MSGS.packageInstallDpTabVersion());
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
            operationInfoText.setText(MSGS.packageInstallOperationInfo());
            operationInfoText.setStyleAttribute("margin-bottom", "5px");
            operationOptionsForm.add(operationInfoText);

            operationRebootField = new CheckBox();
            operationRebootField.setName("reboot");
            operationRebootField.setFieldLabel(MSGS.packageInstallOperationTabReboot());
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
            operationRebootDelayField.setFieldLabel(MSGS.packageInstallOperationTabRebootDelay());
            operationRebootDelayField.setEmptyText("0");
            operationRebootDelayField.setAllowDecimals(false);
            operationRebootDelayField.setAllowNegative(false);
            operationOptionsForm.add(operationRebootDelayField, formData);
        }

        //
        // Tabs setup
        packageInfoTab = new TabItem(MSGS.packageInstallDpTabTitle());
        packageInfoTab.setBorders(false);
        packageInfoTab.setLayout(new FormLayout());
        packageInfoTab.add(packageInfoForm);
        m_tabsPanel.add(packageInfoTab);

        operationOptionsTab = new TabItem(MSGS.packageInstallOperationTabTitle());
        operationOptionsTab.setBorders(false);
        operationOptionsTab.setLayout(new FormLayout());
        operationOptionsTab.add(operationOptionsForm);
        m_tabsPanel.add(operationOptionsTab);
    }

    @Override
    public void preSubmit() {
        if (!packageInfoForm.isValid()) {
            m_tabsPanel.setSelection(packageInfoTab);
            return;
        }

        if (!operationOptionsForm.isValid()) {
            m_tabsPanel.setSelection(operationOptionsTab);
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

        gwtDeviceManagementService.installPackage(xsrfToken, gwtPackageInstallRequest, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                m_exitStatus = true;
                m_exitMessage = MSGS.packageInstallSuccess();
                hide();
            }

            @Override
            public void onFailure(Throwable caught) {
                m_exitStatus = false;
                m_exitMessage = caught.getMessage();
                hide();
            }
        });
    }

    @Override
    protected void addListeners() {
        // TODO Auto-generated method stub

    }

    @Override
    public AbstractImagePrototype getHeaderIcon() {
        return null;
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.packageInstallNewPackage();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }
}
