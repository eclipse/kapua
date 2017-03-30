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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user.tabs.credentials;

import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CredentialEditDialog extends CredentialAddDialog {

    private final GwtCredential selectedCredential;

    public CredentialEditDialog(GwtSession currentSession, GwtCredential selectedCredential, GwtUser selectedUser) {
        super(currentSession, selectedUser);
        this.selectedCredential = selectedCredential;
    }

    @Override
    public void submit() {
        // TODO read enabled and expire date
        selectedCredential.setCredentialKey(password.getValue());
        gwtCredentialService.update(xsrfToken, selectedCredential, new AsyncCallback<GwtCredential>() {

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                m_submitButton.enable();
                m_cancelButton.enable();
                m_status.hide();

                m_exitStatus = false;
                m_exitMessage = MSGS.dialogEditError(caught.getLocalizedMessage());

                hide();
            }

            @Override
            public void onSuccess(GwtCredential result) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogEditConfirmation();
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
        password.setValue(selectedCredential.getCredentialKey());
        confirmPassword.setValue(selectedCredential.getCredentialKey());
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        credentialType.disable();
        //password.disable();
        //confirmPassword.disable();
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedCredential.getUsername());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }
}
