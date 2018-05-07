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
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;
import org.eclipse.kapua.app.console.module.authentication.shared.model.permission.CredentialSessionPermission;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CredentialGrid extends EntityGrid<GwtCredential> {

    private static final ConsoleCredentialMessages CREDENTIAL_MSGS = GWT.create(ConsoleCredentialMessages.class);
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final GwtCredentialServiceAsync GWT_CREDENTIAL_SERVICE = GWT.create(GwtCredentialService.class);
    private GwtCredentialQuery query;
    private String selectedUserId;
    private String selectedUserName;

    private CredentialToolbar toolbar;

    public CredentialGrid(AbstractEntityView<GwtCredential> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtCredentialQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtCredential>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtCredential>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtCredential>> callback) {
                if (query.getUserId() == null) {
                    callback.onSuccess(new BasePagingLoadResult<GwtCredential>(new ArrayList<GwtCredential>()));
                } else {
                    GWT_CREDENTIAL_SERVICE.query((PagingLoadConfig) loadConfig,
                            query,
                            callback);
                }
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtCredential selectedItem) {
        super.selectionChangedEvent(selectedItem);
        updateToolbarButtons();
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", CREDENTIAL_MSGS.gridCredentialColumnHeaderStatus(), 50);
        GridCellRenderer<GwtCredential> setStatusIcon = new GridCellRenderer<GwtCredential>() {

            @Override
            public String render(GwtCredential gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtCredential> deviceList, Grid<GwtCredential> grid) {

                KapuaIcon icon;
                if (gwtUser.getCredentialStatusEnum() != null) {
                    switch (gwtUser.getCredentialStatusEnum()) {
                    case DISABLED:
                        icon = new KapuaIcon(IconSet.KEY);
                        icon.setColor(Color.RED);
                        icon.setTitle(MSGS.disabled());
                        break;

                    case ENABLED:
                        icon = new KapuaIcon(IconSet.KEY);
                        icon.setColor(Color.GREEN);
                        icon.setTitle(MSGS.enabled());
                        break;

                    default:
                        icon = new KapuaIcon(IconSet.KEY);
                        icon.setColor(Color.GREY);
                        icon.setTitle(MSGS.unknown());
                        break;
                    }
                } else {
                    icon = new KapuaIcon(IconSet.KEY);
                    icon.setColor(Color.GREY);
                    icon.setTitle(MSGS.unknown());
                }
                return icon.getInlineHTML();
            }
        };
        columnConfig.setRenderer(setStatusIcon);
        columnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("lockoutReset", CREDENTIAL_MSGS.gridCredentialColumnHeaderLockStatus(), 50);
        GridCellRenderer<GwtCredential> setLockoutIcon = new GridCellRenderer<GwtCredential>() {

            @Override
            public String render(GwtCredential gwtCredential, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtCredential> deviceList, Grid<GwtCredential> grid) {
                KapuaIcon icon;
                if (gwtCredential.getLockoutReset() != null && gwtCredential.getLockoutReset().after(new Date())) {
                    icon = new KapuaIcon(IconSet.LOCK);
                    icon.setColor(Color.RED);
                    icon.setTitle(CREDENTIAL_MSGS.gridCredentialLockedUntil(DateUtils.formatDateTime(gwtCredential.getLockoutReset())));
                } else {
                    icon = new KapuaIcon(IconSet.UNLOCK);
                    icon.setColor(Color.GREEN);
                    icon.setTitle(CREDENTIAL_MSGS.gridCredentialUnlocked());
                }
                return icon.getInlineHTML();
            }
        };
        columnConfig.setRenderer(setLockoutIcon);
        columnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("id", CREDENTIAL_MSGS.gridCredentialColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("credentialType", CREDENTIAL_MSGS.gridCredentialColumnHeaderCredentialType(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("expirationDateFormatted", CREDENTIAL_MSGS.gridCredentialColumnHeaderExpirationDate(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedOnFormatted", CREDENTIAL_MSGS.gridCredentialColumnHeaderModifiedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedByName", CREDENTIAL_MSGS.gridCredentialColumnHeaderModifiedBy(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtCredentialQuery) filterQuery;
    }

    @Override
    public CredentialToolbar getToolbar() {
        if (toolbar == null) {
            toolbar = new CredentialToolbar(currentSession);
            toolbar.setBorders(false);
        }
        return toolbar;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
        query.setUserId(selectedUserId);
        getToolbar().setSelectedUserId(selectedUserId);
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
        getToolbar().setSelectedUserName(selectedUserName);
    }

    @Override
    public void refresh() {
        super.refresh();
        updateToolbarButtons();
    }

    @Override
    public void refresh(GwtQuery query) {
        setFilterQuery(query);
        refresh();
    }

    private void updateToolbarButtons() {
        getToolbar().getAddEntityButton().setEnabled(selectedUserId != null);
        getToolbar().getEditEntityButton().setEnabled(getSelectionModel().getSelectedItem() != null && currentSession.hasPermission(CredentialSessionPermission.write()));
        getToolbar().getDeleteEntityButton().setEnabled(getSelectionModel().getSelectedItem() != null && currentSession.hasPermission(CredentialSessionPermission.delete()));
        getToolbar().getUnlockButton().setEnabled(
                getSelectionModel().getSelectedItem() != null &&
                        getSelectionModel().getSelectedItem().getLockoutReset() != null &&
                        currentSession.hasPermission(CredentialSessionPermission.write()));
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        /* Despite this grid, being a "slave" grid (i.e. a grid that depends on the value
         * selected in another grid) and so not refreshed on render (see comment in
         * EntityGrid class), it should be refreshed anyway on render if no item is
         * selected on the master grid, otherwise the paging toolbar will still be enabled
         * even if no results are actually available in this grid */
        if (selectedUserId == null) {
            refresh();
        }
    }
}
