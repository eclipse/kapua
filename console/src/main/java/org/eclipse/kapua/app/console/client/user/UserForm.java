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
package org.eclipse.kapua.app.console.client.user;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.Constants;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.MessageUtils;
import org.eclipse.kapua.app.console.client.util.PasswordFieldValidator;
import org.eclipse.kapua.app.console.client.util.TextFieldValidator;
import org.eclipse.kapua.app.console.client.util.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtUser;
import org.eclipse.kapua.app.console.shared.model.GwtUser.GwtUserStatus;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserForm extends Window {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    protected final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);
    protected final GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    protected String m_accountId;
    protected GwtSession m_currentSession;
    protected GwtUser m_existingUser;
    protected FormData formData;
    protected UserForm m_userForm;
    protected FormPanel m_formPanel;
    protected Status m_status;

    protected TabPanel m_tabsPanel;
    protected int preselectedTabIndex = 0;
    private TabItem m_tabUserInfo;
    protected TabItem m_tab2FA;

    protected FieldSet statusFieldSet;

    protected FormPanel internalPanel;

    protected Status internalStatus;

    protected TextField<String> username;
    protected TextField<String> password;
    protected TextField<String> confirmPassword;
    protected TextField<String> displayName;
    protected TextField<String> email;
    protected TextField<String> phoneNumber;
    protected NumberField optlock;
    protected SimpleComboBox<String> statusCombo;
    protected CheckBox lockedCheckBox;
    protected CheckBoxGroup lockedCheckGroup;
    protected LabelField loginOnLabel;
    protected LabelField loginAttemptsLabel;
    protected LabelField loginAttemptsResetOnLabel;
    protected LabelField lockedOnLabel;
    protected LabelField unlockOnLabel;

    protected Button submitButton;

    /**
     * No new user is allowed for this form. Only the UserManagerForm can create a new user form using this as super constructor
     * 
     * @param accountId
     */
    protected UserForm(String accountId) {
        m_accountId = accountId;
        m_userForm = this;

        setModal(true);
        setLayout(new FitLayout());
        setResizable(false);
        setHeading(MSGS.userFormNew());

        // Check the client browser height to manage the height of the UserForm to avoid it to bleed out the screen
        DialogUtils.resizeDialog(this, 500, 530);
    }

    public UserForm(String accountId, GwtUser existingUser, GwtSession session) {
        this(accountId);

        m_existingUser = existingUser;
        if (m_existingUser != null) {
            setHeading(MSGS.userFormUpdate(m_existingUser.getUsername()));
        }
        m_currentSession = session;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        FormData formData = new FormData("0");

        // Create separate tabs
        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(true);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setBodyBorder(false);

        m_formPanel = new FormPanel();
        m_formPanel.setFrame(false);
        m_formPanel.setBorders(false);
        m_formPanel.setBodyBorder(false);
        m_formPanel.setHeaderVisible(false);
        m_formPanel.setLayout(new FitLayout());
        m_formPanel.setPadding(0);
        m_formPanel.add(m_tabsPanel);

        //
        // User info tab
        //
        FieldSet infoFieldSet = new FieldSet();
        infoFieldSet.setHeading(MSGS.userFormInfo());
        infoFieldSet.setBorders(true);
        infoFieldSet.setStyleAttribute("margin", "0px 10px 0px 10px");

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_FORM);
        infoFieldSet.setLayout(layout);

        username = new TextField<String>();
        username.setAllowBlank(false);
        username.setName("userName");
        username.setFieldLabel("* " + MSGS.userFormName());
        username.setValidator(new TextFieldValidator(username, FieldType.NAME));
        infoFieldSet.add(username, formData);

        password = new TextField<String>();
        password.setAllowBlank(false);
        password.setName("password");
        password.setFieldLabel("* " + MSGS.userFormPassword());
        password.setValidator(new PasswordFieldValidator(password));
        password.setPassword(true);
        infoFieldSet.add(password, formData);

        confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + MSGS.userFormConfirmPassword());
        confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, password));
        confirmPassword.setPassword(true);
        infoFieldSet.add(confirmPassword, formData);

        LabelField tooltip = new LabelField();
        tooltip.setValue(MSGS.userFormPasswordTooltip());
        tooltip.setStyleAttribute("margin-top", "-5px");
        tooltip.setStyleAttribute("color", "gray");
        tooltip.setStyleAttribute("font-size", "10px");
        infoFieldSet.add(tooltip, formData);

        displayName = new TextField<String>();
        displayName.setName("displayName");
        displayName.setFieldLabel(MSGS.userFormDisplayName());
        infoFieldSet.add(displayName, formData);

        email = new TextField<String>();
        email.setName("userEmail");
        email.setFieldLabel(MSGS.userFormEmail());
        email.setValidator(new TextFieldValidator(email, FieldType.EMAIL));
        infoFieldSet.add(email, formData);

        phoneNumber = new TextField<String>();
        phoneNumber.setName("phoneNumber");
        phoneNumber.setFieldLabel(MSGS.userFormPhoneNumber());
        infoFieldSet.add(phoneNumber, formData);

        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        infoFieldSet.add(optlock, formData);

        // status field set
        FormLayout userLayout = new FormLayout();
        userLayout.setLabelWidth(Constants.LABEL_WIDTH_FORM);

        statusFieldSet = new FieldSet();
        statusFieldSet.setBorders(true);
        statusFieldSet.setStyleAttribute("margin", "5px 10px 0px 10px");
        statusFieldSet.setHeading(MSGS.userFormStatus());
        statusFieldSet.setLayout(userLayout);

        statusCombo = new SimpleComboBox<String>();
        statusCombo.setName("comboStatus");
        statusCombo.setFieldLabel(MSGS.userFormStatus());
        statusCombo.setLabelSeparator(":");
        statusCombo.setEditable(false);
        statusCombo.setTypeAhead(true);
        statusCombo.setTriggerAction(TriggerAction.ALL);
        // show account status combo box
        for (GwtUserStatus status : GwtUserStatus.values()) {
            statusCombo.add(MessageUtils.get(status.name()));
        }
        statusCombo.setSimpleValue(MessageUtils.get(GwtUserStatus.ENABLED.name()));
        statusFieldSet.add(statusCombo, formData);

        m_tabUserInfo = new TabItem(MSGS.userFormInformation());
        m_tabUserInfo.setBorders(false);
        m_tabUserInfo.setStyleAttribute("background-color", "#E8E8E8");
        m_tabUserInfo.setScrollMode(Scroll.AUTOY);
        m_tabUserInfo.add(infoFieldSet);
        m_tabUserInfo.add(statusFieldSet);

        m_tabsPanel.add(m_tabUserInfo);

        // button bar
        m_status = new Status();
        m_status.setBusy(MSGS.waitMsg());
        m_status.hide();
        m_status.setAutoWidth(true);

        submitButton = new Button(MSGS.submitButton(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!m_formPanel.isValid()) {
                    return;
                }

                // Hold the dialog until the action comes back
                m_status.show();
                m_formPanel.getButtonBar().disable();

                submitAccount();
            }
        });

        Button cancelButton = new Button(MSGS.cancelButton(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });

        m_formPanel.getButtonBar().add(m_status);
        m_formPanel.getButtonBar().add(new FillToolItem());
        m_formPanel.setButtonAlign(HorizontalAlignment.CENTER);
        m_formPanel.addButton(submitButton);
        m_formPanel.addButton(cancelButton);

        loadUser();

        add(m_formPanel);

        setEditability();
    }

    protected void setEditability() {
        statusCombo.setEnabled(false);
        if (!hasSelfManage()) {
            username.setEnabled(false);
            password.setEnabled(false);
            confirmPassword.setEnabled(false);
            email.setEnabled(false);
            displayName.setEnabled(false);
            phoneNumber.setEnabled(false);
            submitButton.setVisible(false);
        }
    }

    protected void loadUser() {
        // populate if necessary
        if (m_existingUser != null) {
            gwtUserService.find(m_accountId, m_existingUser.getId(), new AsyncCallback<GwtUser>() {

                public void onFailure(Throwable caught) {
                    FailureHandler.handle(caught);
                }

                public void onSuccess(GwtUser gwtUser) {
                    populateTabUserInfo(gwtUser);
                }
            });
        } else {
            // New user. No need to show the status field set
            statusFieldSet.setVisible(false);
        }
    }

    protected void populateTabUserInfo(GwtUser gwtUser) {
        // set value and original value as we want to track the Dirty state
        username.setValue(gwtUser.getUnescapedUsername());
        username.setOriginalValue(gwtUser.getUnescapedUsername());

        // password.setValue(gwtUser.getPassword().substring(0, Math.min(10, gwtUser.getPassword().length() - 1)));
        // password.setOriginalValue(gwtUser.getPassword().substring(0, Math.min(10, gwtUser.getPassword().length() - 1)));
        // password.clearInvalid();
        password.setValidator(null);

        // confirmPassword.setValue(gwtUser.getPassword().substring(0, Math.min(10, gwtUser.getPassword().length() - 1)));
        // confirmPassword.setOriginalValue(gwtUser.getPassword().substring(0, Math.min(10, gwtUser.getPassword().length() - 1)));
        // confirmPassword.clearInvalid();
        confirmPassword.setValidator(null);

        displayName.setValue(gwtUser.getUnescapedDisplayName());
        displayName.setOriginalValue(gwtUser.getUnescapedDisplayName());

        email.setValue(gwtUser.getUnescapedEmail());
        email.setOriginalValue(gwtUser.getUnescapedEmail());

        phoneNumber.setValue(gwtUser.getUnescapedPhoneNumber());
        phoneNumber.setOriginalValue(gwtUser.getUnescapedPhoneNumber());

        statusCombo.setSimpleValue(MessageUtils.get(gwtUser.getStatus()));

        optlock.setValue(gwtUser.getOptlock());
    }

    protected void submitAccount() {
        // update
        m_existingUser.setUsername(username.getValue());
        m_existingUser.setDisplayName(displayName.getValue());
        m_existingUser.setEmail(email.getValue());
        m_existingUser.setPhoneNumber(phoneNumber.getValue());
        m_existingUser.setStatus((GwtUserStatus.values()[statusCombo.getSelectedIndex()]).name());

        m_existingUser.setOptlock(optlock.getValue().intValue());

        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

            @Override
            public void onFailure(Throwable ex) {
                FailureHandler.handle(ex);
            }

            @Override
            public void onSuccess(GwtXSRFToken token) {
                updateCall(token);
            }
        });
    }

    protected void updateCall(GwtXSRFToken token) {
        gwtUserService.update(token, m_existingUser, new AsyncCallback<GwtUser>() {

            public void onFailure(Throwable caught) {
                FailureHandler.handleFormException(m_formPanel, caught);
                m_status.hide();
                m_formPanel.getButtonBar().enable();
            }

            public void onSuccess(GwtUser user) {
                ConsoleInfo.display(MSGS.info(), MSGS.userUpdatedConfirmation(user.getUnescapedUsername()));
                hide();
            }
        });
    }

    private boolean hasSelfManage() {
        return m_currentSession != null && m_currentSession.hasUserUpdatePermission();
    }

    public void show(int tabIndex) {
        preselectedTabIndex = tabIndex;

        super.show();
    }

}
