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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client.dialog;

import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

public class UserEditDialog extends UserAddDialog {

    private GwtUser selectedUser;

    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    private final static ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

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
                exitMessage = MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
                unmaskDialog();
                hide();
            }
        });

    }

    @Override
    public void submit() {
        selectedUser.setUsername(username.getValue());
        selectedUser.setDisplayName(displayName.getValue());
        selectedUser.setEmail(email.getValue());
        selectedUser.setPhoneNumber(phoneNumber.getValue());
        selectedUser.setStatus(userStatus.getValue().getValue().toString());
        selectedUser.setExpirationDate(expirationDate.getValue());

        gwtUserService.update(xsrfToken, selectedUser, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditError(cause.getLocalizedMessage());
                hide();
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedUser.getUsername());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }


    private void populateEditDialog(GwtUser gwtUser) {
        username.setValue(gwtUser.getUsername());
        username.disable();
        password.setVisible(false);
        password.setAllowBlank(true);
        password.setValidator(null);
        confirmPassword.setVisible(false);
        confirmPassword.setAllowBlank(true);
        confirmPassword.setValidator(null);
        displayName.setValue(gwtUser.getDisplayName());
        email.setValue(gwtUser.getEmail());
        phoneNumber.setValue(gwtUser.getPhoneNumber());
        userStatus.setSimpleValue(gwtUser.getStatusEnum());
        expirationDate.setValue(gwtUser.getExpirationDate());
    }
}
