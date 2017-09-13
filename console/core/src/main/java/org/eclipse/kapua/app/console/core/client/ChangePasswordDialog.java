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
package org.eclipse.kapua.app.console.core.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.ActionDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;

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
        FormPanel credentialFormPanel = new FormPanel(ActionDialog.FORM_LABEL_WIDTH);
        DialogUtils.resizeDialog(this, 400, 200);

        oldPassword = new TextField<String>();
        oldPassword.setAllowBlank(false);
        oldPassword.setName("oldPassword");
        oldPassword.setFieldLabel("* " + ActionDialog.MSGS.oldPassword());
        oldPassword.setPassword(true);
        credentialFormPanel.add(oldPassword);

        newPassword = new TextField<String>();
        newPassword.setAllowBlank(false);
        newPassword.setName("newPassword");
        newPassword.setFieldLabel("* " + ActionDialog.MSGS.newPassword());
        newPassword.setValidator(new PasswordFieldValidator(newPassword));
        newPassword.setPassword(true);
        credentialFormPanel.add(newPassword);

        confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + ActionDialog.MSGS.confirmPassword());
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
        credentialService.changePassword(xsrfToken, oldPassword.getValue(), newPassword.getValue(), currentSession.getUserId(), currentSession.getSelectedAccountId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                ConsoleInfo.display("Error", ActionDialog.MSGS.changePasswordError(caught.getLocalizedMessage()));
            }

            @Override
            public void onSuccess(Void result) {
                hide();

                ConsoleInfo.display("Info", ActionDialog.MSGS.changePasswordConfirmation());
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return ActionDialog.MSGS.changePassword();
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
