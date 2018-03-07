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
package org.eclipse.kapua.app.console.module.account.client.toolbar;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;

public class AccountDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleAccountMessages MSGS = GWT.create(ConsoleAccountMessages.class);
    private static final GwtAccountServiceAsync GWT_ACCOUNT_SERVICE = GWT.create(GwtAccountService.class);

    GwtAccount selectedAccount;

    public AccountDeleteDialog(GwtAccount selectedAccount) {
        super();
        this.selectedAccount = selectedAccount;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        GWT_ACCOUNT_SERVICE.delete(xsrfToken, selectedAccount, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void v) {
                exitStatus = true;
                exitMessage = MSGS.accountDeletedConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable t) {
                exitStatus = false;
                exitMessage = MSGS.accountDeleteErrorMessage();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.accountDeleteDialogHeader(selectedAccount.getUnescapedName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.accountDeleteInfoMessage();
    }

}
