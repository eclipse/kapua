/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.client.toolbar.AccountGridToolbar;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.app.console.module.account.shared.model.permission.AccountSessionPermission;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.ModifiedByNameCellRenderer;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

import java.util.ArrayList;
import java.util.List;

public class AccountGrid extends EntityGrid<GwtAccount> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleAccountMessages ACCOUNT_MSGS = GWT.create(ConsoleAccountMessages.class);
    private final GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);
    private static final String CHILD_ACCOUNT = "child account";

    private GwtAccountQuery filterQuery;
    private AccountGridToolbar toolbar;

    AccountGrid(AbstractEntityView<GwtAccount> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        filterQuery = new GwtAccountQuery();
        filterQuery.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    public void refresh(GwtQuery query) {
        super.refresh(query);
        GwtAccount selectedAccount = getSelectionModel().getSelectedItem();
        updateToolBarButtons(selectedAccount);

    }

    @Override
    protected EntityCRUDToolbar<GwtAccount> getToolbar() {
        if (toolbar == null) {
            toolbar = new AccountGridToolbar(currentSession);
        }
        return toolbar;
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtAccount>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtAccount>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtAccount>> callback) {
                gwtAccountService.query((PagingLoadConfig) loadConfig, filterQuery, callback);
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtAccount selectedItem) {
        super.selectionChangedEvent(selectedItem);
        updateToolBarButtons(selectedItem);
        getToolbar().getAddEntityButton().setEnabled(currentSession.hasPermission(AccountSessionPermission.write()));
        if (selectedItem != null) {
            getToolbar().getEditEntityButton().setEnabled(currentSession.hasPermission(AccountSessionPermission.write()));
            getToolbar().getDeleteEntityButton().setEnabled(currentSession.hasPermission(AccountSessionPermission.delete()));
        } else {
            getToolbar().getEditEntityButton().setEnabled(false);
            getToolbar().getDeleteEntityButton().setEnabled(false);
        }
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        ColumnConfig column = null;
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        column = new ColumnConfig("name", 120);
        column.setHeader(ACCOUNT_MSGS.accountTableName());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("orgName", 120);
        column.setHeader(ACCOUNT_MSGS.accountTableOrgName());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("orgEmail", 120);
        column.setHeader(ACCOUNT_MSGS.accountTableOrgEmail());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("expirationDateFormatted", 120);
        column.setHeader(ACCOUNT_MSGS.accountTableExpirationDate());
        GridCellRenderer<GwtAccount> setExpirationDate = new GridCellRenderer<GwtAccount>() {

            @Override
            public Object render(GwtAccount gwtAccount, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<GwtAccount> store, Grid<GwtAccount> grid) {
                if (gwtAccount.getExpirationDateFormatted() != null) {
                    return gwtAccount.getExpirationDateFormatted();
                } else {
                    return MSGS.never();
                }
            }
        };
        column.setRenderer(setExpirationDate);
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("modifiedOnFormatted", ACCOUNT_MSGS.accountTableModifiedOn(), 130);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("modifiedByName", ACCOUNT_MSGS.accountTableModifiedBy(), 130);
        column.setRenderer(new ModifiedByNameCellRenderer<GwtAccount>());
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("contactName", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableContactName());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("phoneNumber", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTablePhoneNumber());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("address1", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableAddress1());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("address2", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableAddress2());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("address3", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableAddress3());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("zipPostCode", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableZipPostCode());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("city", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableCity());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("stateProvince", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableStateProvince());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("country", 120);
        column.setHidden(true);
        column.setHeader(ACCOUNT_MSGS.accountTableCountry());
        column.setWidth(150);
        column.setSortable(false);
        configs.add(column);

        return configs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return filterQuery;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.filterQuery = (GwtAccountQuery) filterQuery;
    }

    private void updateToolBarButtons(GwtAccount selectedAccount) {
        if (selectedAccount == null) {
            toolbar.getEditEntityButton().setEnabled(false);
            toolbar.getDeleteEntityButton().setEnabled(false);
        } else {
            toolbar.getEditEntityButton().setEnabled(true);
            toolbar.getDeleteEntityButton().setEnabled(true);
        }
    }

    @Override
    public String getEmptyGridText() {
        return MSGS.gridNoResultFound(CHILD_ACCOUNT);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return MSGS.specificPagingToolbarShowingPost(CHILD_ACCOUNT);
            }

            @Override
            public String pagingToolbarNoResult() {
                return MSGS.specificPagingToolbarNoResult(CHILD_ACCOUNT);
            }
        };
    }
}
