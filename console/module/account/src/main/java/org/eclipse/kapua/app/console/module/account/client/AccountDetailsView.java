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

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountDetailsView extends AbstractView {

    private GwtSession currentSession;
    private AccountView centerAccountView;

    private GwtAccount selectedAccount;
    private static final ConsoleAccountMessages MSGS = GWT.create(ConsoleAccountMessages.class);

    private final TabPanel tabPanel = new TabPanel();

    public AccountDetailsView(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public AccountDetailsView(AccountView centerAccountView, GwtSession currentSession) {
        this(currentSession);
        this.centerAccountView = centerAccountView;
    }

    public static String getName() {
        return MSGS.settings();
    }

    public void setAccount(GwtAccount selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        LayoutContainer bodyLayoutContainer = new LayoutContainer();
        bodyLayoutContainer.setBorders(false);
        bodyLayoutContainer.setLayout(new BorderLayout());
        bodyLayoutContainer.setScrollMode(Scroll.AUTO);
        bodyLayoutContainer.setStyleAttribute("background-color", "#F0F0F0");
        bodyLayoutContainer.setStyleAttribute("padding", "2px 0px 0px 0px");

        LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
        resultContainer.setBorders(false);

        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.CENTER);
        northData.setMargins(new Margins(0, 0, 0, 0));
        northData.setMinSize(0);
        resultContainer.add(tabPanel, northData);

        tabPanel.setPlain(true);
        tabPanel.setBorders(false);
        tabPanel.setBodyBorder(false);
        AccountTabConfiguration settingsTabItem = new AccountTabConfiguration(currentSession, this);
        settingsTabItem.removeElements();
        settingsTabItem.setEntity(selectedAccount);

        AccountDetailsTabDescription accountDescriptionTab = new AccountDetailsTabDescription(currentSession);
        settingsTabItem.setDescriptionTab(accountDescriptionTab);
        accountDescriptionTab.setEntity(selectedAccount);
        tabPanel.add(accountDescriptionTab);
        tabPanel.add(settingsTabItem);
        bodyLayoutContainer.add(tabPanel, northData);

        add(bodyLayoutContainer);

    }

    public void refresh() {
        for (TabItem item : tabPanel.getItems()) {
            KapuaTabItem kapuaTabItem = (KapuaTabItem) item;
            kapuaTabItem.refresh();
        }
    }

}
