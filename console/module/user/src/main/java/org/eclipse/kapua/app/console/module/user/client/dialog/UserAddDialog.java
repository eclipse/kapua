/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser.GwtUserStatus;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUserCreator;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

public class UserAddDialog extends EntityAddEditDialog {

    protected static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    protected TextField<String> username;
    protected TextField<String> password;
    protected TextField<String> confirmPassword;
    protected TextField<String> displayName;
    protected TextField<String> email;
    protected TextField<String> phoneNumber;
    protected SimpleComboBox<GwtUser.GwtUserStatus> userStatus;
    protected DateField expirationDate;
    protected NumberField optlock;

    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    public UserAddDialog(GwtSession currentSession) {
        super(currentSession);

        DialogUtils.resizeDialog(this, 400, 500);
    }

    @Override
    public void createBody() {

        FormPanel userFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        userFormPanel.setFrame(false);
        userFormPanel.setBorders(false);
        userFormPanel.setBodyBorder(false);
        userFormPanel.setHeaderVisible(false);
        userFormPanel.setPadding(0);

        //
        // User info tab
        //
        FieldSet infoFieldSet = new FieldSet();
        infoFieldSet.setHeading(MSGS.dialogAddFieldSet());
        infoFieldSet.setBorders(true);
        infoFieldSet.setStyleAttribute("margin", "0px 10px 0px 10px");

        FieldSet statusFieldSet = new FieldSet();
        statusFieldSet.setBorders(true);
        statusFieldSet.setStyleAttribute("margin", "5px 10px 0px 10px");
        statusFieldSet.setHeading(MSGS.dialogAddStatus());

        FormLayout userLayout = new FormLayout();
        FormLayout statusLayout = new FormLayout();
        userLayout.setLabelWidth(Constants.LABEL_WIDTH_FORM);
        statusLayout.setLabelWidth(Constants.LABEL_WIDTH_FORM);
        infoFieldSet.setLayout(userLayout);
        infoFieldSet.setStyleAttribute("background-color", "E8E8E8");
        statusFieldSet.setLayout(statusLayout);
        statusFieldSet.setStyleAttribute("background-color", "E8E8E8");

        username = new TextField<String>();
        username.setAllowBlank(false);
        username.setName("userName");
        username.setFieldLabel("* " + MSGS.dialogAddFieldUsername());
        username.setValidator(new TextFieldValidator(username, FieldType.NAME));
        infoFieldSet.add(username);

        password = new TextField<String>();
        password.setAllowBlank(false);
        password.setName("password");
        password.setFieldLabel("* " + MSGS.dialogAddFieldPassword());
        password.setValidator(new PasswordFieldValidator(password));
        password.setPassword(true);
        infoFieldSet.add(password);

        confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + MSGS.dialogAddFieldConfirmPassword());
        confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, password));
        confirmPassword.setPassword(true);
        infoFieldSet.add(confirmPassword);

        LabelField tooltip = new LabelField();
        tooltip.setValue(MSGS.dialogAddTooltipPassword());
        tooltip.setStyleAttribute("margin-top", "-5px");
        tooltip.setStyleAttribute("color", "gray");
        tooltip.setStyleAttribute("font-size", "10px");
        infoFieldSet.add(tooltip);

        displayName = new TextField<String>();
        displayName.setName("displayName");
        displayName.setFieldLabel(MSGS.dialogAddFieldDisplayName());
        infoFieldSet.add(displayName);

        email = new TextField<String>();
        email.setName("userEmail");
        email.setFieldLabel(MSGS.dialogAddFieldEmail());
        email.setValidator(new TextFieldValidator(email, FieldType.EMAIL));
        infoFieldSet.add(email);

        phoneNumber = new TextField<String>();
        phoneNumber.setName("phoneNumber");
        phoneNumber.setFieldLabel(MSGS.dialogAddFieldPhoneNumber());
        infoFieldSet.add(phoneNumber);

        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        infoFieldSet.add(optlock);

        userStatus = new SimpleComboBox<GwtUser.GwtUserStatus>();
        userStatus.setName("comboStatus");
        userStatus.setFieldLabel(MSGS.dialogAddStatus());
        userStatus.setLabelSeparator(":");
        userStatus.setEditable(false);
        userStatus.setTypeAhead(true);
        userStatus.setTriggerAction(ComboBox.TriggerAction.ALL);
        // show account status combo box
        userStatus.add(GwtUserStatus.ENABLED);
        userStatus.add(GwtUserStatus.DISABLED);
        userStatus.setSimpleValue(GwtUser.GwtUserStatus.ENABLED);
        statusFieldSet.add(userStatus);

        expirationDate = new DateField();
        expirationDate.setName("expirationDate");
        expirationDate.setFormatValue(true);
        expirationDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDate.setFieldLabel(MSGS.dialogAddExpirationDate());
        expirationDate.setLabelSeparator(":");
        expirationDate.setAllowBlank(true);
        expirationDate.setEmptyText(MSGS.dialogAddNoExpiration());
        expirationDate.setValue(null);
        statusFieldSet.add(expirationDate);

        userFormPanel.add(infoFieldSet);
        userFormPanel.add(statusFieldSet);

        bodyPanel.add(userFormPanel);
    }

    @Override
    public void submit() {
        GwtUserCreator gwtUserCreator = new GwtUserCreator();

        gwtUserCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtUserCreator.setUsername(username.getValue());
        gwtUserCreator.setPassword(password.getValue());
        gwtUserCreator.setDisplayName(displayName.getValue());
        gwtUserCreator.setEmail(email.getValue());
        gwtUserCreator.setPhoneNumber(phoneNumber.getValue());
        gwtUserCreator.setUserStatus(userStatus.getValue().getValue());
        gwtUserCreator.setExpirationDate(expirationDate.getValue());

        gwtUserService.create(xsrfToken, gwtUserCreator, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                exitMessage = MSGS.dialogAddError(cause.getLocalizedMessage());

                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }

}
