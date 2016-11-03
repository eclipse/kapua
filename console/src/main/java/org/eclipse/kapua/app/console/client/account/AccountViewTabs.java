/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.client.account;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.client.user.UserView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class AccountViewTabs extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private TabPanel m_tabsPanel;
    private TabItem m_generalTab;
    private TabItem m_usersTab;
    private AccountDetailsView m_centerAccountViewGeneralTab;

    private UserView m_centerAccountViewUserTab;

    private GwtSession m_currentSession;
    private AccountView m_centerAccountView;

    public AccountViewTabs(AccountView centerAccountView, GwtSession currentSession) {

        m_centerAccountView = centerAccountView;
        m_currentSession = currentSession;

        m_centerAccountViewGeneralTab = new AccountDetailsView(m_centerAccountView, m_currentSession);
        m_centerAccountViewUserTab = new UserView(m_currentSession);
    }

    public void setAccount(GwtAccount selectedAccount) {
        m_centerAccountViewGeneralTab.setAccount(selectedAccount);
        m_centerAccountViewUserTab.setAccount(selectedAccount);

        if (m_tabsPanel.getSelectedItem() == m_generalTab) {
            m_centerAccountViewGeneralTab.refresh();
        } else {
            m_centerAccountViewUserTab.refresh();
        }
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());

        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(true);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setBodyBorder(false);

        m_generalTab = new TabItem(MSGS.tabGeneral(), new KapuaIcon(IconSet.INFO));
        m_generalTab.setBorders(false);
        m_generalTab.setLayout(new FitLayout());
        m_generalTab.add(m_centerAccountViewGeneralTab);
        m_generalTab.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                m_centerAccountViewGeneralTab.refresh();
            }
        });
        m_tabsPanel.add(m_generalTab);

        if (m_currentSession.hasUserReadPermission()) {
            m_usersTab = new TabItem(MSGS.accountTabUsers(), new KapuaIcon(IconSet.USERS));
            m_usersTab.setBorders(false);
            m_usersTab.setLayout(new FitLayout());
            m_usersTab.add(m_centerAccountViewUserTab);
            m_usersTab.addListener(Events.Select, new Listener<ComponentEvent>() {

                public void handleEvent(ComponentEvent be) {
                    m_centerAccountViewUserTab.refresh();
                }
            });
            m_tabsPanel.add(m_usersTab);
        }

        m_tabsPanel.addListener(Events.Select, new Listener<TabPanelEvent>() {

            public void handleEvent(TabPanelEvent tpe) {
                TabItem tabItem = (TabItem) tpe.getItem();
                if (tabItem.getItem(0) instanceof AccountDetailsView) {
                    AccountDetailsView generalTab = (AccountDetailsView) tabItem.getItem(0);
                    generalTab.refresh();
                }
            }
        });

        add(m_tabsPanel);
    }
}
