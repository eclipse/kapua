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
package org.eclipse.kapua.app.console.client.account;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.commons.client.ui.button.Button;
import org.eclipse.kapua.app.console.commons.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.commons.client.util.FailureHandler;
import org.eclipse.kapua.app.console.commons.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.commons.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.shared.service.GwtAccountServiceAsync;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccountDetailsView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);

    private GwtSession currentSession;
    private AccountView centerAccountView;

    private GwtAccount selectedAccount;
    private Button editButton;

    private boolean dirty;
    private boolean initialized;

    private FormPanel formPanel;
    private Grid<GwtGroupedNVPair> grid;
    private GroupingStore<GwtGroupedNVPair> store;
    private BaseListLoader<ListLoadResult<GwtGroupedNVPair>> loader;

    public AccountDetailsView(AccountView centerAccountView, GwtSession currentSession) {
        this.centerAccountView = centerAccountView;
        this.currentSession = currentSession;

        dirty = true;
        initialized = false;
    }

    public void setAccount(GwtAccount selectedAccount) {
        dirty = true;
        this.selectedAccount = selectedAccount;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        // Borderlayout that expands to the whole screen
        setLayout(new FitLayout());

        LayoutContainer bodyLayoutContainer = new LayoutContainer();
        bodyLayoutContainer.setBorders(true);
        bodyLayoutContainer.setLayout(new BorderLayout());
        bodyLayoutContainer.setScrollMode(Scroll.AUTO);
        bodyLayoutContainer.setStyleAttribute("background-color", "#F0F0F0");
        bodyLayoutContainer.setStyleAttribute("padding", "0px");

        //
        // Toolbar
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 27.0F);
        northData.setMargins(new Margins(0, 0, 0, 0));
        northData.setSplit(false);
        bodyLayoutContainer.add(getAccountsToolBar(), northData);

        //
        // Center View
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 0, 0, 0));
        centerData.setSplit(false);

        createGrid(parent);
        bodyLayoutContainer.add(grid, centerData);

        //
        // South View

        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 430.0F);
        southData.setCollapsible(true);
        southData.setHideCollapseTool(true);
        southData.setSplit(true);
        southData.setMargins(new Margins(5, 0, 0, 0));
        TabPanel tabPanel = new TabPanel();
        tabPanel.setPlain(true);
        tabPanel.setBorders(false);
        tabPanel.setBodyBorder(false);
        AccountTabConfiguration settingsTabItem = new AccountTabConfiguration(currentSession);
        settingsTabItem.setEntity(selectedAccount);
        tabPanel.add(settingsTabItem);
        bodyLayoutContainer.add(tabPanel, southData);

        add(bodyLayoutContainer);
        initialized = true;

    }

    private void createGrid(Element parent) {
        RpcProxy<ListLoadResult<GwtGroupedNVPair>> proxy = new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                gwtAccountService.getAccountInfo(selectedAccount.getId(), callback);
            }
        };

        loader = new BaseListLoader<ListLoadResult<GwtGroupedNVPair>>(proxy);
        loader.addLoadListener(new DataLoadListener());

        store = new GroupingStore<GwtGroupedNVPair>(loader);
        store.groupBy("groupLoc");

        ColumnConfig name = new ColumnConfig("nameLoc", MSGS.devicePropName(), 50);
        ColumnConfig value = new ColumnConfig("value", MSGS.devicePropValue(), 50);

        List<ColumnConfig> config = new ArrayList<ColumnConfig>();
        config.add(name);
        config.add(value);

        ColumnModel cm = new ColumnModel(config);
        GroupingView view = new GroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.setEmptyText(MSGS.accountNoSelectedAccount());

        grid = new Grid<GwtGroupedNVPair>(store, cm);
        grid.setView(view);
        grid.setBorders(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.disableTextSelection(false);

        add(grid);
    }

    private ToolBar getAccountsToolBar() {
        ToolBar accountsToolBar = new ToolBar();
        accountsToolBar.setHeight("27px");
        if (currentSession.hasAccountUpdatePermission()) {
            //
            // Edit Account Button
            editButton = new EditButton(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (selectedAccount != null) {
                        final AccountForm accountForm = new AccountForm(currentSession, selectedAccount);
                        accountForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                            public void handleEvent(ComponentEvent be) {

                                // reload the account and update the grid
                                if (centerAccountView != null) {
                                    //m_centerAccountView.updateAccountGrid(accountForm.getExistingAccount());
                                }
                                setAccount(accountForm.getExistingAccount());
                                refresh();
                            }
                        });
                        accountForm.show();
                    }
                }
            });
            editButton.setEnabled(false);

            accountsToolBar.add(editButton);
            accountsToolBar.add(new SeparatorToolItem());
        }

        return accountsToolBar;
    }

    public void refresh() {
        if (initialized && dirty && selectedAccount != null) {
            updateAccountInfo();
            dirty = false;
        }
        if (selectedAccount != null) {
            if (formPanel != null) {
                formPanel.show();
            }
            editButton.setEnabled(true);
        } else {
            if (formPanel != null) {
                formPanel.hide();
            }
            editButton.setEnabled(false);
        }
    }

    private void updateAccountInfo() {
        store.removeAll();
        loader.load();
    }

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        public DataLoadListener() {
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
        }
    }
}
