/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.app.console.core.client.util.Logout;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.ActionDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;

import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangePasswordDialog extends SimpleDialog {

    GwtCredentialServiceAsync credentialService = GWT.create(GwtCredentialService.class);

    private TextField<String> oldPassword;
    private TextField<String> newPassword;
    private TextField<String> confirmPassword;
    protected LabelField passwordTooltip;

    private GwtSession currentSession;

    public ChangePasswordDialog(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel credentialFormPanel = new FormPanel(ActionDialog.FORM_LABEL_WIDTH);
        DialogUtils.resizeDialog(this, 400, 230);

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

        passwordTooltip = new LabelField();
        passwordTooltip.setValue(ActionDialog.MSGS.dialogAddTooltipCredentialPassword());
        passwordTooltip.setStyleAttribute("margin-top", "-5px");
        passwordTooltip.setStyleAttribute("color", "gray");
        passwordTooltip.setStyleAttribute("font-size", "10px");
        credentialFormPanel.add(passwordTooltip);

        bodyPanel.add(credentialFormPanel);
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void submit() {
        credentialService.changePassword(xsrfToken, oldPassword.getValue(), newPassword.getValue(), currentSession.getUserId(), currentSession.getAccountId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();
                if (caught instanceof GwtKapuaException) {
                    GwtKapuaException gwtCaught = (GwtKapuaException) caught;
                    if (gwtCaught.getCode().equals(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD)) {
                        ConsoleInfo.display("Error", ActionDialog.MSGS.changePasswordError(MSGS.changePasswordErrorWrongOldPassword()));
                        oldPassword.markInvalid(MSGS.changePasswordErrorWrongOldPassword());
                    } else if (gwtCaught.getCode().equals(GwtKapuaErrorCode.UNAUTHENTICATED)) {
                        ConsoleInfo.display("Error", ActionDialog.MSGS.changePasswordError(caught.getLocalizedMessage()));
                        hide();
                        Logout.logout();
                    } else {
                        ConsoleInfo.display("Error", ActionDialog.MSGS.changePasswordError(caught.getLocalizedMessage()));
                    }
                }
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
