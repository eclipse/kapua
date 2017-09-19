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
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtCredentialServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class CredentialGrid extends EntityGrid<GwtCredential> {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);
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

        ColumnConfig columnConfig = new ColumnConfig("status", MSGS.gridCredentialColumnHeaderStatus(), 50);
        GridCellRenderer<GwtCredential> setStatusIcon = new GridCellRenderer<GwtCredential>() {

            public String render(GwtCredential gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtCredential> deviceList, Grid<GwtCredential> grid) {

                KapuaIcon icon;
                if (gwtUser.getCredentialStatusEnum() != null) {
                    switch (gwtUser.getCredentialStatusEnum()) {
                    case DISABLED:
                        icon = new KapuaIcon(IconSet.KEY);
                        icon.setColor(Color.RED);
                        break;

                    case ENABLED:
                        icon = new KapuaIcon(IconSet.KEY);
                        icon.setColor(Color.GREEN);
                        break;

                    default:
                         icon = new KapuaIcon(IconSet.KEY);
                            icon.setColor(Color.GREY);
                        break;
                    }
                } else {
                    icon = new KapuaIcon(IconSet.KEY);
                    icon.setColor(Color.GREY);
                }
                return icon.getInlineHTML();
            }
        };
        columnConfig.setRenderer(setStatusIcon);
        columnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("id", MSGS.gridCredentialColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("credentialType", MSGS.gridCredentialColumnHeaderCredentialType(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("", MSGS.gridCredentialColumnHeaderExpirationDate(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOn", MSGS.gridCredentialColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", MSGS.gridCredentialColumnHeaderCreatedBy(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedOn", MSGS.gridCredentialColumnHeaderModifiedOn(), 200);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", MSGS.gridCredentialColumnHeaderModifiedBy(), 200);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtCredentialQuery) filterQuery;
    }

    @Override
    public EntityCRUDToolbar<GwtCredential> getToolbar() {
        if (toolbar == null) {
            toolbar = new CredentialToolbar(currentSession);
            toolbar.setBorders(false);
        }
        return toolbar;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
        if (selectedUserId != null) {
            query.setUserId(selectedUserId);
        }
        ((CredentialToolbar) getToolbar()).setSelectedUserId(selectedUserId);
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
        ((CredentialToolbar) getToolbar()).setSelectedUserName(selectedUserName);
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
        getToolbar().getEditEntityButton().setEnabled(getSelectionModel().getSelectedItem() != null && currentSession.hasCredentialUpdatePermission());
        getToolbar().getDeleteEntityButton().setEnabled(getSelectionModel().getSelectedItem() != null && currentSession.hasCredentialDeletePermission());
    }
}
