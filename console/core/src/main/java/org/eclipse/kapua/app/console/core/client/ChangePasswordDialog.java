/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.client;

import org.eclipse.kapua.app.console.core.client.util.TokenCleaner;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.ActionDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordFieldValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptions;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtMfaCredentialOptionsService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtMfaCredentialOptionsServiceAsync;

import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangePasswordDialog extends SimpleDialog {

    GwtCredentialServiceAsync credentialService = GWT.create(GwtCredentialService.class);
    GwtMfaCredentialOptionsServiceAsync mfaCredentialOptionsService = GWT.create(GwtMfaCredentialOptionsService.class);
    private static final ConsoleMessages CONSOLE_MSGS = GWT.create(ConsoleMessages.class);

    private TextField<String> oldPassword;
    private TextField<String> newPassword;
    protected LabelField passwordTooltip;
    protected TextField<String> code;

    private GwtSession currentSession;
    private GwtMfaCredentialOptions gwtMfaCredentialOptions;
    private FormPanel credentialFormPanel;

    public ChangePasswordDialog(GwtSession currentSession, GwtMfaCredentialOptions gwtMfaCredentialOptions) {
        this.currentSession = currentSession;
        this.gwtMfaCredentialOptions = gwtMfaCredentialOptions;
    }

    @Override
    public void createBody() {

        submitButton.disable();
        credentialFormPanel = new FormPanel(ActionDialog.FORM_LABEL_WIDTH);
        DialogUtils.resizeDialog(ChangePasswordDialog.this, 400, 230);

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
        newPassword.setPassword(true);
        credentialFormPanel.add(newPassword);

        final TextField<String> confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + ActionDialog.MSGS.confirmPassword());
        confirmPassword.setPassword(true);
        credentialFormPanel.add(confirmPassword);

        passwordTooltip = new LabelField();
        credentialService.getMinPasswordLength(currentSession.getAccountId(), new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(Integer result) {
                newPassword.setValidator(new PasswordFieldValidator(newPassword, result));
                confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, newPassword, result));
                passwordTooltip.setValue(MSGS.dialogAddTooltipCredentialPassword(result.toString()));
            }
        });
        passwordTooltip.setStyleAttribute("margin-top", "-5px");
        passwordTooltip.setStyleAttribute("color", "gray");
        passwordTooltip.setStyleAttribute("font-size", "10px");
        credentialFormPanel.add(passwordTooltip);

        // MFA code
        code = new TextField<String>();
        code.setFieldLabel(MSGS.loginCode());
        if (gwtMfaCredentialOptions != null) {
            credentialFormPanel.add(code);
            DialogUtils.resizeDialog(ChangePasswordDialog.this, 400, 250);
        }
        bodyPanel.add(credentialFormPanel);
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void submit() {
        credentialService.changePassword(xsrfToken, oldPassword.getValue(), newPassword.getValue(), code.getValue(), currentSession.getUserId(), currentSession.getAccountId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();
                if (caught instanceof GwtKapuaException) {
                    GwtKapuaException gwtCaught = (GwtKapuaException) caught;
                    if (gwtCaught.getCode().equals(GwtKapuaErrorCode.INVALID_USERNAME_PASSWORD)) {
                        if (credentialFormPanel.findComponent(code) != null) {
                            ConsoleInfo.display(CONSOLE_MSGS.error(), ActionDialog.MSGS.changePasswordError(MSGS.changePasswordErrorWrongOldPasswordOrMfaCode()));
                        } else {
                            ConsoleInfo.display(CONSOLE_MSGS.error(), ActionDialog.MSGS.changePasswordError(MSGS.changePasswordErrorWrongOldPassword()));
                            oldPassword.markInvalid(MSGS.changePasswordErrorWrongOldPassword());
                        }
                    } else if (gwtCaught.getCode().equals(GwtKapuaErrorCode.UNAUTHENTICATED)) {
                        ConsoleInfo.display(CONSOLE_MSGS.error(), ActionDialog.MSGS.changePasswordError(caught.getLocalizedMessage()));
                        hide();
                        TokenCleaner.cleanToken();
                    } else {
                        ConsoleInfo.display(CONSOLE_MSGS.error(), ActionDialog.MSGS.changePasswordError(caught.getLocalizedMessage()));
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
