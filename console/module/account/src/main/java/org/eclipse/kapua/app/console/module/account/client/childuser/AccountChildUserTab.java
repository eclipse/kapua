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
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.util.CssLiterals;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class AccountChildUserTab extends KapuaTabItem<GwtAccount> {

    protected AccountChildUserGrid userGrid;

    public AccountChildUserTab(GwtSession currentSession) {
        super(currentSession, "Users", new KapuaIcon(IconSet.USERS));
        this.currentSession = currentSession;
        setEnabled(false);
        userGrid = new AccountChildUserGrid(currentSession);
        userGrid.setRefreshOnRender(false);
    }

    @Override
    public void setEntity(GwtAccount t) {
        super.setEntity(t);
        setEnabled(t != null);
        userGrid.setSelectedAccount(t);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        this.add(userGrid);
        EntityCRUDToolbar<GwtUser> toolBar = userGrid.getToolbar();

        layout(true);
        toolBar.setStyleAttribute("border-top", CssLiterals.BORDER_0PX_NONE);
        toolBar.setStyleAttribute("border-left", CssLiterals.BORDER_0PX_NONE);
        toolBar.setStyleAttribute("border-right", CssLiterals.BORDER_0PX_NONE);
        toolBar.setStyleAttribute("border-bottom", "1px solid rgb(208, 208, 208)");
    }

    @Override
    protected void doRefresh() {
        userGrid.refresh();
    }

}
