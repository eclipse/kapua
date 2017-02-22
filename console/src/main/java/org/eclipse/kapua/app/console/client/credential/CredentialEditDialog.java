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
package org.eclipse.kapua.app.console.client.credential;

import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;

import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CredentialEditDialog extends CredentialAddDialog {

    private final GwtCredential selectedCredential;

    public CredentialEditDialog(GwtSession currentSession, GwtCredential selectedCredential) {
        super(currentSession);
        this.selectedCredential = selectedCredential;
    }

    @Override
    public void submit() {
        // TODO read enabled and expire date
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
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }
        });
    }

    @Override
    public void createBody() {
        super.createBody();
        subject.getStore().addStoreListener(new SubjectStoreListener());
        loadCredential();
    }

    private void loadCredential() {
        credentialType.setSimpleValue(selectedCredential.getCredentialTypeEnum());
        password.setValue(selectedCredential.getCredentialKey());
        confirmPassword.setValue(selectedCredential.getCredentialKey());
        subjectType.setSimpleValue(selectedCredential.getSubjectTypeEnum());
        subject.setValue(subject.getStore().findModel(selectedCredential.getUserId()));
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        credentialType.disable();
        password.disable();
        confirmPassword.disable();
        subjectType.disable();
        subject.disable();
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedCredential.getUsername());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    private class SubjectStoreListener extends StoreListener<GwtUser> {

        @Override
        public void storeAdd(StoreEvent<GwtUser> se) {
            subject.setValue(se.getStore().findModel("id", selectedCredential.getUserId()));
        }
    }
}
