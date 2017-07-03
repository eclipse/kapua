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
package org.eclipse.kapua.app.console.client;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.PasswordFieldValidator;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialServiceAsync;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangePasswordDialog extends SimpleDialog {

    GwtCredentialServiceAsync credentialService = GWT.create(GwtCredentialService.class);

    private TextField<String> oldPassword;
    private TextField<String> newPassword;
    private TextField<String> confirmPassword;

    private GwtSession currentSession;

    public ChangePasswordDialog(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public void createBody() {
        FormPanel credentialFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        DialogUtils.resizeDialog(this, 400, 200);

        oldPassword = new TextField<String>();
        oldPassword.setAllowBlank(false);
        oldPassword.setName("oldPassword");
        oldPassword.setFieldLabel("* " + MSGS.oldPassword());
        oldPassword.setPassword(true);
        credentialFormPanel.add(oldPassword);

        newPassword = new TextField<String>();
        newPassword.setAllowBlank(false);
        newPassword.setName("newPassword");
        newPassword.setFieldLabel("* " + MSGS.newPassword());
        newPassword.setValidator(new PasswordFieldValidator(newPassword));
        newPassword.setPassword(true);
        credentialFormPanel.add(newPassword);

        confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + MSGS.confirmPassword());
        confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, newPassword));
        confirmPassword.setPassword(true);
        credentialFormPanel.add(confirmPassword);

        bodyPanel.add(credentialFormPanel);
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void submit() {
        credentialService.changePassword(xsrfToken, oldPassword.getValue(), newPassword.getValue(), currentSession.getUser().getId(), currentSession.getSelectedAccount().getId(),
                new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        unmask();

                        submitButton.enable();
                        cancelButton.enable();
                        status.hide();

                        ConsoleInfo.display("Error", MSGS.changePasswordError(caught.getLocalizedMessage()));
                    }

                    @Override
                    public void onSuccess(Void result) {
                        hide();

                        ConsoleInfo.display("Info", MSGS.changePasswordConfirmation());
                    }
                });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.changePassword();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInfoMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
