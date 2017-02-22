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
package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RolePermissionGrid extends EntityGrid<GwtRolePermission> {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    RolePermissionToolbar rolePermissionToolBar;
    private GwtRole selectedRole;
    private GwtQuery filterQuery;

    protected RolePermissionGrid(EntityView<GwtRolePermission> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

    }

    @Override
    protected EntityCRUDToolbar<GwtRolePermission> getToolbar() {
        if (rolePermissionToolBar == null) {
            rolePermissionToolBar = new RolePermissionToolbar(currentSession, this);
        }

        return rolePermissionToolBar;
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtRolePermission>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtRolePermission>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtRolePermission>> callback) {
                if (selectedRole != null) {
                    gwtRoleService.getRolePermissions((PagingLoadConfig) loadConfig, selectedRole.getScopeId(), selectedRole.getId(), callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtRolePermission>(new ArrayList<GwtRolePermission>()));
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridRolePermissionColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("domain", MSGS.gridRolePermissionColumnHeaderDomain(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("action", MSGS.gridRolePermissionColumnHeaderAction(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("groupId", MSGS.gridRolePermissionColumnHeaderTargetGroup(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("targetScopeId", MSGS.gridRolePermissionColumnHeaderTargetScopeId(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdBy", MSGS.gridRolePermissionColumnHeaderCreatedBy(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridRolePermissionColumnHeaderCreatedOn(), 100);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    public void setSelectedRole(GwtRole gwtRole) {
        selectedRole = gwtRole;
        if (rolePermissionToolBar != null) {
            rolePermissionToolBar.setSelectedRole(selectedRole);
        }
    }

    @Override
    protected void selectionChangedEvent(GwtRolePermission selectedItem) {
        super.selectionChangedEvent(selectedItem);
        if (selectedItem == null) {
            rolePermissionToolBar.getDeleteEntityButton().disable();
        } else {
            rolePermissionToolBar.getDeleteEntityButton().enable();
        }
    }

    public void clear() {
        entityStore.removeAll();
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return filterQuery;
    }

    @Override
    protected void setFilterQuery(GwtQuery query) {
        this.filterQuery = query;
    }

}