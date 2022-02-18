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
package org.eclipse.kapua.app.console.module.account.client.childuser;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserAddDialog;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserDeleteDialog;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserEditDialog;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class AccountChildUserToolbar extends EntityCRUDToolbar<GwtUser> {

    private String accountId;

    public AccountChildUserToolbar(GwtSession currentSession) {
        super(currentSession, true);
    }

    public void setSelectedAccountId(String accountId) {
        this.accountId = accountId;
        updateToolBarButtons();
    }

    public void updateToolBarButtons() {
        if (isRendered()) {
            if (accountId == null) {
                getAddEntityButton().disable();
                getEditEntityButton().disable();
                getDeleteEntityButton().disable();
            } else {
                getAddEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.write()));
                if (gridSelectionModel.getSelectedItem() != null) {
                    getEditEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.write()));
                    getDeleteEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.delete()));
                } else {
                    getEditEntityButton().disable();
                    getDeleteEntityButton().disable();
                }
            }
        }
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new UserAddDialog(currentSession, accountId);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserEditDialog dialog = null;
        if (selectedUser != null) {
            dialog = new UserEditDialog(currentSession, selectedUser);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserDeleteDialog dialog = null;
        if (selectedUser != null) {
            dialog = new UserDeleteDialog(selectedUser);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        setBorders(false);

        getAddEntityButton().setEnabled(accountId != null && currentSession != null && currentSession.hasPermission(UserSessionPermission.write()));
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
    }

}
