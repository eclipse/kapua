/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

public class UserEditDialog extends UserAddDialog {

    private GwtUser selectedUser;

    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    public UserEditDialog(GwtSession currentSession, GwtUser selectedUser) {
        super(currentSession);
        this.selectedUser = selectedUser;
        DialogUtils.resizeDialog(this, 400, 390);
    }

    @Override
    public void createBody() {
        super.createBody();

        loadUser();
    }

    private void loadUser() {
        maskDialog();
        gwtUserService.find(selectedUser.getScopeId(), selectedUser.getId(), new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser gwtUser) {
                unmaskDialog();
                populateEditDialog(gwtUser);
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = USER_MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
                unmaskDialog();
                hide();
            }
        });

    }

    @Override
    public void submit() {
        selectedUser.setUsername(username.getValue());
        selectedUser.setDisplayName(KapuaSafeHtmlUtils.htmlUnescape(displayName.getValue()));
        selectedUser.setEmail(email.getValue());
        selectedUser.setPhoneNumber(phoneNumber.getValue());
        selectedUser.setStatus(userStatus.getValue().getValue().toString());
        selectedUser.setExpirationDate(expirationDate.getValue());

        gwtUserService.update(xsrfToken, selectedUser, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser arg0) {
                exitStatus = true;
                exitMessage = USER_MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = USER_MSGS.dialogEditError(cause.getLocalizedMessage());
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (cause instanceof GwtKapuaException) {
                    GwtKapuaException gwtCause = (GwtKapuaException) cause;
                    if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                        username.markInvalid(gwtCause.getMessage());
                    } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.ILLEGAL_ARGUMENT)) {
                        if (gwtCause.getArguments().length == 2 && gwtCause.getArguments()[0].equals("status") && gwtCause.getArguments()[1].equals("DISABLED")) {
                            userStatus.markInvalid(gwtCause.getMessage());
                        }
                    }
                }
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return USER_MSGS.dialogEditHeader(selectedUser.getUsername());
    }

    @Override
    public String getInfoMessage() {
        return USER_MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtUser gwtUser) {
        infoFieldSet.remove(username);
        usernameLabel.setVisible(true);
        usernameLabel.setValue(gwtUser.getUsername());
        if (password != null) {
            password.setVisible(false);
            password.setAllowBlank(true);
            password.setValidator(null);
        }
        if (confirmPassword != null) {
            confirmPassword.setVisible(false);
            confirmPassword.setAllowBlank(true);
            confirmPassword.setValidator(null);
        }
        if (passwordTooltip != null) {
            passwordTooltip.hide();
        }
        username.setValue(gwtUser.getUsername());
        displayName.setValue(gwtUser.getUnescapedDisplayName());
        email.setValue(gwtUser.getEmail());
        phoneNumber.setValue(gwtUser.getPhoneNumber());
        userStatus.setSimpleValue(gwtUser.getStatusEnum());
        expirationDate.setValue(gwtUser.getExpirationDate());
        expirationDate.setMaxLength(10);
    }
}
