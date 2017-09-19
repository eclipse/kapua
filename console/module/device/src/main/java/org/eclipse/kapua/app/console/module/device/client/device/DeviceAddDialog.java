/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceCreator;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates.GwtDeviceStatus;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

import java.util.List;

public class DeviceAddDialog extends EntityAddEditDialog {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    protected static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    protected final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);
    // protected final GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);
    protected final GwtGroupServiceAsync gwtGroupService = GWT.create(GwtGroupService.class);

    // General info fields
    protected LabelField clientIdLabel;
    protected TextField<String> clientIdField;
    protected ComboBox<GwtGroup> groupCombo;
    protected TextField<String> displayNameField;
    protected SimpleComboBox<GwtDeviceStatus> statusCombo;

    // Security Options fields
    // protected SimpleComboBox<String> credentialsTightCombo;
    // protected ComboBox<GwtUser> deviceUserCombo;
    // protected CheckBox allowCredentialsChangeCheckbox;

    // Custom attributes
    protected TextField<String> customAttribute1Field;
    protected TextField<String> customAttribute2Field;
    protected TextField<String> customAttribute3Field;
    protected TextField<String> customAttribute4Field;
    protected TextField<String> customAttribute5Field;

    protected NumberField optlock;

    protected static final GwtGroup NO_GROUP;

    static {
        NO_GROUP = new GwtGroup();
        NO_GROUP.setGroupName(DEVICE_MSGS.deviceFormNoGroup());
        NO_GROUP.setId(null);
    }

    // protected TextField<String> username;
    // protected TextField<String> password;
    // protected TextField<String> confirmPassword;
    // protected TextField<String> displayName;
    // protected TextField<String> email;
    // protected TextField<String> phoneNumber;
    // protected SimpleComboBox<GwtUser.GwtUserStatus> userStatus;
    // protected DateField expirationDate;
    // protected NumberField optlock;

    public DeviceAddDialog(GwtSession currentSession) {
        super(currentSession);

         DialogUtils.resizeDialog(this, 550, 450);
    }

    @Override
    public void createBody() {
        generateBody();

        // hide fields used for edit
        clientIdLabel.hide();
        statusCombo.hide();
    }

    protected void generateBody() {

        FormData formData = new FormData("-20");

        FormPanel formPanel = new FormPanel(FORM_LABEL_WIDTH);
        formPanel.setFrame(false);
        formPanel.setBodyBorder(false);
        formPanel.setHeaderVisible(false);
        // formPanel.setWidth(310);
        formPanel.setScrollMode(Scroll.AUTOY);
        // formPanel.setStyleAttribute("padding-bottom", "0px");
        // formPanel.setLayout(new FlowLayout());

        // Device general info fieldset
        FieldSet fieldSet = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        fieldSet.setLayout(layout);
        fieldSet.setHeading(DEVICE_MSGS.deviceFormFieldsetGeneralInfo());

        // Device Client ID
        clientIdLabel = new LabelField();
        clientIdLabel.setFieldLabel(DEVICE_MSGS.deviceFormClientID());
        clientIdLabel.setLabelSeparator(":");
        clientIdLabel.setWidth(225);
        fieldSet.add(clientIdLabel, formData);

        clientIdField = new TextField<String>();
        clientIdField.setAllowBlank(false);
        clientIdField.setName("clientID");
        clientIdField.setFieldLabel("* " + DEVICE_MSGS.deviceFormClientID());
        clientIdField.setValidator(new TextFieldValidator(clientIdField, FieldType.DEVICE_CLIENT_ID));
        clientIdField.setWidth(225);

        fieldSet.add(clientIdField, formData);

        // Display name
        displayNameField = new TextField<String>();
        displayNameField.setAllowBlank(true);
        displayNameField.setName("displayName");
        displayNameField.setFieldLabel(DEVICE_MSGS.deviceFormDisplayName());
        displayNameField.setWidth(225);
        fieldSet.add(displayNameField, formData);

        // Device Status
        statusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus>();
        statusCombo.setName("status");
        statusCombo.setFieldLabel(DEVICE_MSGS.deviceFormStatus());
        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);

        statusCombo.setEmptyText(DEVICE_MSGS.deviceFilteringPanelStatusEmptyText());
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.ENABLED);
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.DISABLED);

        fieldSet.add(statusCombo, formData);

        groupCombo = new ComboBox<GwtGroup>();
        groupCombo.setStore(new ListStore<GwtGroup>());
        groupCombo.setFieldLabel("* " + DEVICE_MSGS.deviceFormGroup());
        groupCombo.setForceSelection(true);
        groupCombo.setTypeAhead(false);
        groupCombo.setTriggerAction(TriggerAction.ALL);
        groupCombo.setAllowBlank(false);
        groupCombo.setEditable(false);
        groupCombo.setDisplayField("groupName");
        groupCombo.setValueField("id");

        gwtGroupService.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<List<GwtGroup>>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(List<GwtGroup> result) {
                groupCombo.getStore().removeAll();
                groupCombo.getStore().add(NO_GROUP);
                groupCombo.getStore().add(result);
            }
        });
        fieldSet.add(groupCombo, formData);

        // Tag fieldset
        // FieldSet fieldSetTags = new FieldSet();
        // FormLayout layoutTags = new FormLayout();
        // layoutTags.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        // fieldSetTags.setLayout(layoutTags);
        // fieldSetTags.setHeading(MSGS.deviceFormFieldsetTags());

        // Device Custom attributes fieldset
        FormLayout layoutSecurityOptions = new FormLayout();
        layoutSecurityOptions.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);

        // FieldSet fieldSetSecurityOptions = new FieldSet();
        // fieldSetSecurityOptions.setLayout(layoutSecurityOptions);
        // fieldSetSecurityOptions.setHeading(MSGS.deviceFormFieldsetSecurityOptions());

        // Provisioned Credentials Tight
        // credentialsTightCombo = new SimpleComboBox<String>();
        // credentialsTightCombo.setName("provisionedCredentialsTight");
        // credentialsTightCombo.setEditable(false);
        // credentialsTightCombo.setTypeAhead(false);
        // credentialsTightCombo.setAllowBlank(false);
        // credentialsTightCombo.setFieldLabel(MSGS.deviceFormProvisionedCredentialsTight());
        // credentialsTightCombo.setToolTip(MSGS.deviceFormProvisionedCredentialsTightTooltip());
        // credentialsTightCombo.setTriggerAction(TriggerAction.ALL);

        // fieldSetSecurityOptions.add(credentialsTightCombo, formData);

        // credentialsTightCombo.add(GwtDeviceCredentialsTight.INHERITED.getLabel());
        // credentialsTightCombo.add(GwtDeviceCredentialsTight.LOOSE.getLabel());
        // credentialsTightCombo.add(GwtDeviceCredentialsTight.STRICT.getLabel());
        //
        // credentialsTightCombo.setSimpleValue(GwtDeviceCredentialsTight.INHERITED.getLabel());

        // Device User
        // RpcProxy<ListLoadResult<GwtUser>> deviceUserProxy = new RpcProxy<ListLoadResult<GwtUser>>() {
        // @Override
        // protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtUser>> callback)
        // {
        // gwtUserService.findAll(m_currentSession.getSelectedAccount().getId(),
        // callback);
        // }
        // };

        // BaseListLoader<ListLoadResult<GwtUser>> deviceUserLoader = new BaseListLoader<ListLoadResult<GwtUser>>(deviceUserProxy);
        // ListStore<GwtUser> deviceUserStore = new ListStore<GwtUser>(deviceUserLoader);

        // deviceUserCombo = new ComboBox<GwtUser>();
        // deviceUserCombo.setName("deviceUserCombo");
        // deviceUserCombo.setEditable(false);
        // deviceUserCombo.setTypeAhead(false);
        // deviceUserCombo.setAllowBlank(false);
        // deviceUserCombo.setFieldLabel(MSGS.deviceFormDeviceUser());
        // deviceUserCombo.setTriggerAction(TriggerAction.ALL);
        // deviceUserCombo.setStore(deviceUserStore);
        // deviceUserCombo.setDisplayField("username");
        // deviceUserCombo.setValueField("id");
        // fieldSetSecurityOptions.add(deviceUserCombo, formData);

        // Allow credential change
        // allowCredentialsChangeCheckbox = new CheckBox();
        // allowCredentialsChangeCheckbox.setName("allowNewUnprovisionedDevicesCheckbox");
        // allowCredentialsChangeCheckbox.setFieldLabel(MSGS.deviceFormAllowCredentialsChange());
        // allowCredentialsChangeCheckbox.setToolTip(MSGS.deviceFormAllowCredentialsChangeTooltip());
        // allowCredentialsChangeCheckbox.setBoxLabel("");
        // allowCredentialsChangeCheckbox.hide();
        // fieldSetSecurityOptions.add(allowCredentialsChangeCheckbox, formData);

        // Device Custom attributes fieldset
        FieldSet fieldSetCustomAttributes = new FieldSet();
        FormLayout layoutCustomAttributes = new FormLayout();
        layoutCustomAttributes.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        fieldSetCustomAttributes.setLayout(layoutCustomAttributes);
        fieldSetCustomAttributes.setHeading(DEVICE_MSGS.deviceFormFieldsetCustomAttributes());

        // Custom Attribute #1
        customAttribute1Field = new TextField<String>();
        customAttribute1Field.setName("customAttribute1");
        customAttribute1Field.setFieldLabel(DEVICE_MSGS.deviceFormCustomAttribute1());
        customAttribute1Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute1Field, formData);

        // Custom Attribute #2
        customAttribute2Field = new TextField<String>();
        customAttribute2Field.setName("customAttribute2");
        customAttribute2Field.setFieldLabel(DEVICE_MSGS.deviceFormCustomAttribute2());
        customAttribute2Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute2Field, formData);

        // Custom Attribute #3
        customAttribute3Field = new TextField<String>();
        customAttribute3Field.setName("customAttribute3");
        customAttribute3Field.setFieldLabel(DEVICE_MSGS.deviceFormCustomAttribute3());
        customAttribute3Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute3Field, formData);

        // Custom Attribute #4
        customAttribute4Field = new TextField<String>();
        customAttribute4Field.setName("customAttribute4");
        customAttribute4Field.setFieldLabel(DEVICE_MSGS.deviceFormCustomAttribute4());
        customAttribute4Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute4Field, formData);

        // Custom Attribute #5
        customAttribute5Field = new TextField<String>();
        customAttribute5Field.setName("customAttribute5");
        customAttribute5Field.setFieldLabel(DEVICE_MSGS.deviceFormCustomAttribute5());
        customAttribute5Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute5Field, formData);

        // Optlock
        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        fieldSet.add(optlock, formData);

        formPanel.add(fieldSet);
        // m_formPanel.add(fieldSetTags);
        // m_formPanel.add(fieldSetSecurityOptions);
        formPanel.add(fieldSetCustomAttributes);

        // formPanel.setButtonAlign(HorizontalAlignment.CENTER);
        //
        // Button submitButton = new Button(MSGS.deviceFormSubmitButton());
        // submitButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
        //
        // @Override
        // public void handleEvent(BaseEvent be) {
        // // make sure all visible fields are valid before performing the action
        // for (Field<?> field : formPanel.getFields()) {
        // if (field.isVisible() && !field.isValid()) {
        // MessageBox.alert(MSGS.error(), MSGS.formErrors(), null);
        // return;
        // }
        // }
        //
        // if (selectedDevice == null) {
        //
        // final GwtDeviceCreator gwtDeviceCreator = new GwtDeviceCreator();
        // gwtDeviceCreator.setScopeId(currentSession.getSelectedAccount().getId());
        //
        // gwtDeviceCreator.setClientId(clientIdField.getValue());
        // gwtDeviceCreator.setGroupId(groupCombo.getValue().getId());
        // gwtDeviceCreator.setDisplayName(displayNameField.getValue());
        //
        // // Security Options
        // // gwtDeviceCreator.setGwtCredentialsTight(credentialsTightCombo.getSimpleValue());
        // // gwtDeviceCreator.setGwtPreferredUserId(deviceUserCombo.getValue().getId());
        //
        // // Custom attributes
        // gwtDeviceCreator.setCustomAttribute1(unescapeValue(customAttribute1Field.getValue()));
        // gwtDeviceCreator.setCustomAttribute2(unescapeValue(customAttribute2Field.getValue()));
        // gwtDeviceCreator.setCustomAttribute3(unescapeValue(customAttribute3Field.getValue()));
        // gwtDeviceCreator.setCustomAttribute4(unescapeValue(customAttribute4Field.getValue()));
        // gwtDeviceCreator.setCustomAttribute5(unescapeValue(customAttribute5Field.getValue()));
        //
        // //
        // // Getting XSRF token
        // gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
        //
        // @Override
        // public void onFailure(Throwable ex) {
        // FailureHandler.handle(ex);
        // }
        //
        // @Override
        // public void onSuccess(GwtXSRFToken token) {
        // gwtDeviceService.createDevice(token, gwtDeviceCreator, new AsyncCallback<GwtDevice>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // FailureHandler.handle(caught);
        // }
        //
        // public void onSuccess(final GwtDevice gwtDevice) {
        // //
        // // Getting XSRF token
        // gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
        //
        // @Override
        // public void onFailure(Throwable ex) {
        // FailureHandler.handle(ex);
        // }
        //
        // @Override
        // public void onSuccess(GwtXSRFToken token) {
        // hide();
        // ConsoleInfo.display(MSGS.info(), MSGS.deviceUpdateSuccess());
        // }
        // });
        //
        // }
        // });
        // }
        // });
        // }
        // // Edit
        // else {
        // // General info
        // selectedDevice.setDisplayName(unescapeValue(displayNameField.getValue()));
        // selectedDevice.setGwtDeviceStatus(statusCombo.getSimpleValue().name());
        // selectedDevice.setGroupId(groupCombo.getValue().getId());
        //
        // // Security Options
        // // m_selectedDevice.setCredentialsTight(GwtDeviceCredentialsTight.getEnumFromLabel(credentialsTightCombo.getSimpleValue()).name());
        // // m_selectedDevice.setCredentialsAllowChange(allowCredentialsChangeCheckbox.getValue());
        // // m_selectedDevice.setDeviceUserId(deviceUserCombo.getValue().getId());
        //
        // // Custom attributes
        // selectedDevice.setCustomAttribute1(unescapeValue(customAttribute1Field.getValue()));
        // selectedDevice.setCustomAttribute2(unescapeValue(customAttribute2Field.getValue()));
        // selectedDevice.setCustomAttribute3(unescapeValue(customAttribute3Field.getValue()));
        // selectedDevice.setCustomAttribute4(unescapeValue(customAttribute4Field.getValue()));
        // selectedDevice.setCustomAttribute5(unescapeValue(customAttribute5Field.getValue()));
        //
        // // Optlock
        // selectedDevice.setOptlock(optlock.getValue().intValue());
        //
        // //
        // // Getting XSRF token
        // gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
        //
        // @Override
        // public void onFailure(Throwable ex) {
        // FailureHandler.handle(ex);
        // }
        //
        // @Override
        // public void onSuccess(GwtXSRFToken token) {
        // gwtDeviceService.updateAttributes(token, selectedDevice, new AsyncCallback<GwtDevice>() {
        //
        // public void onFailure(Throwable caught) {
        // FailureHandler.handle(caught);
        // }
        //
        // public void onSuccess(GwtDevice gwtDevice) {
        // //
        // // Getting XSRF token
        // gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
        //
        // @Override
        // public void onFailure(Throwable ex) {
        // FailureHandler.handle(ex);
        // }
        //
        // @Override
        // public void onSuccess(GwtXSRFToken token) {
        // hide();
        // ConsoleInfo.display(MSGS.info(), selectedDevice == null ? MSGS.deviceCreationSuccess() : MSGS.deviceUpdateSuccess());
        // }
        // });
        //
        // }
        // });
        // }
        // });
        // }
        // }
        // });
        //
        // Button cancelButton = new Button(MSGS.deviceFormCancelButton());
        // cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
        //
        // @Override
        // public void handleEvent(BaseEvent be) {
        // hide();
        // }
        // });
        //
        // formPanel.addButton(submitButton);
        // formPanel.addButton(cancelButton);

        bodyPanel.add(formPanel);
    }

    @Override
    public void submit() {
        final GwtDeviceCreator gwtDeviceCreator = new GwtDeviceCreator();
        gwtDeviceCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtDeviceCreator.setClientId(clientIdField.getValue());
        gwtDeviceCreator.setGroupId(groupCombo.getValue().getId());
        gwtDeviceCreator.setDisplayName(displayNameField.getValue());

        // Security Options
        // gwtDeviceCreator.setGwtCredentialsTight(credentialsTightCombo.getSimpleValue());
        // gwtDeviceCreator.setGwtPreferredUserId(deviceUserCombo.getValue().getId());

        // Custom attributes
        gwtDeviceCreator.setCustomAttribute1(KapuaSafeHtmlUtils.htmlUnescape(customAttribute1Field.getValue()));
        gwtDeviceCreator.setCustomAttribute2(KapuaSafeHtmlUtils.htmlUnescape(customAttribute2Field.getValue()));
        gwtDeviceCreator.setCustomAttribute3(KapuaSafeHtmlUtils.htmlUnescape(customAttribute3Field.getValue()));
        gwtDeviceCreator.setCustomAttribute4(KapuaSafeHtmlUtils.htmlUnescape(customAttribute4Field.getValue()));
        gwtDeviceCreator.setCustomAttribute5(KapuaSafeHtmlUtils.htmlUnescape(customAttribute5Field.getValue()));

        //
        // Submit
        gwtDeviceService.createDevice(xsrfToken, gwtDeviceCreator, new AsyncCallback<GwtDevice>() {

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                // FIXME:
                exitMessage = MSGS.error();
                // exitMessage = MSGS.dialogAddError(caught.getLocalizedMessage());

                hide();
                //
                // FailureHandler.handle(caught);
            }

            public void onSuccess(final GwtDevice gwtDevice) {
                exitStatus = true;
                exitMessage = DEVICE_MSGS.deviceUpdateSuccess();
                hide();
            }
        });

        // GwtUserCreator gwtUserCreator = new GwtUserCreator();
        //
        // gwtUserCreator.setScopeId(currentSession.getSelectedAccount().getId());
        //
        // gwtUserCreator.setUsername(username.getValue());
        // gwtUserCreator.setPassword(password.getValue());
        // gwtUserCreator.setDisplayName(displayName.getValue());
        // gwtUserCreator.setEmail(email.getValue());
        // gwtUserCreator.setPhoneNumber(phoneNumber.getValue());
        // gwtUserCreator.setUserStatus(userStatus.getValue().getValue());
        // gwtUserCreator.setExpirationDate(expirationDate.getValue());
        //
        // gwtUserService.create(xsrfToken, gwtUserCreator, new AsyncCallback<GwtUser>() {
        //
        // @Override
        // public void onSuccess(GwtUser arg0) {
        // exitStatus = true;
        // exitMessage = MSGS.dialogAddConfirmation();
        // hide();
        // }
        //
        // @Override
        // public void onFailure(Throwable cause) {
        // unmask();
        //
        // submitButton.enable();
        // cancelButton.enable();
        // status.hide();
        //
        // exitStatus = false;
        // exitMessage = MSGS.dialogAddError(cause.getLocalizedMessage());
        //
        // hide();
        // }
        // });
    }

    @Override
    public String getHeaderMessage() {
        return DEVICE_MSGS.deviceFormHeadingNew();
    }

    @Override
    public String getInfoMessage() {
        // FIXME
        return null;
        // return MSGS.dialogAddInfo();
    }

}
