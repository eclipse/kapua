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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailsTabDescription extends KapuaTabItem<GwtAccount> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);

    private AccountView centerAccountView;
    private Button editButton;

    private ContentPanel tableContainer;
    private FormPanel formPanel;
    private Grid<GwtGroupedNVPair> grid;
    private GroupingStore<GwtGroupedNVPair> store;
    private BaseListLoader<ListLoadResult<GwtGroupedNVPair>> loader;
    private boolean dirty;
    private boolean initialized;
    private GwtAccount selectedAccount;
    private GwtSession currentSession;
    private AccountDetailsView accoountDetailsView;
    private ToolBar accountsToolBar;

    public AccountDetailsTabDescription(GwtSession currentSession, AccountDetailsView accoountDetailsView, AccountView centerAccountView) {
        super(MSGS.entityTabDescriptionTitle(), new KapuaIcon(IconSet.INFO));
        this.currentSession = currentSession;
        this.accoountDetailsView = accoountDetailsView;
        this.centerAccountView = centerAccountView;

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);
        initTable();

    }

    protected void initTable() {
        getAccountsToolBar();
        initGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(false);
        tableContainer.setTopComponent(accountsToolBar);
        tableContainer.setScrollMode(Scroll.AUTO);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(grid);
        add(tableContainer);

    }

    private void initGrid() {
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
        updateAccountInfo();
        add(grid);
    }

    protected void updateAccountInfo() {
        if (store != null) {
            store.removeAll();
            loader.load();
        }
    }

    private ToolBar getAccountsToolBar() {

        accountsToolBar = new ToolBar();
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
            editButton.setEnabled(true);

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

    public void setAccount(GwtAccount selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    private class DataLoadListener extends KapuaLoadListener {

        public DataLoadListener() {
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
        }
    }

    @Override
    protected void doRefresh() {
        // TODO Auto-generated method stub

    }

}
