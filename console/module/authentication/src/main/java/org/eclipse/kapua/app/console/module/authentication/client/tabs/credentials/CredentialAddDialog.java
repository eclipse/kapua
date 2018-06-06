/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialStatus;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialType;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;

public class CredentialAddDialog extends EntityAddEditDialog {

    protected static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);
    private static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);

    protected FormPanel credentialFormPanel;
    private String selectedUserId;
    private String selectedUserName;
    SimpleComboBox<GwtCredentialType> credentialType;
    protected LabelField credentialTypeLabel;
    private LabelField subject;
    protected TextField<String> password;
    protected TextField<String> confirmPassword;
    protected KapuaDateField expirationDate;
    SimpleComboBox<GwtCredentialStatus> credentialStatus;
    NumberField optlock;

    static final GwtCredentialServiceAsync GWT_CREDENTIAL_SERVICE = GWT.create(GwtCredentialService.class);

    CredentialAddDialog(GwtSession currentSession, String selectedUserId, String selectedUserName) {
        super(currentSession);
        this.selectedUserId = selectedUserId;
        this.selectedUserName = selectedUserName;
        DialogUtils.resizeDialog(this, 400, 285);
    }

    @Override
    public void createBody() {

        credentialFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        subject = new LabelField();
        subject.setText(selectedUserName);
        subject.setFieldLabel(MSGS.dialogAddFieldSubject());
        subject.setLabelSeparator(":");
        credentialFormPanel.add(subject);

        credentialTypeLabel = new LabelField();
        credentialTypeLabel.setFieldLabel(MSGS.dialogAddFieldCredentialType());
        credentialTypeLabel.setLabelSeparator(":");
        credentialTypeLabel.setVisible(false);
        credentialFormPanel.add(credentialTypeLabel);

        credentialType = new SimpleComboBox<GwtCredentialType>();
        credentialType.setEditable(false);
        credentialType.setTypeAhead(false);
        credentialType.setAllowBlank(false);
        credentialType.setFieldLabel("* " + MSGS.dialogAddFieldCredentialType());
        credentialType.setTriggerAction(ComboBox.TriggerAction.ALL);
        credentialType.add(GwtCredentialType.PASSWORD);
        credentialType.add(GwtCredentialType.API_KEY);
        credentialType.setEmptyText(MSGS.credentialTypePlaceHolder());
        credentialType.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<GwtCredentialType>>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<GwtCredentialType>> selectionChangedEvent) {
                password.setVisible(selectionChangedEvent.getSelectedItem().getValue() == GwtCredentialType.PASSWORD);
                password.setAllowBlank(selectionChangedEvent.getSelectedItem().getValue() != GwtCredentialType.PASSWORD);
                confirmPassword.setVisible(selectionChangedEvent.getSelectedItem().getValue() == GwtCredentialType.PASSWORD);
                confirmPassword.setAllowBlank(selectionChangedEvent.getSelectedItem().getValue() != GwtCredentialType.PASSWORD);
            }
        });
        credentialFormPanel.add(credentialType);

        password = new TextField<String>();
        password.setAllowBlank(false);
        password.setName("password");
        password.setFieldLabel("* " + MSGS.dialogAddFieldPassword());
        password.setValidator(new PasswordFieldValidator(password));
        password.setPassword(true);
        password.setVisible(false);
        credentialFormPanel.add(password);

        confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + MSGS.dialogAddFieldConfirmPassword());
        confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, password));
        confirmPassword.setPassword(true);
        confirmPassword.setVisible(false);
        credentialFormPanel.add(confirmPassword);

        expirationDate = new KapuaDateField();
        expirationDate.setEmptyText(MSGS.dialogAddNoExpiration());
        expirationDate.setFieldLabel(MSGS.dialogAddFieldExpirationDate());
        expirationDate.setFormatValue(true);
        expirationDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDate.setMaxLength(10);
        credentialFormPanel.add(expirationDate);

        credentialStatus = new SimpleComboBox<GwtCredentialStatus>();
        credentialStatus.setName("comboStatus");
        credentialStatus.setFieldLabel(MSGS.dialogAddStatus());
        credentialStatus.setLabelSeparator(":");
        credentialStatus.setEditable(false);
        credentialStatus.setTypeAhead(true);
        credentialStatus.setTriggerAction(ComboBox.TriggerAction.ALL);
        credentialStatus.setEmptyText(MSGS.credentialStatusPlaceHolder());
        // show account status combo box
        for (GwtCredentialStatus credentialStatus : GwtCredentialStatus.values()) {
            this.credentialStatus.add(credentialStatus);
        }
        credentialStatus.setSimpleValue(GwtCredentialStatus.ENABLED);

        credentialFormPanel.add(credentialStatus);

        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        credentialFormPanel.add(optlock);

        bodyPanel.add(credentialFormPanel);
    }

    @Override
    public void submit() {
        final GwtCredentialCreator gwtCredentialCreator = new GwtCredentialCreator();

        gwtCredentialCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtCredentialCreator.setCredentialType(credentialType.getValue().getValue());
        gwtCredentialCreator.setCredentialPlainKey(password.getValue());
        gwtCredentialCreator.setUserId(selectedUserId);
        gwtCredentialCreator.setExpirationDate(expirationDate.getValue());
        gwtCredentialCreator.setCredentialStatus(credentialStatus.getValue().getValue());

        GWT_CREDENTIAL_SERVICE.create(xsrfToken, gwtCredentialCreator, new AsyncCallback<GwtCredential>() {

            @Override
            public void onSuccess(GwtCredential arg0) {
                if (gwtCredentialCreator.getCredentialType() == GwtCredentialType.API_KEY) {
                    ApiKeyConfirmationDialog apiKeyConfirmationDialog = new ApiKeyConfirmationDialog(arg0.getCredentialKey());
                    apiKeyConfirmationDialog.show();
                    exitMessage = MSGS.dialogAddConfirmationAPI();
                } else if (gwtCredentialCreator.getCredentialType() == GwtCredentialType.PASSWORD) {
                    exitMessage = MSGS.dialogAddConfirmationPassword();
                }
                exitStatus = true;
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                exitMessage = cause.getLocalizedMessage();
                credentialType.markInvalid(exitMessage);
                ConsoleInfo.display(CMSGS.error(), exitMessage);
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
