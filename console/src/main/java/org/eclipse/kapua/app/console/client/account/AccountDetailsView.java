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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.button.Button;
import org.eclipse.kapua.app.console.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
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

    private GwtSession m_currentSession;
    private AccountView m_centerAccountView;

    private GwtAccount selectedAccount;
    private Button m_editButton;
    private LayoutContainer m_bodyLayoutContainer;

    private boolean m_dirty;
    private boolean m_initialized;

    private FormPanel m_formPanel;
    private Grid<GwtGroupedNVPair> m_grid;
    private GroupingStore<GwtGroupedNVPair> m_store;
    private BaseListLoader<ListLoadResult<GwtGroupedNVPair>> m_loader;

    public AccountDetailsView(AccountView centerAccountView, GwtSession currentSession) {
        m_centerAccountView = centerAccountView;
        m_currentSession = currentSession;

        m_dirty = true;
        m_initialized = false;
    }

    public void setAccount(GwtAccount selectedAccount) {
        m_dirty = true;
        this.selectedAccount = selectedAccount;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        // Borderlayout that expands to the whole screen
        setLayout(new FitLayout());

        m_bodyLayoutContainer = new LayoutContainer();
        m_bodyLayoutContainer.setBorders(true);
        m_bodyLayoutContainer.setLayout(new BorderLayout());
        m_bodyLayoutContainer.setScrollMode(Scroll.AUTO);
        m_bodyLayoutContainer.setStyleAttribute("padding", "0px");
        m_bodyLayoutContainer.setStyleAttribute("background-color", "white");

        //
        // Toolbar
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 27.0F);
        northData.setMargins(new Margins(0, 0, 0, 0));
        northData.setSplit(false);
        m_bodyLayoutContainer.add(getAccountsToolBar(), northData);

        //
        // Center View
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 0, 0, 0));
        centerData.setSplit(false);

        createGrid(parent);
        m_bodyLayoutContainer.add(m_grid, centerData);

        add(m_bodyLayoutContainer);
        m_initialized = true;
    }

    private void createGrid(Element parent) {
        RpcProxy<ListLoadResult<GwtGroupedNVPair>> proxy = new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                gwtAccountService.getAccountInfo(selectedAccount.getId(), callback);
            }
        };

        m_loader = new BaseListLoader<ListLoadResult<GwtGroupedNVPair>>(proxy);
        m_loader.addLoadListener(new DataLoadListener());

        m_store = new GroupingStore<GwtGroupedNVPair>(m_loader);
        m_store.groupBy("groupLoc");

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

        m_grid = new Grid<GwtGroupedNVPair>(m_store, cm);
        m_grid.setView(view);
        m_grid.setBorders(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.disableTextSelection(false);

        add(m_grid);
    }

    private ToolBar getAccountsToolBar() {
        ToolBar accountsToolBar = null;
        if (m_currentSession.hasAccountUpdatePermission()) {
            accountsToolBar = new ToolBar();
            accountsToolBar.setHeight("27px");

            //
            // Edit Account Button
            m_editButton = new EditButton(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (selectedAccount != null) {
                        final AccountForm accountForm = new AccountForm(m_currentSession, selectedAccount);
                        accountForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                            public void handleEvent(ComponentEvent be) {

                                // reload the account and update the grid
                                if (m_centerAccountView != null) {
                                    m_centerAccountView.updateAccountGrid(accountForm.getExistingAccount());
                                }
                                setAccount(accountForm.getExistingAccount());
                                refresh();
                            }
                        });
                        accountForm.show();
                    }
                }
            });
            m_editButton.setEnabled(false);

            accountsToolBar.add(m_editButton);
            accountsToolBar.add(new SeparatorToolItem());
        }

        return accountsToolBar;
    }

    public void refresh() {
        if (m_initialized && m_dirty && selectedAccount != null) {
            updateAccountInfo();
            m_dirty = false;
        }
        if (selectedAccount != null) {
            if (m_formPanel != null) {
                m_formPanel.show();
            }
            m_editButton.setEnabled(true);
        } else {
            if (m_formPanel != null) {
                m_formPanel.hide();
            }
            m_editButton.setEnabled(false);
        }
    }

    private void updateAccountInfo() {
        m_store.removeAll();
        m_loader.load();
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
