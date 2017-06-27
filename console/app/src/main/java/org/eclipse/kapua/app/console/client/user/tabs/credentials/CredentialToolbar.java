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
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user.tabs.credentials;

import org.eclipse.kapua.app.console.commons.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.commons.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.commons.shared.model.GwtUser;

import com.google.gwt.user.client.Element;

public class CredentialToolbar extends EntityCRUDToolbar<GwtCredential> {

    private GwtUser selectedUser;

    public CredentialToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        updateAddButtonEnabled();
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
    }

    @Override
    protected KapuaDialog getAddDialog() {
        if (selectedUser != null) {
            return new CredentialAddDialog(currentSession, selectedUser);
        }
        return null;
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtCredential selectedCredential = gridSelectionModel.getSelectedItem();
        CredentialEditDialog dialog = null;
        if (selectedUser != null && selectedCredential != null) {
            dialog = new CredentialEditDialog(currentSession, selectedCredential, selectedUser);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtCredential selectedCredential = gridSelectionModel.getSelectedItem();
        CredentialDeleteDialog dialog = null;
        if (selectedCredential != null) {
            dialog = new CredentialDeleteDialog(selectedCredential);
        }
        return dialog;
    }

    public void setSelectedUser(GwtUser selectedUser) {
        this.selectedUser = selectedUser;
        if (this.isRendered()) {
            updateAddButtonEnabled();
        }
    }

    private void updateAddButtonEnabled() {
        if (selectedUser == null) {
            getAddEntityButton().disable();
        } else {
            getAddEntityButton().enable();
        }
    }
}
