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
package org.eclipse.kapua.app.console.module.account.client;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;

public class AccountView extends AbstractEntityView<GwtAccount> {

    private AccountGrid accountGrid;
    private AccountFilterPanel filterPanel;

    private static final ConsoleAccountMessages MSGS = GWT.create(ConsoleAccountMessages.class);

    public AccountView(GwtSession currentSession) {
        super(currentSession);
    }

    public static String getName() {
        return MSGS.childAccounts();
    }

    @Override
    public EntityGrid<GwtAccount> getEntityGrid(AbstractEntityView<GwtAccount> entityView, GwtSession currentSession) {
        if(accountGrid == null){
            accountGrid = new AccountGrid(this, currentSession);
        }
        return accountGrid;
    }

    @Override
    public EntityFilterPanel<GwtAccount> getEntityFilterPanel(AbstractEntityView<GwtAccount> entityView, GwtSession currentSession) {
        if(filterPanel == null){
            filterPanel = new AccountFilterPanel(entityView, currentSession);
        }
        return filterPanel;
    }
}
