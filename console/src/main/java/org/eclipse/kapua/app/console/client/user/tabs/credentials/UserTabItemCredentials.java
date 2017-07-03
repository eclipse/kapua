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
package org.eclipse.kapua.app.console.client.user.tabs.credentials;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class UserTabItemCredentials extends KapuaTabItem<GwtUser> {

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private CredentialGrid credentialsGrid;

    public UserTabItemCredentials(GwtSession currentSession) {
        super(MSGS.gridUserTabCredentialsLabel(), new KapuaIcon(IconSet.KEY));
        credentialsGrid = new CredentialGrid(null, currentSession);
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
    public void setEntity(GwtUser user) {
        super.setEntity(user);
        credentialsGrid.setSelectedUser(user);
    }

    @Override
    protected void doRefresh() {
        credentialsGrid.refresh();
        credentialsGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null);
        credentialsGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
