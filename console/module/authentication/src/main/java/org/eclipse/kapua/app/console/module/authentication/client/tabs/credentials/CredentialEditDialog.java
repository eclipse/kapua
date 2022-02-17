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
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.client.util.validator.ConfirmPasswordUpdateFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.PasswordUpdateFieldValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialType;

import java.util.Date;

public class CredentialEditDialog extends CredentialAddDialog {

    private final GwtCredential selectedCredential;

    private final Label lockedUntil = new Label();

    public CredentialEditDialog(GwtSession currentSession, GwtCredential selectedCredential, String selectedUserId, String selectedUserName) {
        super(currentSession, selectedUserId, selectedUserName);
        this.selectedCredential = selectedCredential;
    }

    @Override
    public void submit() {
        // TODO read enabled and expire date
        selectedCredential.setCredentialKey(password.getValue());
        selectedCredential.setExpirationDate(expirationDate.getValue());
        selectedCredential.setCredentialStatus(credentialStatus.getValue().getValue().toString());
        selectedCredential.setOptlock(optlock.getValue().intValue());
        GWT_CREDENTIAL_SERVICE.update(xsrfToken, selectedCredential, new AsyncCallback<GwtCredential>() {

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
            public void onSuccess(GwtCredential result) {
                exitStatus = true;

                if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
                    exitMessage = MSGS.dialogEditConfirmationAPI();
                } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
                    exitMessage = MSGS.dialogEditConfirmationPassword();
                }

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
            DialogUtils.resizeDialog(CredentialEditDialog.this, 400, 355);
            expirationDate.setToolTip(MSGS.dialogAddFieldExpirationDatePasswordTooltip());
            credentialStatus.setToolTip(MSGS.dialogAddStatusPasswordTooltip());
        }
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        credentialFormPanel.remove(credentialType);
        credentialTypeLabel.setVisible(true);
        credentialTypeLabel.setValue(selectedCredential.getCredentialType());
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
        } else if (!expirationDate.isValid()) {
            ConsoleInfo.display(CMSGS.popupError(), KapuaSafeHtmlUtils.htmlUnescape(expirationDate.getErrorMessage()));
        }
    }

    @Override
    protected void preSubmit() {
        super.preSubmit();
    }

    @Override
    public String getHeaderMessage() {
        if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
            return MSGS.dialogEditApiKeyHeader(selectedCredential.getUsername());
        } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
            return MSGS.dialogEditPasswordHeader(selectedCredential.getUsername());
        }
        return MSGS.dialogEditHeader(selectedCredential.getUsername());
    }

    @Override
    public String getInfoMessage() {
        if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
            return MSGS.dialogEditApiKeyInfo();
        } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
            return MSGS.dialogEditPasswordInfo();
        }
        return MSGS.dialogEditInfo();
    }
}
