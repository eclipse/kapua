/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialType;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;

public class CredentialDeleteDialog extends EntityDeleteDialog {

    private final GwtCredential selectedCredential;
    private static final GwtCredentialServiceAsync GWT_CREDENTIAL_SERVICE = GWT.create(GwtCredentialService.class);
    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    public CredentialDeleteDialog(GwtCredential selectedCredential) {
        this.selectedCredential = selectedCredential;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
            return MSGS.dialogDeleteHeaderAPI(selectedCredential.getId());
        } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
            return MSGS.dialogDeleteHeaderPassword(selectedCredential.getId());
        } else {
            return "";
        }
    }

    @Override
    public String getInfoMessage() {
        if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
            return MSGS.dialogDeleteInfoAPI();
        } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
            return MSGS.dialogDeleteInfoPassword();
        } else {
            return "";
        }
    }

    @Override
    public void submit() {
        GWT_CREDENTIAL_SERVICE.delete(xsrfToken, selectedCredential.getScopeId(), selectedCredential.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;

                if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
                    exitMessage = MSGS.dialogDeleteConfirmationAPI();
                } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
                    exitMessage = MSGS.dialogDeleteConfirmationPassword();
                }

                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;

                if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.API_KEY) {
                    exitMessage = MSGS.dialogDeleteErrorAPI(cause.getLocalizedMessage());
                } else if (selectedCredential.getCredentialTypeEnum() == GwtCredentialType.PASSWORD) {
                    exitMessage = MSGS.dialogDeleteErrorPassword(cause.getLocalizedMessage());
                }

                hide();
            }
        });

    }
}
