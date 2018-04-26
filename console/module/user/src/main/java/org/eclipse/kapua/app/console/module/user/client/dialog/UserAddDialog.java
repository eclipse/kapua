/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
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

public class UserAddDialog extends EntityAddEditDialog {

    protected static final ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);

    protected KapuaTextField<String> username;
    protected KapuaTextField<String> password;
    protected KapuaTextField<String> confirmPassword;
    protected LabelField passwordTooltip;
    protected TextField<String> displayName;
    protected TextField<String> email;
    protected TextField<String> phoneNumber;
    protected SimpleComboBox<GwtUser.GwtUserStatus> userStatus;
    protected KapuaDateField expirationDate;
    protected NumberField optlock;

    private String specificAccountId;

    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    public UserAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 440);
    }

    public UserAddDialog(GwtSession currentSession, String specificAccountId) {
        this(currentSession);
        this.specificAccountId = specificAccountId;
    }

    @Override
    public void createBody() {

        FormPanel userFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        userFormPanel.setFrame(false);
        userFormPanel.setBorders(false);
        userFormPanel.setBodyBorder(false);
        userFormPanel.setHeaderVisible(false);
        userFormPanel.setPadding(0);

        FormData subFieldsetFormData = new FormData();
        FormData subFormData = new FormData();

        //
        // User info tab
        //
        FieldSet infoFieldSet = new FieldSet();
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

        username = new KapuaTextField<String>();
        username.setAllowBlank(false);
        username.setMaxLength(255);
        username.setName("userName");
        username.setFieldLabel("* " + USER_MSGS.dialogAddFieldUsername());
        username.setValidator(new TextFieldValidator(username, FieldType.NAME));
        infoFieldSet.add(username, subFieldsetFormData);

        if (currentSession.hasPermission(CredentialSessionPermission.write())) {
            password = new KapuaTextField<String>();
            password.setAllowBlank(false);
            password.setName("password");
            password.setFieldLabel("* " + USER_MSGS.dialogAddFieldPassword());
            password.setValidator(new PasswordFieldValidator(password));
            password.setPassword(true);
            password.setMaxLength(255);
            infoFieldSet.add(password, subFieldsetFormData);

            confirmPassword = new KapuaTextField<String>();
            confirmPassword.setAllowBlank(false);
            confirmPassword.setName("confirmPassword");
            confirmPassword.setFieldLabel("* " + USER_MSGS.dialogAddFieldConfirmPassword());
            confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, password));
            confirmPassword.setPassword(true);
            confirmPassword.setMaxLength(255);
            infoFieldSet.add(confirmPassword, subFieldsetFormData);

            passwordTooltip = new LabelField();
            passwordTooltip.setValue(USER_MSGS.dialogAddTooltipPassword());
            passwordTooltip.setStyleAttribute("margin-top", "-5px");
            passwordTooltip.setStyleAttribute("color", "gray");
            passwordTooltip.setStyleAttribute("font-size", "10px");
            infoFieldSet.add(passwordTooltip);
        }
        displayName = new TextField<String>();
        displayName.setName("displayName");
        displayName.setFieldLabel(USER_MSGS.dialogAddFieldDisplayName());
        displayName.setMaxLength(255);
        infoFieldSet.add(displayName, subFieldsetFormData);

        email = new TextField<String>();
        email.setName("userEmail");
        email.setFieldLabel(USER_MSGS.dialogAddFieldEmail());
        email.setValidator(new TextFieldValidator(email, FieldType.EMAIL));
        email.setMaxLength(255);
        infoFieldSet.add(email, subFieldsetFormData);

        phoneNumber = new TextField<String>();
        phoneNumber.setName("phoneNumber");
        phoneNumber.setFieldLabel(USER_MSGS.dialogAddFieldPhoneNumber());
        phoneNumber.setValidator(new TextFieldValidator(phoneNumber, FieldType.PHONE));
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
        userStatus.setLabelSeparator(":");
        userStatus.setEditable(false);
        userStatus.setTypeAhead(true);
        userStatus.setTriggerAction(TriggerAction.ALL);
        // show account status combo box
        userStatus.add(GwtUserStatus.ENABLED);
        userStatus.add(GwtUserStatus.DISABLED);
        userStatus.setSimpleValue(GwtUserStatus.ENABLED);
        statusFieldSet.add(userStatus, subFieldsetFormData);

        expirationDate = new KapuaDateField();
        expirationDate.setName("expirationDate");
        expirationDate.setFormatValue(true);
        expirationDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDate.setFieldLabel(USER_MSGS.dialogAddExpirationDate());
        expirationDate.setLabelSeparator(":");
        expirationDate.setAllowBlank(true);
        expirationDate.setEmptyText(USER_MSGS.dialogAddNoExpiration());
        expirationDate.setValue(null);
        expirationDate.setMaxLength(10);
        statusFieldSet.add(expirationDate, subFieldsetFormData);

        userFormPanel.add(infoFieldSet, subFormData);
        userFormPanel.add(statusFieldSet, subFormData);

        bodyPanel.add(userFormPanel);
    }

    @Override
    public void submit() {
        GwtUserCreator gwtUserCreator = new GwtUserCreator();

        gwtUserCreator.setScopeId(specificAccountId != null ? specificAccountId : currentSession.getSelectedAccountId());

        gwtUserCreator.setUsername(username.getValue());
        if (password != null) {
            gwtUserCreator.setPassword(password.getValue());
        }
        gwtUserCreator.setDisplayName(displayName.getValue());
        gwtUserCreator.setEmail(email.getValue());
        gwtUserCreator.setPhoneNumber(phoneNumber.getValue());
        gwtUserCreator.setUserStatus(userStatus.getValue().getValue());
        gwtUserCreator.setExpirationDate(expirationDate.getValue());

        gwtUserService.create(xsrfToken, gwtUserCreator, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser arg0) {
                exitStatus = true;
                exitMessage = USER_MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (cause instanceof GwtKapuaException) {
                    GwtKapuaException gwtCause = (GwtKapuaException) cause;
                    if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                        username.markInvalid(gwtCause.getMessage());
                    }
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
