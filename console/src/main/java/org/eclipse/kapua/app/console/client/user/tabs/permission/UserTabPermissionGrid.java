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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user.tabs.permission;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermission;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.shared.service.GwtAccessPermissionServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserTabPermissionGrid extends EntityGrid<GwtAccessPermission> {

    private static final GwtAccessPermissionServiceAsync gwtAccessPermissionService = GWT.create(GwtAccessPermissionService.class);

    private final static ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private String userId = null;

    private UserTabPermissionToolbar toolbar;

    public UserTabPermissionGrid(EntityView<GwtAccessPermission> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtAccessPermission>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtAccessPermission>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtAccessPermission>> callback) {
                gwtAccessPermissionService.findByUserId((PagingLoadConfig) loadConfig,
                        currentSession.getSelectedAccount().getId(),
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

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridAccessRoleColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionDomain", MSGS.gridAccessRoleColumnHeaderDomain(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionAction", MSGS.gridAccessRoleColumnHeaderAction(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionTargetScopeId", MSGS.gridAccessRoleColumnHeaderTargetScopeId(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("permissionGroupId", MSGS.gridAccessRoleColumnHeaderGroupId(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdBy", MSGS.gridAccessRoleColumnHeaderCreatedBy(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOn", MSGS.gridAccessRoleColumnHeaderCreatedOn(), 200);
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
    protected GwtQuery getFilterQuery() {
        return null;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
    }

    @Override
    public EntityCRUDToolbar<GwtAccessPermission> getToolbar() {
        if (toolbar == null) {
            toolbar = new UserTabPermissionToolbar(currentSession);
            toolbar.setEditButtonVisible(false);
        }
        return toolbar;
    }
    
}
