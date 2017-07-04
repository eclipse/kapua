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
package org.eclipse.kapua.app.console.client.account.childuser;

import org.eclipse.kapua.app.console.commons.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.commons.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.client.user.dialog.UserAddDialog;
import org.eclipse.kapua.app.console.client.user.dialog.UserDeleteDialog;
import org.eclipse.kapua.app.console.client.user.dialog.UserEditDialog;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

import com.google.gwt.user.client.Element;

public class AccountChildUserToolbar extends EntityCRUDToolbar<GwtUser> {

    private GwtSession selectedAccountSession;

    public AccountChildUserToolbar(GwtSession currentSession) {
        super(currentSession);
        selectedAccountSession = new GwtSession();
        selectedAccountSession.setSelectedAccountId(null);
    }

    public void setSelectedAccountId(String accountId) {
        selectedAccountSession.setSelectedAccountId(accountId);
        updateToolBarButtons();
    }

    public void updateToolBarButtons() {
        if (isRendered()) {
            if (selectedAccountSession.getSelectedAccountId() == null) {
                getAddEntityButton().disable();
                getEditEntityButton().disable();
                getDeleteEntityButton().disable();
            } else {
                if (currentSession.hasUserCreatePermission()) {
                    getAddEntityButton().enable();
                }
                if (gridSelectionModel.getSelectedItem() != null) {
                    if (currentSession.hasUserUpdatePermission()) {
                        getEditEntityButton().enable();
                    }
                    if (currentSession.hasUserDeletePermission()) {
                        getDeleteEntityButton().enable();
                    }
                } else {
                    getEditEntityButton().disable();
                    getDeleteEntityButton().disable();
                }
            }
        }
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new UserAddDialog(selectedAccountSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserEditDialog dialog = null;
        if (selectedUser != null) {
            dialog = new UserEditDialog(selectedAccountSession, selectedUser);
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
        if (selectedAccountSession.getSelectedAccountId() != null && currentSession != null && currentSession.hasUserCreatePermission()) {
            getAddEntityButton().enable();
        } else {
            getAddEntityButton().disable();
        }
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
    }

}
