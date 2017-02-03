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
package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.Constants;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.TextFieldValidator;
import org.eclipse.kapua.app.console.client.util.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDevice.GwtDeviceCredentialsTight;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceCreator;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceForm extends Window
{

    private static final ConsoleMessages                             MSGS             = GWT.create(ConsoleMessages.class);

    private final GwtDeviceServiceAsync                              gwtDeviceService = GWT.create(GwtDeviceService.class);
    private final GwtUserServiceAsync                                gwtUserService   = GWT.create(GwtUserService.class);
    private final GwtSecurityTokenServiceAsync                       gwtXSRFService   = GWT.create(GwtSecurityTokenService.class);

    private FormPanel                                                m_formPanel;
    private GwtDevice                                                m_selectedDevice;
    private GwtSession                                               m_currentSession;

    // General info fields
    private LabelField                                               clientIdLabel;
    private TextField<String>                                        clientIdField;
    private TextField<String>                                        displayNameField;
    private SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus> statusCombo;

    // Security Options fields
    private SimpleComboBox<String>                                   credentialsTightCombo;
    private ComboBox<GwtUser>                                        deviceUserCombo;
    private CheckBox                                                 allowCredentialsChangeCheckbox;

    // Custom attributes
    private TextField<String>                                        customAttribute1Field;
    private TextField<String>                                        customAttribute2Field;
    private TextField<String>                                        customAttribute3Field;
    private TextField<String>                                        customAttribute4Field;
    private TextField<String>                                        customAttribute5Field;

    private NumberField                                              optlock;

    public DeviceForm(GwtSession currentSession)
    {
        this(null, currentSession);
    }

    public DeviceForm(GwtDevice gwtDevice, GwtSession currentSession)
    {
        m_selectedDevice = gwtDevice;
        m_currentSession = currentSession;

        setModal(true);
        setLayout(new FitLayout());
        setResizable(false);
        setHeading(m_selectedDevice == null ? MSGS.deviceFormHeadingNew()
                                            : MSGS.deviceFormHeadingEdit(m_selectedDevice.getDisplayName() != null ? m_selectedDevice.getDisplayName() : m_selectedDevice.getClientId()));

        DialogUtils.resizeDialog(this, 550, 570);
    }

    protected void onRender(Element parent, int index)
    {
        super.onRender(parent, index);

        FormData formData = new FormData("-20");

        m_formPanel = new FormPanel();
        m_formPanel.setFrame(false);
        m_formPanel.setBodyBorder(false);
        m_formPanel.setHeaderVisible(false);
        m_formPanel.setWidth(310);
        m_formPanel.setScrollMode(Scroll.AUTOY);
        m_formPanel.setStyleAttribute("padding-bottom", "0px");
        m_formPanel.setLayout(new FlowLayout());

        // Device general info fieldset
        FieldSet fieldSet = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        fieldSet.setLayout(layout);
        fieldSet.setHeading(MSGS.deviceFormFieldsetGeneralInfo());

        // Device Client ID
        clientIdLabel = new LabelField();
        clientIdLabel.setFieldLabel(MSGS.deviceFormClientID());
        clientIdLabel.setLabelSeparator(":");
        clientIdLabel.setWidth(225);
        fieldSet.add(clientIdLabel, formData);

        clientIdField = new TextField<String>();
        clientIdField.setAllowBlank(false);
        clientIdField.setName("clientID");
        clientIdField.setFieldLabel(MSGS.deviceFormClientID());
        clientIdField.setValidator(new TextFieldValidator(clientIdField, FieldType.DEVICE_CLIENT_ID));
        clientIdField.setWidth(225);

        fieldSet.add(clientIdField, formData);

        // Display name
        displayNameField = new TextField<String>();
        displayNameField.setAllowBlank(true);
        displayNameField.setName("displayName");
        displayNameField.setFieldLabel(MSGS.deviceFormDisplayName());
        displayNameField.setWidth(225);
        fieldSet.add(displayNameField, formData);

        // Device Status
        statusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus>();
        statusCombo.setName("status");
        statusCombo.setFieldLabel(MSGS.deviceFormStatus());
        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);

        statusCombo.setEmptyText(MSGS.deviceFilteringPanelStatusEmptyText());
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.ENABLED);
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.DISABLED);

        fieldSet.add(statusCombo, formData);

        // Tag fieldset
        FieldSet fieldSetTags = new FieldSet();
        FormLayout layoutTags = new FormLayout();
        layoutTags.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        fieldSetTags.setLayout(layoutTags);
        fieldSetTags.setHeading(MSGS.deviceFormFieldsetTags());

        ContentPanel panel = new ContentPanel();
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeaderVisible(false);
        panel.setLayout(new RowLayout(Orientation.HORIZONTAL));
        panel.setBodyStyle("background-color:transparent");
        panel.setHeight(35);

        // Device Custom attributes fieldset
        FormLayout layoutSecurityOptions = new FormLayout();
        layoutSecurityOptions.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);

        FieldSet fieldSetSecurityOptions = new FieldSet();
        fieldSetSecurityOptions.setLayout(layoutSecurityOptions);
        fieldSetSecurityOptions.setHeading(MSGS.deviceFormFieldsetSecurityOptions());

        // Provisioned Credentials Tight
        credentialsTightCombo = new SimpleComboBox<String>();
        credentialsTightCombo.setName("provisionedCredentialsTight");
        credentialsTightCombo.setEditable(false);
        credentialsTightCombo.setTypeAhead(false);
        credentialsTightCombo.setAllowBlank(false);
        credentialsTightCombo.setFieldLabel(MSGS.deviceFormProvisionedCredentialsTight());
        credentialsTightCombo.setToolTip(MSGS.deviceFormProvisionedCredentialsTightTooltip());
        credentialsTightCombo.setTriggerAction(TriggerAction.ALL);

        fieldSetSecurityOptions.add(credentialsTightCombo, formData);

        credentialsTightCombo.add(GwtDeviceCredentialsTight.INHERITED.getLabel());
        credentialsTightCombo.add(GwtDeviceCredentialsTight.LOOSE.getLabel());
        credentialsTightCombo.add(GwtDeviceCredentialsTight.STRICT.getLabel());

        credentialsTightCombo.setSimpleValue(GwtDeviceCredentialsTight.INHERITED.getLabel());

        // Device User
        RpcProxy<ListLoadResult<GwtUser>> deviceUserProxy = new RpcProxy<ListLoadResult<GwtUser>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtUser>> callback)
            {
                gwtUserService.findAll(m_currentSession.getSelectedAccount().getId(),
                                       callback);
            }
        };

        BaseListLoader<ListLoadResult<GwtUser>> deviceUserLoader = new BaseListLoader<ListLoadResult<GwtUser>>(deviceUserProxy);
        ListStore<GwtUser> deviceUserStore = new ListStore<GwtUser>(deviceUserLoader);

        deviceUserCombo = new ComboBox<GwtUser>();
        deviceUserCombo.setName("deviceUserCombo");
        deviceUserCombo.setEditable(false);
        deviceUserCombo.setTypeAhead(false);
        deviceUserCombo.setAllowBlank(false);
        deviceUserCombo.setFieldLabel(MSGS.deviceFormDeviceUser());
        deviceUserCombo.setTriggerAction(TriggerAction.ALL);
        deviceUserCombo.setStore(deviceUserStore);
        deviceUserCombo.setDisplayField("username");
        deviceUserCombo.setValueField("id");
        fieldSetSecurityOptions.add(deviceUserCombo, formData);

        // Allow credential change
        allowCredentialsChangeCheckbox = new CheckBox();
        allowCredentialsChangeCheckbox.setName("allowNewUnprovisionedDevicesCheckbox");
        allowCredentialsChangeCheckbox.setFieldLabel(MSGS.deviceFormAllowCredentialsChange());
        allowCredentialsChangeCheckbox.setToolTip(MSGS.deviceFormAllowCredentialsChangeTooltip());
        allowCredentialsChangeCheckbox.setBoxLabel("");
        fieldSetSecurityOptions.add(allowCredentialsChangeCheckbox, formData);

        // Device Custom attributes fieldset
        FieldSet fieldSetCustomAttributes = new FieldSet();
        FormLayout layoutCustomAttributes = new FormLayout();
        layoutCustomAttributes.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        fieldSetCustomAttributes.setLayout(layoutCustomAttributes);
        fieldSetCustomAttributes.setHeading(MSGS.deviceFormFieldsetCustomAttributes());

        // Custom Attribute #1
        customAttribute1Field = new TextField<String>();
        customAttribute1Field.setName("customAttribute1");
        customAttribute1Field.setFieldLabel("* " + MSGS.deviceFormCustomAttribute1());
        customAttribute1Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute1Field, formData);

        // Custom Attribute #2
        customAttribute2Field = new TextField<String>();
        customAttribute2Field.setName("customAttribute2");
        customAttribute2Field.setFieldLabel("* " + MSGS.deviceFormCustomAttribute2());
        customAttribute2Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute2Field, formData);

        // Custom Attribute #3
        customAttribute3Field = new TextField<String>();
        customAttribute3Field.setName("customAttribute3");
        customAttribute3Field.setFieldLabel("* " + MSGS.deviceFormCustomAttribute3());
        customAttribute3Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute3Field, formData);

        // Custom Attribute #4
        customAttribute4Field = new TextField<String>();
        customAttribute4Field.setName("customAttribute4");
        customAttribute4Field.setFieldLabel("* " + MSGS.deviceFormCustomAttribute4());
        customAttribute4Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute4Field, formData);

        // Custom Attribute #5
        customAttribute5Field = new TextField<String>();
        customAttribute5Field.setName("customAttribute5");
        customAttribute5Field.setFieldLabel("* " + MSGS.deviceFormCustomAttribute5());
        customAttribute5Field.setWidth(225);
        fieldSetCustomAttributes.add(customAttribute5Field, formData);

        // Optlock
        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        fieldSet.add(optlock, formData);

        m_formPanel.add(fieldSet);
        m_formPanel.add(fieldSetTags);
        m_formPanel.add(fieldSetSecurityOptions);
        m_formPanel.add(fieldSetCustomAttributes);

        m_formPanel.setButtonAlign(HorizontalAlignment.CENTER);

        Button submitButton = new Button(MSGS.deviceFormSubmitButton());
        submitButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be)
            {
                // make sure all visible fields are valid before performing the action
                for (Field<?> field : m_formPanel.getFields()) {
                    if (field.isVisible() && !field.isValid()) {
                        MessageBox.alert(MSGS.error(), MSGS.formErrors(), null);
                        return;
                    }
                }

                if (m_selectedDevice == null) {

                    final GwtDeviceCreator gwtDeviceCreator = new GwtDeviceCreator();
                    gwtDeviceCreator.setScopeId(m_currentSession.getSelectedAccount().getId());

                    gwtDeviceCreator.setClientId(clientIdField.getValue());
                    gwtDeviceCreator.setDisplayName(displayNameField.getValue());

                    // Security Options
                    gwtDeviceCreator.setGwtCredentialsTight(credentialsTightCombo.getSimpleValue());
                    gwtDeviceCreator.setGwtPreferredUserId(deviceUserCombo.getValue().getId());

                    // Custom attributes
                    gwtDeviceCreator.setCustomAttribute1(unescapeValue(customAttribute1Field.getValue()));
                    gwtDeviceCreator.setCustomAttribute2(unescapeValue(customAttribute2Field.getValue()));
                    gwtDeviceCreator.setCustomAttribute3(unescapeValue(customAttribute3Field.getValue()));
                    gwtDeviceCreator.setCustomAttribute4(unescapeValue(customAttribute4Field.getValue()));
                    gwtDeviceCreator.setCustomAttribute5(unescapeValue(customAttribute5Field.getValue()));

                    //
                    // Getting XSRF token
                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
                        @Override
                        public void onFailure(Throwable ex)
                        {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token)
                        {
                            gwtDeviceService.createDevice(token, gwtDeviceCreator, new AsyncCallback<GwtDevice>() {
                                @Override
                                public void onFailure(Throwable caught)
                                {
                                    FailureHandler.handle(caught);
                                }

                                public void onSuccess(final GwtDevice gwtDevice)
                                {
                                    //
                                    // Getting XSRF token
                                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                        @Override
                                        public void onFailure(Throwable ex)
                                        {
                                            FailureHandler.handle(ex);
                                        }

                                        @Override
                                        public void onSuccess(GwtXSRFToken token)
                                        {
                                            hide();
                                            ConsoleInfo.display(MSGS.info(), MSGS.deviceUpdateSuccess());
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
                // Edit
                else {
                    // General info
                    m_selectedDevice.setDisplayName(unescapeValue(displayNameField.getValue()));
                    m_selectedDevice.setGwtDeviceStatus(statusCombo.getSimpleValue().name());

                    // Security Options
                    m_selectedDevice.setCredentialsTight(GwtDeviceCredentialsTight.getEnumFromLabel(credentialsTightCombo.getSimpleValue()).name());
                    m_selectedDevice.setCredentialsAllowChange(allowCredentialsChangeCheckbox.getValue());
                    m_selectedDevice.setDeviceUserId(deviceUserCombo.getValue().getId());

                    // Custom attributes
                    m_selectedDevice.setCustomAttribute1(unescapeValue(customAttribute1Field.getValue()));
                    m_selectedDevice.setCustomAttribute2(unescapeValue(customAttribute2Field.getValue()));
                    m_selectedDevice.setCustomAttribute3(unescapeValue(customAttribute3Field.getValue()));
                    m_selectedDevice.setCustomAttribute4(unescapeValue(customAttribute4Field.getValue()));
                    m_selectedDevice.setCustomAttribute5(unescapeValue(customAttribute5Field.getValue()));

                    // Optlock
                    m_selectedDevice.setOptlock(optlock.getValue().intValue());

                    //
                    // Getting XSRF token
                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
                        @Override
                        public void onFailure(Throwable ex)
                        {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token)
                        {
                            gwtDeviceService.updateAttributes(token, m_selectedDevice, new AsyncCallback<GwtDevice>() {
                                public void onFailure(Throwable caught)
                                {
                                    FailureHandler.handle(caught);
                                }

                                public void onSuccess(GwtDevice gwtDevice)
                                {
                                    //
                                    // Getting XSRF token
                                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                        @Override
                                        public void onFailure(Throwable ex)
                                        {
                                            FailureHandler.handle(ex);
                                        }

                                        @Override
                                        public void onSuccess(GwtXSRFToken token)
                                        {
                                            hide();
                                            ConsoleInfo.display(MSGS.info(), m_selectedDevice == null ? MSGS.deviceCreationSuccess() : MSGS.deviceUpdateSuccess());
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
            }
        });

        Button cancelButton = new Button(MSGS.deviceFormCancelButton());
        cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be)
            {
                hide();
            }
        });

        m_formPanel.addButton(submitButton);
        m_formPanel.addButton(cancelButton);

        add(m_formPanel);

        // Hide components according to NEW/EDIT mode
        makeNewEditAppearance();

        // Populate fields if we are in EDIT mode
        if (m_selectedDevice != null) {
            populateFields();
        }
    }

    private void populateFields()
    {
        if (m_selectedDevice != null) {
            // General info data
            clientIdLabel.setText(m_selectedDevice.getClientId());
            displayNameField.setValue(m_selectedDevice.getUnescapedDisplayName());
            statusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceStatus.valueOf(m_selectedDevice.getGwtDeviceStatus()));

            // Security options data
            credentialsTightCombo.setSimpleValue(m_selectedDevice.getCredentialTightEnum().getLabel());
            allowCredentialsChangeCheckbox.setValue(m_selectedDevice.getCredentialsAllowChange());
            gwtUserService.find(m_currentSession.getSelectedAccount().getId(), m_selectedDevice.getDeviceUserId(), new AsyncCallback<GwtUser>() {
                @Override
                public void onFailure(Throwable caught)
                {
                    FailureHandler.handle(caught);
                }

                @Override
                public void onSuccess(GwtUser gwtUser)
                {
                    deviceUserCombo.setValue(gwtUser);
                }
            });

            // Custom attributes data
            customAttribute1Field.setValue(m_selectedDevice.getUnescapedCustomAttribute1());
            customAttribute2Field.setValue(m_selectedDevice.getUnescapedCustomAttribute2());
            customAttribute4Field.setValue(m_selectedDevice.getUnescapedCustomAttribute4());
            customAttribute3Field.setValue(m_selectedDevice.getUnescapedCustomAttribute3());
            customAttribute5Field.setValue(m_selectedDevice.getUnescapedCustomAttribute5());

            // Other data
            optlock.setValue(m_selectedDevice.getOptlock());
        }
    }

    private void makeNewEditAppearance()
    {
        // New
        if (m_selectedDevice == null) {
            clientIdLabel.hide();
            statusCombo.hide();
            allowCredentialsChangeCheckbox.hide();
        }
        // Edit
        else {
            clientIdField.hide();

            if (m_selectedDevice.getCredentialTightEnum().equals(GwtDeviceCredentialsTight.LOOSE)) {
                allowCredentialsChangeCheckbox.hide();
            }
            else {

                if (m_selectedDevice.getCredentialTightEnum().equals(GwtDeviceCredentialsTight.INHERITED)) {
                    allowCredentialsChangeCheckbox.hide();
                }
            }
        }
    }

    private String unescapeValue(String value)
    {
        return KapuaSafeHtmlUtils.htmlUnescape(value);
    }
}
