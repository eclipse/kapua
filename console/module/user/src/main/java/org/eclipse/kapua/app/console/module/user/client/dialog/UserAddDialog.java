/*******************************************************************************
 * Copyright (c) 2011, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client.dialog;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.shared.model.permission.CredentialSessionPermission;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser.GwtUserStatus;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserCreator;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserAddDialog extends EntityAddEditDialog {

    protected static final ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);
    protected static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);

    protected FieldSet infoFieldSet;
    protected KapuaTextField<String> username;
    protected LabelField usernameLabel;
    protected KapuaTextField<String> externalId;
    protected LabelField externalIdLabel;
    protected KapuaTextField<String> password;
    protected KapuaTextField<String> confirmPassword;
    protected LabelField passwordTooltip;
    protected KapuaTextField<String> displayName;
    protected KapuaTextField<String> email;
    protected KapuaTextField<String> phoneNumber;
    protected SimpleComboBox<GwtUser.GwtUserStatus> userStatus;
    protected LabelField userType;
    protected KapuaDateField expirationDate;
    protected NumberField optlock;

    protected RadioGroup userRadioGroup = new RadioGroup();

    private String specificAccountId;
    private Boolean passwordIsShown = false;
    private Boolean externalIdIsShown = false;

    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    public UserAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 500);
    }

    public UserAddDialog(GwtSession currentSession, String specificAccountId) {
        this(currentSession);
        this.specificAccountId = specificAccountId;
    }

    private enum RadioGroupStatus {
        INTERNAL, EXTERNAL
    }

    @Override
    public void createBody() {

        submitButton.disable();
        FormPanel userFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        userFormPanel.setFrame(false);
        userFormPanel.setBorders(false);
        userFormPanel.setBodyBorder(false);
        userFormPanel.setHeaderVisible(false);
        userFormPanel.setPadding(0);

        final FormData subFieldsetFormData = new FormData();
        FormData subFormData = new FormData();

        //
        // User info tab
        //
        infoFieldSet = new FieldSet();
        infoFieldSet.setHeading(USER_MSGS.dialogAddFieldSet());
        infoFieldSet.setBorders(true);
        infoFieldSet.setStyleAttribute("margin", "0px 10px 0px 10px");

        FieldSet statusFieldSet = new FieldSet();
        statusFieldSet.setBorders(true);
        statusFieldSet.setStyleAttribute("margin", "5px 10px 0px 10px");
        statusFieldSet.setHeading(USER_MSGS.dialogAddStatus());

        FormLayout userLayout = new FormLayout();
        FormLayout statusLayout = new FormLayout();
        userLayout.setLabelWidth(Constants.LABEL_WIDTH_FORM);
        statusLayout.setLabelWidth(Constants.LABEL_WIDTH_FORM);
        infoFieldSet.setLayout(userLayout);
        infoFieldSet.setStyleAttribute("background-color", "E8E8E8");
        statusFieldSet.setLayout(statusLayout);
        statusFieldSet.setStyleAttribute("background-color", "E8E8E8");

        usernameLabel = new LabelField();
        usernameLabel.setFieldLabel(USER_MSGS.dialogAddFieldUsername());
        usernameLabel.setLabelSeparator(":");
        usernameLabel.setVisible(false);
        infoFieldSet.add(usernameLabel);

        username = new KapuaTextField<String>();
        username.setAllowBlank(false);
        username.setMaxLength(255);
        username.setName("userName");
        username.setFieldLabel("* " + USER_MSGS.dialogAddFieldUsername());
        username.setValidator(new TextFieldValidator(username, FieldType.NAME));
        username.setToolTip(USER_MSGS.dialogAddFieldNameTooltip());
        infoFieldSet.add(username, subFieldsetFormData);

        externalIdLabel = new LabelField();
        externalIdLabel.setFieldLabel(USER_MSGS.dialogAddFieldExternalId());
        externalIdLabel.setLabelSeparator(":");
        externalIdLabel.setVisible(false);

        externalId = new KapuaTextField<String>();
        externalId.setAllowBlank(false);
        externalId.setMaxLength(255);
        externalId.setName("externalId");
        externalId.setFieldLabel("* " + USER_MSGS.dialogAddFieldExternalId());
        // Not using a TextFieldValidator for the externalId, since the  external id format can change between
        // different providers.
        externalId.setToolTip(USER_MSGS.dialogAddFieldExternalIdTooltip());

        password = new KapuaTextField<String>();
        password.setAllowBlank(false);
        password.setName("password");
        password.setFieldLabel("* " + USER_MSGS.dialogAddFieldPassword());
        password.setValidator(new PasswordFieldValidator(password));
        password.setToolTip(USER_MSGS.dialogAddTooltipPassword());
        password.setPassword(true);
        password.setMaxLength(255);

        confirmPassword = new KapuaTextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + USER_MSGS.dialogAddFieldConfirmPassword());
        confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, password));
        confirmPassword.setToolTip(USER_MSGS.dialogAddTooltipPassword());
        confirmPassword.setPassword(true);
        confirmPassword.setMaxLength(255);

        passwordTooltip = new LabelField();
        passwordTooltip.setValue(USER_MSGS.dialogAddTooltipPassword());
        passwordTooltip.setStyleAttribute("margin-top", "-5px");
        passwordTooltip.setStyleAttribute("color", "gray");
        passwordTooltip.setStyleAttribute("font-size", "10px");

        userRadioGroup.setFieldLabel(USER_MSGS.dialogAddFieldUserTypeRadioButton());
        userRadioGroup.setOrientation(Style.Orientation.HORIZONTAL);
        userRadioGroup.setSelectionRequired(true);
        userRadioGroup.setStyleAttribute("margin-left", "5px");
        userRadioGroup.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent baseEvent) {
                Radio radio = ((RadioGroup) baseEvent.getSource()).getValue();
                if (radio.getValueAttribute().equals(RadioGroupStatus.INTERNAL.name())) {
                    //DialogUtils.resizeDialog(UserAddDialog.this, 400, 470);

                    password.show();
                    password.enable();
                    confirmPassword.show();
                    confirmPassword.enable();
                    passwordTooltip.show();
                    passwordTooltip.enable();
                    passwordIsShown = true;

                    externalId.hide();
                    externalId.disable();
                    externalIdIsShown = false;
                } else if (radio.getValueAttribute().equals(RadioGroupStatus.EXTERNAL.name())) {
                    //DialogUtils.resizeDialog(UserAddDialog.this, 400, 100);
                    //UserAddDialog.this.layout();

                    password.hide();
                    password.disable();
                    confirmPassword.hide();
                    confirmPassword.disable();
                    passwordTooltip.hide();
                    passwordTooltip.disable();
                    passwordIsShown = false;

                    externalId.show();
                    externalId.enable();
                    externalIdIsShown = true;

                }
            }
        });

        Radio internalRadio = new Radio();
        internalRadio.setValueAttribute(RadioGroupStatus.INTERNAL.name());
        internalRadio.setBoxLabel(USER_MSGS.dialogAddFieldInternalUser());
        internalRadio.setValue(true);
        Radio externalRadio = new Radio();
        externalRadio.setValueAttribute(RadioGroupStatus.EXTERNAL.name());
        externalRadio.setBoxLabel(USER_MSGS.dialogAddFieldExternalUser());
        userRadioGroup.add(internalRadio);
        userRadioGroup.add(externalRadio);
        infoFieldSet.add(userRadioGroup);

        infoFieldSet.add(externalId, subFieldsetFormData);
        externalId.hide();
        externalId.disable();
        infoFieldSet.add(externalIdLabel);

        if (currentSession.hasPermission(CredentialSessionPermission.write())) {
            infoFieldSet.add(password, subFieldsetFormData);
            infoFieldSet.add(confirmPassword, subFieldsetFormData);
            infoFieldSet.add(passwordTooltip);
        }

        userRadioGroup.hide();
        userRadioGroup.disable();
        if (currentSession.isSsoEnabled()) {
            userRadioGroup.show();
            userRadioGroup.enable();
        } else {
            passwordIsShown = true;
            password.show();
            password.enable();
            confirmPassword.show();
            confirmPassword.enable();
            passwordTooltip.show();
            passwordTooltip.enable();
        }

        displayName = new KapuaTextField<String>();
        displayName.setName("displayName");
        displayName.setFieldLabel(USER_MSGS.dialogAddFieldDisplayName());
        displayName.setToolTip(USER_MSGS.dialogAddFieldDisplayNameTooltip());
        displayName.setMaxLength(255);
        infoFieldSet.add(displayName, subFieldsetFormData);

        email = new KapuaTextField<String>();
        email.setName("userEmail");
        email.setFieldLabel(USER_MSGS.dialogAddFieldEmail());
        email.setValidator(new TextFieldValidator(email, FieldType.EMAIL));
        email.setToolTip(USER_MSGS.dialogAddFieldEmailTooltip());
        email.setMaxLength(255);
        infoFieldSet.add(email, subFieldsetFormData);

        phoneNumber = new KapuaTextField<String>();
        phoneNumber.setName("phoneNumber");
        phoneNumber.setFieldLabel(USER_MSGS.dialogAddFieldPhoneNumber());
        phoneNumber.setValidator(new TextFieldValidator(phoneNumber, FieldType.PHONE));
        phoneNumber.setToolTip(USER_MSGS.dialogAddFieldPhoneNumberTooltip());
        phoneNumber.setMaxLength(64);
        infoFieldSet.add(phoneNumber, subFieldsetFormData);

        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        infoFieldSet.add(optlock);

        userStatus = new SimpleComboBox<GwtUserStatus>();
        userStatus.setName("comboStatus");
        userStatus.setFieldLabel(USER_MSGS.dialogAddStatus());
        userStatus.setToolTip(USER_MSGS.dialogAddStatusTooltip());
        userStatus.setLabelSeparator(":");
        userStatus.setEditable(false);
        userStatus.setTypeAhead(true);
        userStatus.setTriggerAction(TriggerAction.ALL);
        // show account status combo box
        userStatus.add(GwtUserStatus.ENABLED);
        userStatus.add(GwtUserStatus.DISABLED);
        userStatus.setSimpleValue(GwtUserStatus.ENABLED);
        statusFieldSet.add(userStatus, subFieldsetFormData);

        expirationDate = new KapuaDateField(this);
        expirationDate.setName("expirationDate");
        expirationDate.setFormatValue(true);
        expirationDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDate.setFieldLabel(USER_MSGS.dialogAddExpirationDate());
        expirationDate.setToolTip(USER_MSGS.dialogAddExpirationDateTooltip());
        expirationDate.setLabelSeparator(":");
        expirationDate.setAllowBlank(true);
        expirationDate.setEmptyText(USER_MSGS.dialogAddNoExpiration());
        expirationDate.setValue(null);
        expirationDate.setMaxLength(10);
        expirationDate.getDatePicker().addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                formPanel.fireEvent(Events.OnClick);
            }
        });
        statusFieldSet.add(expirationDate, subFieldsetFormData);

        userFormPanel.add(infoFieldSet, subFormData);
        userFormPanel.add(statusFieldSet, subFormData);

        bodyPanel.add(userFormPanel);
    }

    public void validateUser() {
        if (username.getValue() == null || (passwordIsShown && password.getValue() == null)
                || (passwordIsShown && confirmPassword.getValue() == null)
                || (externalIdIsShown && externalId.getValue() == null)) {
            ConsoleInfo.display("Error", CMSGS.allFieldsRequired());
        } else if (passwordIsShown && !password.isValid()) {
            ConsoleInfo.display("Error", password.getErrorMessage());
        } else if (passwordIsShown && !password.getValue().equals(confirmPassword.getValue())) {
            ConsoleInfo.display("Error", confirmPassword.getErrorMessage());
        } else if (!email.isValid()) {
            ConsoleInfo.display("Error", email.getErrorMessage());
        } else if (!phoneNumber.isValid()) {
            ConsoleInfo.display("Error", phoneNumber.getErrorMessage());
        } else if (!expirationDate.isValid()) {
            ConsoleInfo.display("Error", KapuaSafeHtmlUtils.htmlUnescape(expirationDate.getErrorMessage()));
        }
    }

    @Override
    protected void preSubmit() {
        validateUser();
        super.preSubmit();
    }

    @Override
    public void submit() {
        GwtUserCreator gwtUserCreator = new GwtUserCreator();

        gwtUserCreator.setScopeId(specificAccountId != null ? specificAccountId : currentSession.getSelectedAccountId());

        gwtUserCreator.setUsername(username.getValue());

        if (userRadioGroup.getValue().getValueAttribute().equals(RadioGroupStatus.INTERNAL.name())) {
            gwtUserCreator.setGwtUserType(GwtUser.GwtUserType.INTERNAL);
            if (password != null) {
                gwtUserCreator.setPassword(password.getValue());
            }
        } else if (userRadioGroup.getValue().getValueAttribute().equals(RadioGroupStatus.EXTERNAL.name())) {
            gwtUserCreator.setGwtUserType(GwtUser.GwtUserType.EXTERNAL);
            if (externalId != null) {
                gwtUserCreator.setExternalId(externalId.getValue());
            }
        }
        // TODO: implement third case? Or throw exception

        gwtUserCreator.setDisplayName(displayName.getValue());
        gwtUserCreator.setEmail(email.getValue());
        gwtUserCreator.setPhoneNumber(phoneNumber.getValue());
        gwtUserCreator.setUserStatus(userStatus.getValue().getValue());
        gwtUserCreator.setExpirationDate(expirationDate.getValue());

        gwtUserService.create(xsrfToken, gwtUserCreator, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser gwtUser) {
                exitStatus = true;
                exitMessage = USER_MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (!isPermissionErrorMessage(cause)) {
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        switch (gwtCause.getCode()) {
                            case DUPLICATE_NAME:
                            case ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT:
                                username.markInvalid(gwtCause.getMessage());
                                break;
                            case DUPLICATE_EXTERNAL_ID:
                            case EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT:
                                externalId.markInvalid(gwtCause.getMessage());
                                break;
                            default:
                                break;
                        }
                    }
                    FailureHandler.handleFormException(formPanel, cause);
                }
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return USER_MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return USER_MSGS.dialogAddInfo();
    }

}
