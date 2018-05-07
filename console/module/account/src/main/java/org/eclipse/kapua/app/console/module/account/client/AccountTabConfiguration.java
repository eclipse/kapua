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
package org.eclipse.kapua.app.console.module.account.client;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountTabConfiguration extends KapuaTabItem<GwtAccount> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private AccountConfigComponents configComponents;
    private AccountDetailsView accountDetailsView;
    private AccountDetailsTabDescription accountDetailsTabDescription;

    public AccountTabConfiguration(GwtSession currentSession, AccountDetailsView accountDetailsView) {
        super(currentSession, MSGS.settingsTabTitle(), new KapuaIcon(IconSet.COG));
        this.accountDetailsView = accountDetailsView;
        configComponents = new AccountConfigComponents(currentSession, this);
        setEnabled(false);

        setBorders(false);
        setLayout(new FitLayout());
        addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                refresh();
            }
        });
    }

    @Override
    public void setEntity(GwtAccount selectedAccount) {
        super.setEntity(selectedAccount);
        if (selectedAccount != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
        configComponents.setAccount(selectedAccount);
    }

    @Override
    protected void doRefresh() {
        configComponents.refresh();
    }

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("AccountTabsContainer");
        setLayout(new FitLayout());

        add(configComponents);
    }

    public void setDescriptionTab(AccountDetailsTabDescription accountDetailsTabDescription) {
        this.accountDetailsTabDescription = accountDetailsTabDescription;
        configComponents.setDescriptionTab(accountDetailsTabDescription);

    }

    public void removeElements(){
        configComponents.removeApplyAndResetButtons();
    }

}
