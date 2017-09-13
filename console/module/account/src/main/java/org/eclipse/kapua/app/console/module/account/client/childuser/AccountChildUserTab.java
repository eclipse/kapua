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
package org.eclipse.kapua.app.console.module.account.client.childuser;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;

import com.google.gwt.user.client.Element;

public class AccountChildUserTab extends KapuaTabItem<GwtAccount> {

    protected AccountChildUserGrid userGrid;
    protected GwtSession currentSession;

    public AccountChildUserTab(GwtSession currentSession) {
        super("Users", new KapuaIcon(IconSet.USERS));
        this.currentSession = currentSession;
        userGrid = new AccountChildUserGrid(currentSession);

    }

    @Override
    public void setEntity(GwtAccount t) {
        super.setEntity(t);
        userGrid.setSelectedAccount(t);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        this.add(userGrid);
    }

    @Override
    protected void doRefresh() {
        userGrid.refresh();
    }

}
