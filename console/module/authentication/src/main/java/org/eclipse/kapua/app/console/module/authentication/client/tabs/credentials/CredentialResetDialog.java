/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordUpdateFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordUpdateFieldValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialType;

import java.util.Date;

public class CredentialResetDialog extends CredentialAddDialog {
    private final GwtCredential selectedCredential;

    private final Label lockedUntil = new Label();


    public CredentialResetDialog(GwtSession currentSession, String selectedUserId, String selectedUserName, GwtCredential selectedCredential) {
        super(currentSession, selectedUserId, selectedUserName);
        this.selectedCredential = selectedCredential;
    }


    @Override
    public void submit() {
        selectedCredential.setCredentialKey(password.getValue());
        selectedCredential.setExpirationDate(expirationDate.getValue());
        selectedCredential.setCredentialStatus(credentialStatus.getValue().getValue().toString());
        selectedCredential.setOptlock(optlock.getValue().intValue());
        GWT_CREDENTIAL_SERVICE.resetPassword(xsrfToken, selectedCredential.getScopeId(), selectedCredential.getId(), password.getValue(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                if (!isPermissionErrorMessage(caught)) {
                    exitMessage = MSGS.dialogEditError(caught.getLocalizedMessage());
                }
            }

            @Override
            public void onSuccess(Void result) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmationPassword();

                hide();
            }
        });
    }


    @Override
    public void createBody() {
        super.createBody();
        loadCredential();
    }


    private void loadCredential() {
        credentialType.setSimpleValue(selectedCredential.getCredentialTypeEnum());
        expirationDate.setValue(selectedCredential.getExpirationDate());
        credentialStatus.setSimpleValue(selectedCredential.getCredentialStatusEnum());
        optlock.setValue(selectedCredential.getOptlock());
        if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
            expirationDate.setToolTip(MSGS.dialogAddFieldExpirationDateApiKeyTooltip());
            credentialStatus.setToolTip(MSGS.dialogAddStatusApiKeyTooltip());
        } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
            passwordTooltip.show();
            DialogUtils.resizeDialog(CredentialResetDialog.this, 400, 355);
            expirationDate.setToolTip(MSGS.dialogAddFieldExpirationDatePasswordTooltip());
            credentialStatus.setToolTip(MSGS.dialogAddStatusPasswordTooltip());
        }
    }


    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        DialogUtils.resizeDialog(this, 400, 250);
        password.setVisible(true);
        confirmPassword.setVisible(true);
        passwordTooltip.setVisible(true);
        credentialFormPanel.remove(credentialType);
        credentialTypeLabel.setVisible(false);
        expirationDate.setVisible(false);
        credentialStatus.setVisible(false);
        password.setFieldLabel(MSGS.dialogEditFieldNewPassword());
        password.setAllowBlank(true);
        password.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                confirmPassword.setAllowBlank(password.getValue() == null || password.getValue().equals(""));
            }
        });
        confirmPassword.setFieldLabel(MSGS.dialogEditFieldConfirmNewPassword());
        confirmPassword.setAllowBlank(true);
        if (selectedCredential.getLockoutReset() != null && selectedCredential.getLockoutReset().after(new Date())) {
            lockedUntil.setText(MSGS.dialogEditLockedUntil(DateUtils.formatDateTime(selectedCredential.getLockoutReset())));
            credentialFormPanel.add(lockedUntil);
        }
        GWT_CREDENTIAL_SERVICE.getMinPasswordLength(selectedCredential.getScopeId(), new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(Integer result) {
                confirmPassword.setValidator(new ConfirmPasswordUpdateFieldValidator(confirmPassword, password, result));
                password.setValidator(new PasswordUpdateFieldValidator(password, result));
            }
        });
    }


    @Override
    public void validateUserCredential() {
        if (password.getValue() != null && confirmPassword.getValue() == null) {
            ConsoleInfo.display(CMSGS.popupError(), MSGS.credentialConfirmPasswordRequired());
        } else if (!password.isValid()) {
            ConsoleInfo.display(CMSGS.popupError(), password.getErrorMessage());
        } else if (password.getValue() != null && !password.getValue().equals(confirmPassword.getValue())) {
            ConsoleInfo.display(CMSGS.popupError(), confirmPassword.getErrorMessage());
        }
    }


    @Override
    protected void preSubmit() {
        super.preSubmit();
    }


    @Override
    public String getHeaderMessage() {
        return MSGS.dialogResetPasswordHeader(selectedCredential.getUsername());
    }


    @Override
    public String getInfoMessage() {
        return MSGS.dialogResetPassword();
    }
}
