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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;

public class CredentialUnlockDialog extends EntityDeleteDialog {

    private final GwtCredential selectedCredential;
    private static final GwtCredentialServiceAsync GWT_CREDENTIAL_SERVICE = GWT.create(GwtCredentialService.class);
    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    public CredentialUnlockDialog(GwtCredential selectedCredential) {
        this.selectedCredential = selectedCredential;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogUnlockHeader(selectedCredential.getCredentialType());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogUnlockInfo();
    }

    @Override
    public void submit() {
        GWT_CREDENTIAL_SERVICE.unlock(xsrfToken, selectedCredential.getScopeId(), selectedCredential.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogUnlockConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                if (!isPermissionErrorMessage(cause)) {
                    exitMessage = MSGS.dialogUnlockError(cause.getLocalizedMessage());
                }
                hide();
            }
        });

    }
}
