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
import org.eclipse.kapua.app.console.client.ui.button.AddButton;
import org.eclipse.kapua.app.console.client.ui.button.Button;
import org.eclipse.kapua.app.console.client.ui.button.DeleteButton;
import org.eclipse.kapua.app.console.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccountView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);

    private GwtSession m_currentSession;

    private Grid<GwtAccount> m_grid;
    private BaseListLoader<ListLoadResult<GwtAccount>> m_accountLoader;

    private ToolBar m_accountsToolBar;
    private Button m_newButton;
    private Button m_editButton;

    private Button m_refreshButton;
    private boolean refreshProcess;

    private Button m_deleteButton;
    private boolean deleteProcess;

    private ColumnModel m_columnModel;

    private ContentPanel m_accountTabsPanel;
    private AccountViewTabs m_accountTabs;

    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    public AccountView(GwtSession currentSession) {
        m_currentSession = currentSession;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        //
        // Borderlayout that expands to the whole screen
        setLayout(new FitLayout());
        setBorders(false);

        LayoutContainer mf = new LayoutContainer();
        mf.setLayout(new BorderLayout());

        //
        // Center Panel: Accounts Table
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, .45F);
        centerData.setMargins(new Margins(0, 0, 0, 0));
        centerData.setSplit(true);
        centerData.setMinSize(0);

        ContentPanel accountsTablePanel = new ContentPanel();
        accountsTablePanel.setBorders(true);
        accountsTablePanel.setBodyBorder(false);
        accountsTablePanel.setHeaderVisible(false);
        accountsTablePanel.setScrollMode(Scroll.AUTO);
        accountsTablePanel.setLayout(new FitLayout());

        initAccountsToolBar();

        accountsTablePanel.setTopComponent(m_accountsToolBar);
        accountsTablePanel.add(getAccountsGrid());
        mf.add(accountsTablePanel, centerData);

        //
        // South Panel: Tabs
        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, .55F);
        southData.setMargins(new Margins(5, 0, 0, 0));
        southData.setSplit(true);

        m_accountTabsPanel = new ContentPanel();
        m_accountTabsPanel.setBorders(false);
        m_accountTabsPanel.setBodyBorder(false);
        m_accountTabsPanel.setHeaderVisible(false);
        m_accountTabsPanel.setScrollMode(Scroll.AUTO);
        m_accountTabsPanel.setLayout(new FitLayout());
        m_accountTabs = new AccountViewTabs(this, m_currentSession);
        m_accountTabsPanel.add(m_accountTabs);

        mf.add(m_accountTabsPanel, southData);

        add(mf);
    }

    private void initAccountsToolBar() {

        m_accountsToolBar = new ToolBar();

        //
        // New Account Button
        m_newButton = new AddButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final AccountForm accountForm = new AccountForm(m_currentSession);
                accountForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                    public void handleEvent(ComponentEvent be) {
                        // add the new account to the grid and select it
                        if (accountForm.getNewAccount() != null) {
                            updateAccountGrid(accountForm.getNewAccount());
                        }
                    }
                });
                accountForm.show();
            }

        });

        m_newButton.setEnabled(m_currentSession.hasAccountCreatePermission());
        m_accountsToolBar.add(m_newButton);
        m_accountsToolBar.add(new SeparatorToolItem());

        //
        // Edit Account Button
        m_editButton = new EditButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_grid != null) {
                    GwtAccount gwtAccount = m_grid.getSelectionModel().getSelectedItem();
                    if (gwtAccount != null) {
                        final AccountForm accountForm = new AccountForm(m_currentSession, m_grid.getSelectionModel().getSelectedItem());
                        accountForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                            public void handleEvent(ComponentEvent be) {
                                updateAccountGrid(accountForm.getExistingAccount());
                            }
                        });
                        accountForm.show();
                    }
                }
            }

        });
        m_editButton.setEnabled(false);
        m_accountsToolBar.add(m_editButton);
        m_accountsToolBar.add(new SeparatorToolItem());

        //
        // Refresh Button
        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshProcess = true;

                    updateAccountGrid(null);

                    refreshProcess = false;
                }
            }
        });

        m_refreshButton.setEnabled(true);
        m_accountsToolBar.add(m_refreshButton);
        m_accountsToolBar.add(new SeparatorToolItem());

        //
        // Delete Account Button
        m_deleteButton = new DeleteButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if ((m_grid != null) && (!deleteProcess)) {
                    final GwtAccount gwtAccount = m_grid.getSelectionModel().getSelectedItem();
                    if (gwtAccount != null) {
                        // ask for confirmation
                        MessageBox.confirm(MSGS.confirm(), MSGS.accountDeleteConfirmation(gwtAccount.getName()),
                                new Listener<MessageBoxEvent>() {

                                    public void handleEvent(MessageBoxEvent ce) {
                                        // if confirmed, delete
                                        Dialog dialog = ce.getDialog();
                                        if (dialog.yesText.equals(ce.getButtonClicked().getText())) {

                                            //
                                            // Getting XSRF token
                                            gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                                @Override
                                                public void onFailure(Throwable ex) {
                                                    FailureHandler.handle(ex);
                                                    deleteProcess = false;
                                                }

                                                @Override
                                                public void onSuccess(GwtXSRFToken token) {
                                                    gwtAccountService.delete(token,
                                                            gwtAccount,
                                                            new AsyncCallback<Void>() {

                                                                public void onFailure(Throwable caught) {
                                                                    FailureHandler.handle(caught);
                                                                    deleteProcess = false;
                                                                }

                                                                public void onSuccess(Void arg) {
                                                                    ConsoleInfo.display(MSGS.info(),
                                                                            MSGS.accountDeletedConfirmation(gwtAccount.getUnescapedName()));
                                                                    updateAccountGrid(null);
                                                                    // gwtAccountUtils.loadChildAccounts();
                                                                    deleteProcess = false;
                                                                }
                                                            });
                                                }
                                            });

                                        }
                                    }
                                });
                    }
                }
            }
        });

        m_deleteButton.setEnabled(false);

        m_accountsToolBar.add(m_deleteButton);
    }

    private Grid<GwtAccount> getAccountsGrid() {
        //
        // Column Configuration
        ColumnConfig column = null;
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        column = new ColumnConfig("status", 30);
        column.setAlignment(HorizontalAlignment.CENTER);
        GridCellRenderer<GwtAccount> setStatusIcon = new GridCellRenderer<GwtAccount>() {

            public String render(GwtAccount model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtAccount> employeeList, Grid<GwtAccount> grid) {
                return "<image src=\"eclipse/org/eclipse/kapua/app/console/icon/green.gif\" width=\"12\" height=\"12\" style=\"vertical-align: bottom\" title=\"" + MSGS.enabled() + "\"/>";
            }
        };
        column.setRenderer(setStatusIcon);
        configs.add(column);

        column = new ColumnConfig("name", 120);
        column.setHeader(MSGS.accountTableName());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("modifiedOnFormatted", MSGS.accountTableModifiedOn(), 130);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        m_columnModel = new ColumnModel(configs);

        // rpc data proxy
        RpcProxy<ListLoadResult<GwtAccount>> proxy = new RpcProxy<ListLoadResult<GwtAccount>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtAccount>> callback) {
                gwtAccountService.findChildren(m_currentSession.getSelectedAccount().getId(), false, callback);
            }
        };

        // grid loader
        m_accountLoader = new BaseListLoader<ListLoadResult<GwtAccount>>(proxy);
        SwappableListStore<GwtAccount> store = new SwappableListStore<GwtAccount>(m_accountLoader);
        store.setKeyProvider(new ModelKeyProvider<GwtAccount>() {

            public String getKey(GwtAccount gwtAccount) {
                return String.valueOf(gwtAccount.getId());
            }
        });

        //
        // Grid
        m_grid = new Grid<GwtAccount>(store, m_columnModel);
        m_grid.setBorders(false);
        m_grid.setStateful(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.setTrackMouseOver(false);
        m_grid.setAutoExpandColumn("name");
        m_grid.mask(MSGS.loading());
        m_grid.getView().setAutoFill(true);
        GridView gridView = m_grid.getView();
        gridView.setEmptyText(MSGS.accountTableNoAccounts());

        m_accountLoader.addLoadListener(new DataLoadListener(m_grid));

        GridSelectionModel<GwtAccount> selectionModel = new GridSelectionModel<GwtAccount>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        m_grid.setSelectionModel(selectionModel);
        m_grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtAccount>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtAccount> se) {
                // Manage buttons
                if (se.getSelectedItem() != null) {
                    m_editButton.setEnabled(m_currentSession.hasAccountUpdatePermission());
                    m_deleteButton.setEnabled(m_currentSession.hasAccountDeletePermission());
                } else {
                    m_editButton.setEnabled(false);
                    m_deleteButton.setEnabled(false);
                }

                // Set account on tabs
                if (se.getSelectedItem() != null) {
                    m_accountTabs.setAccount(m_grid.getSelectionModel().getSelectedItem());
                }
            }
        });

        //
        // populate with initial status
        updateAccountGrid(null);

        return m_grid;
    }

    protected void updateAccountGrid(final GwtAccount selection) {
        m_accountLoader.load();
        if (selection != null) {
            m_accountTabs.setAccount(selection);
        }
    }

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        private Grid<GwtAccount> m_accountsGrid;
        private GwtAccount m_selectedAccount;

        public DataLoadListener(Grid<GwtAccount> accountsGrid) {
            m_accountsGrid = accountsGrid;
            m_selectedAccount = null;
        }

        public void loaderBeforeLoad(LoadEvent le) {
            m_selectedAccount = m_accountsGrid.getSelectionModel().getSelectedItem();
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

            ListStore<GwtAccount> store = m_accountsGrid.getStore();
            if (m_selectedAccount != null) {
                GwtAccount modelAccount = store.findModel(String.valueOf(m_selectedAccount.getId()));
                if (modelAccount != null) {
                    m_accountsGrid.getSelectionModel().select(modelAccount, false);
                    m_accountsGrid.getView().focusRow(store.indexOf(modelAccount));
                    m_accountTabs.setAccount(modelAccount);
                    return;
                }
            }

            if (m_accountsGrid.getSelectionModel().getSelectedItem() == null) {
                if (store.getCount() > 0) {
                    GwtAccount modelAccount = store.getAt(0);
                    m_accountsGrid.getSelectionModel().select(0, false);
                    m_accountsGrid.getView().focusRow(0);
                    m_accountTabs.setAccount(modelAccount);
                }
            } else {
                m_accountTabs.setAccount(null);
            }
        }
    }
}
