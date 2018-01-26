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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.permission;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

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
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionServiceAsync;

public class UserTabPermissionGrid extends EntityGrid<GwtAccessPermission> {

    private static final GwtAccessPermissionServiceAsync GWT_ACCESS_PERMISSION_SERVICE = GWT.create(GwtAccessPermissionService.class);

    private static final ConsolePermissionMessages USR_MSGS = GWT.create(ConsolePermissionMessages.class);
    private static final ConsoleMessages COMMONS_MSGS = GWT.create(ConsoleMessages.class);

    private String userId;

    private UserTabPermissionToolbar toolbar;

    public UserTabPermissionGrid(AbstractEntityView<GwtAccessPermission> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtAccessPermission>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtAccessPermission>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtAccessPermission>> callback) {
                GWT_ACCESS_PERMISSION_SERVICE.findByUserId((PagingLoadConfig) loadConfig,
                        currentSession.getSelectedAccountId(),
                        userId,
                        callback);
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtAccessPermission selectedItem) {
        super.selectionChangedEvent(selectedItem);
        if (selectedItem == null) {
            toolbar.getDeleteEntityButton().disable();
        } else {
            toolbar.getDeleteEntityButton().enable();
        }
    }

    @Override
    protected List<ColumnConfig> getColumns() {

        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", USR_MSGS.gridAccessRoleColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionDomain", USR_MSGS.gridAccessRoleColumnHeaderDomain(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionAction", USR_MSGS.gridAccessRoleColumnHeaderAction(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionGroupId", USR_MSGS.gridAccessRoleColumnHeaderGroupId(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionForwardable", USR_MSGS.gridAccessRoleColumnHeaderForwardable(), 200);
        columnConfig.setRenderer(new GridCellRenderer<GwtAccessPermission>() {

            @Override
            public Object render(GwtAccessPermission model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtAccessPermission> store, Grid<GwtAccessPermission> grid) {
                return model.getPermissionForwardable() ? COMMONS_MSGS.yes() : COMMONS_MSGS.no();
            }
        });
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", USR_MSGS.gridAccessRoleColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", USR_MSGS.gridAccessRoleColumnHeaderCreatedBy(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return null;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
    }

    @Override
    public EntityCRUDToolbar<GwtAccessPermission> getToolbar() {
        if (toolbar == null) {
            toolbar = new UserTabPermissionToolbar(currentSession);
            toolbar.setEditButtonVisible(false);
            toolbar.setBorders(false);
        }
        return toolbar;
    }

}
