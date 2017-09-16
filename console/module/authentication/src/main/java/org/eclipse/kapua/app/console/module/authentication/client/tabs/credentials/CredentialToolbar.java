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
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;

public class CredentialToolbar extends EntityCRUDToolbar<GwtCredential> {

    private String selectedUserId;
    private String selectedUserName;

    public CredentialToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        updateAddButtonEnabled();
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
        getRefreshEntityButton().disable();
        getFilterButton().hide();
        setBorders(false);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        if (selectedUserId != null) {
            return new CredentialAddDialog(currentSession, selectedUserId, selectedUserName);
        }
        return null;
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtCredential selectedCredential = gridSelectionModel.getSelectedItem();
        CredentialEditDialog dialog = null;
        if (selectedUserId != null && selectedCredential != null) {
            dialog = new CredentialEditDialog(currentSession, selectedCredential, selectedUserId, selectedUserName);
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

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
        if (this.isRendered()) {
            updateAddButtonEnabled();
        }
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
    }

    private void updateAddButtonEnabled() {
        if (selectedUserId == null) {
            getAddEntityButton().disable();
        } else {
            getAddEntityButton().enable();
        }
    }
}
