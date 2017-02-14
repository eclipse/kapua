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
package org.eclipse.kapua.app.console.client.user.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserEditDialog extends UserAddDialog {

    private GwtUser selectedUser;
    
    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);
    
    private final static ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);
    
    public UserEditDialog(GwtSession currentSession, GwtUser selectedUser) {
        super(currentSession);
        this.selectedUser = selectedUser;
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
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
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

        gwtUserService.update(xsrfToken, selectedUser, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogEditError(cause.getLocalizedMessage());
            }
        });

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
    }
}
