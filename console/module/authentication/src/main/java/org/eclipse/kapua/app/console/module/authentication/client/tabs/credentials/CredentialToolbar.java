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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.permission.CredentialSessionPermission;

public class CredentialToolbar extends EntityCRUDToolbar<GwtCredential> {

    private String selectedUserId;
    private String selectedUserName;

    private final Button unlockButton;

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    public CredentialToolbar(GwtSession currentSession) {
        super(currentSession, true);
        unlockButton = new Button(MSGS.unlockButton(), new KapuaIcon(IconSet.UNLOCK), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                GwtCredential selectedCredential = gridSelectionModel.getSelectedItem();
                if (selectedUserId != null && selectedCredential != null) {
                    showUnlockDialog(selectedCredential);
                }
            }
        });
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        add(new SeparatorToolItem());

        if (currentSession.hasPermission(CredentialSessionPermission.write())) {
            add(unlockButton);
        }

        updateButtonsEnabled();
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
        getRefreshEntityButton().disable();
        unlockButton.disable();
        if (getFilterButton() != null) {
            getFilterButton().hide();
        }
        setBorders(true);
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
        if (isRendered()) {
            updateButtonsEnabled();
        }
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
    }

    private void updateButtonsEnabled() {
        getAddEntityButton().setEnabled(selectedUserId != null);
        //            unlockButton.setEnabled(selectedUserId != null);
    }

    public Button getUnlockButton() {
        return unlockButton;
    }

    private void showUnlockDialog(GwtCredential selectedCredential) {
        CredentialUnlockDialog dialog = new CredentialUnlockDialog(selectedCredential);
        dialog.addListener(Events.Hide, getHideDialogListener());
        dialog.show();
    }

}
