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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.credential;

import org.eclipse.kapua.app.console.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.PasswordFieldValidator;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialCreator;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialType;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtSubjectType;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CredentialAddDialog extends EntityAddEditDialog {

    protected final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    protected SimpleComboBox<GwtCredentialType> credentialType;
    protected SimpleComboBox<GwtSubjectType> subjectType;
    protected ComboBox<GwtUser> subject;
    protected TextField<String> password;
    protected TextField<String> confirmPassword;
    protected DateField expirationDate;
    protected CheckBox enabled;
    protected NumberField optlock;

    protected static final GwtCredentialServiceAsync gwtCredentialService = GWT.create(GwtCredentialService.class);
    protected static final GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    public CredentialAddDialog(GwtSession currentSession) {
        super(currentSession);

        DialogUtils.resizeDialog(this, 400, 400);
    }

    @Override
    public void createBody() {

        FormPanel credentialFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        credentialType = new SimpleComboBox<GwtCredentialType>();
        credentialType.setEditable(false);
        credentialType.setTypeAhead(false);
        credentialType.setAllowBlank(false);
        credentialType.setFieldLabel(MSGS.dialogAddFieldCredentialType());
        credentialType.setTriggerAction(ComboBox.TriggerAction.ALL);
        credentialType.add(GwtCredentialType.PASSWORD);
        credentialType.add(GwtCredentialType.API_KEY);
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

        subjectType = new SimpleComboBox<GwtSubjectType>();
        subjectType.setEditable(false);
        subjectType.setTypeAhead(false);
        subjectType.setAllowBlank(false);
        subjectType.setFieldLabel(MSGS.dialogAddFieldSubjectType());
        subjectType.setTriggerAction(ComboBox.TriggerAction.ALL);
        subjectType.add(GwtSubjectType.USER);
        subjectType.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<GwtSubjectType>>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<GwtSubjectType>> selectionChangedEvent) {
                switch (selectionChangedEvent.getSelectedItem().getValue()) {
                case USER:
                    gwtUserService.findAll(currentSession.getSelectedAccount().getId(), new AsyncCallback<ListLoadResult<GwtUser>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            m_exitMessage = MSGS.dialogAddErrorSubjects(caught.getLocalizedMessage());
                            m_exitStatus = false;
                            hide();
                        }

                        @Override
                        public void onSuccess(ListLoadResult<GwtUser> result) {
                            subject.clear();
                            subject.getStore().add(result.getData());
                        }
                    });
                    break;
                }
            }
        });
        credentialFormPanel.add(subjectType);

        subject = new ComboBox<GwtUser>();
        subject.setEditable(false);
        subject.setTypeAhead(false);
        subject.setAllowBlank(false);
        subject.setFieldLabel(MSGS.dialogAddFieldSubject());
        subject.setTriggerAction(ComboBox.TriggerAction.ALL);
        subject.setStore(new ListStore<GwtUser>());
        subject.setDisplayField("username");
        subject.setValueField("id");
        credentialFormPanel.add(subject);

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

        expirationDate = new DateField();
        expirationDate.setFieldLabel(MSGS.dialogAddFieldExpirationDate());
        credentialFormPanel.add(expirationDate);

        enabled = new CheckBox();
        enabled.setFieldLabel(MSGS.dialogAddFieldEnabled());
        enabled.setBoxLabel("");    // Align to the left
        credentialFormPanel.add(enabled);

        optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        credentialFormPanel.add(optlock);

        m_bodyPanel.add(credentialFormPanel);
    }

    @Override
    public void submit() {
        final GwtCredentialCreator gwtCredentialCreator = new GwtCredentialCreator();

        gwtCredentialCreator.setScopeId(currentSession.getSelectedAccount().getId());

        gwtCredentialCreator.setCredentialType(credentialType.getValue().getValue());
        gwtCredentialCreator.setCredentialPlainKey(password.getValue());
        gwtCredentialCreator.setUserId(subject.getValue().getId());

        gwtCredentialService.create(xsrfToken, gwtCredentialCreator, new AsyncCallback<GwtCredential>() {

            @Override
            public void onSuccess(GwtCredential arg0) {
                if (gwtCredentialCreator.getCredentialType() == GwtCredentialType.API_KEY) {
                    Dialog apiKeyConfirmationDialog = new Dialog();
                    apiKeyConfirmationDialog.setButtons(Dialog.OK);
                    apiKeyConfirmationDialog.setHideOnButtonClick(true);
                    apiKeyConfirmationDialog.setLayout(new FormLayout());
                    Label valueMessage = new Label(new SafeHtmlBuilder().appendEscapedLines(MSGS.dialogAddConfirmationApiKey(arg0.getCredentialKey())).toSafeHtml().asString());
                    apiKeyConfirmationDialog.add(valueMessage);
                    apiKeyConfirmationDialog.show();
                }
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                m_submitButton.enable();
                m_cancelButton.enable();
                m_status.hide();

                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(cause.getLocalizedMessage());

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
