/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client.toolbar;

import com.extjs.gxt.ui.client.event.Events;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.permission.AccountSessionPermission;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountGridToolbar extends EntityCRUDToolbar<GwtAccount> {

    public AccountGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        super.getEditEntityButton().disable();
        super.getDeleteEntityButton().disable();
        getAddEntityButton().setEnabled(currentSession.hasPermission(AccountSessionPermission.write()));
    }

    @Override
    protected KapuaDialog getAddDialog() {
        AccountAddDialog dialog = new AccountAddDialog(currentSession);
        dialog.addListener(Events.Hide, getHideDialogListener());
        return dialog;
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtAccount selectedAccount = gridSelectionModel.getSelectedItem();
        AccountEditDialog dialog = null;
        if (selectedAccount != null) {
            dialog = new AccountEditDialog(currentSession, selectedAccount);
            dialog.addListener(Events.Hide, getHideDialogListener());
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtAccount selectedAccount = gridSelectionModel.getSelectedItem();
        AccountDeleteDialog dialog = null;
        if (selectedAccount != null) {
            dialog = new AccountDeleteDialog(selectedAccount);
        }
        return dialog;
    }

}
