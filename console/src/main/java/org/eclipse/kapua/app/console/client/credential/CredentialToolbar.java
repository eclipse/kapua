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

import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;

import com.google.gwt.user.client.Element;

public class CredentialToolbar extends EntityCRUDToolbar<GwtCredential> {

    public CredentialToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return super.getAddDialog();
    }

    @Override
    protected KapuaDialog getEditDialog() {
        return super.getEditDialog();
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtCredential selectedUser = gridSelectionModel.getSelectedItem();
        CredentialDeleteDialog dialog = null;
        if (selectedUser != null) {
            dialog = new CredentialDeleteDialog(selectedUser);
        }
        return dialog;
    }
}
