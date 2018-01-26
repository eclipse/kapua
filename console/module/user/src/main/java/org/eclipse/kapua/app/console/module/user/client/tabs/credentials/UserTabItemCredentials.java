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
package org.eclipse.kapua.app.console.module.user.client.tabs.credentials;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials.CredentialGrid;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

public class UserTabItemCredentials extends KapuaTabItem<GwtUser> {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    private CredentialGrid credentialsGrid;

    public UserTabItemCredentials(GwtSession currentSession) {
        super(currentSession, MSGS.gridTabCredentialsLabel(), new KapuaIcon(IconSet.KEY));

        credentialsGrid = new CredentialGrid(null, currentSession);
        credentialsGrid.setRefreshOnRender(false);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(credentialsGrid);
    }

    public CredentialGrid getCredentialsGrid() {
        return credentialsGrid;
    }

    @Override
    public void setEntity(GwtUser gwtUser) {
        super.setEntity(gwtUser);
        credentialsGrid.setSelectedUserId(gwtUser != null ? gwtUser.getId() : null);
        credentialsGrid.setSelectedUserName(gwtUser != null ? gwtUser.getUsername() : null);
    }

    @Override
    protected void doRefresh() {
        credentialsGrid.refresh();
        credentialsGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null);
        credentialsGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
