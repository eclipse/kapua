/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.user.client.tabs.credentials;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials.MfaManagementPanel;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class UserTabItemMfa extends KapuaTabItem<GwtUser> {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    private MfaManagementPanel managementPanel;

    public UserTabItemMfa(GwtSession currentSession) {
        super(currentSession, MSGS.gridTabMfaLabel(), new KapuaIcon(IconSet.LOCK));
        setEnabled(false);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);
    }

    @Override
    public void setEntity(GwtUser gwtUser) {
        super.setEntity(gwtUser);
        if (gwtUser != null) {
            setEnabled(true);
            if (managementPanel != null) {
                remove(managementPanel);
            }
            managementPanel = new MfaManagementPanel(currentSession, getSelectedEntity().getId(), getSelectedEntity().getUsername(), currentSession.getSelectedAccountId(), currentSession.getSelectedAccountName());
            add(managementPanel);
            layout(true);
        } else {
            setEnabled(false);
        }
    }

    @Override
    protected void doRefresh() {
    }

}
