/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.client.role;

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
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.CreatedByNameCellRenderer;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.GroupSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class RolePermissionGrid extends EntityGrid<GwtRolePermission> {

    private static final ConsoleRoleMessages ROLE_MSGS = GWT.create(ConsoleRoleMessages.class);
    private static final ConsoleMessages COMMONS_MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtRoleServiceAsync GWT_ROLE_SERVICE = GWT.create(GwtRoleService.class);
    private static final String PERMISSION = "permission";

    RolePermissionToolbar rolePermissionToolBar;
    private GwtRole selectedRole;
    private GwtQuery filterQuery;

    protected RolePermissionGrid(AbstractEntityView<GwtRolePermission> entityView, GwtSession currentSession) {
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
    public String getEmptyGridText() {
        return COMMONS_MSGS.gridNoResultAvailable(PERMISSION);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return COMMONS_MSGS.specificPagingToolbarShowingPost(PERMISSION);
            }

            @Override
            public String pagingToolbarNoResult() {
                return COMMONS_MSGS.specificPagingToolbarNoResult(PERMISSION);
            }
        };
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtRolePermission>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtRolePermission>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtRolePermission>> callback) {
                if (selectedRole != null) {
                    GWT_ROLE_SERVICE.getRolePermissions((PagingLoadConfig) loadConfig, selectedRole.getScopeId(), selectedRole.getId(), callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtRolePermission>(new ArrayList<GwtRolePermission>()));
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", ROLE_MSGS.gridRolePermissionColumnHeaderId(), 100);
        columnConfig.setSortable(false);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("domain", ROLE_MSGS.gridRolePermissionColumnHeaderDomain(), 100);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("action", ROLE_MSGS.gridRolePermissionColumnHeaderAction(), 100);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("targetScopeIdByName", ROLE_MSGS.gridRolePermissionColumnHeaderTargetScopeId(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        if (currentSession.hasPermission(GroupSessionPermission.read())) {
            columnConfig = new ColumnConfig("groupName", ROLE_MSGS.gridRolePermissionColumnHeaderTargetGroup(), 100);
            columnConfig.setSortable(false);
            columnConfigs.add(columnConfig);
        }

        columnConfig = new ColumnConfig("forwardable", ROLE_MSGS.gridRolePermissionColumnHeaderForwardable(), 200);
        columnConfig.setRenderer(new GridCellRenderer<GwtRolePermission>() {

            @Override
            public Object render(GwtRolePermission model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtRolePermission> store, Grid<GwtRolePermission> grid) {
                return model.getForwardable() ? COMMONS_MSGS.yes() : COMMONS_MSGS.no();
            }
        });
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", ROLE_MSGS.gridRolePermissionColumnHeaderCreatedOn(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", ROLE_MSGS.gridRolePermissionColumnHeaderCreatedBy(), 100);
        columnConfig.setRenderer(new CreatedByNameCellRenderer<GwtRolePermission>());
        columnConfig.setSortable(false);
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
        rolePermissionToolBar.getAddEntityButton().setEnabled(selectedRole != null
                && currentSession.hasPermission(RoleSessionPermission.write()));
        if (selectedItem == null) {
            rolePermissionToolBar.getDeleteEntityButton().disable();
        } else {
            rolePermissionToolBar.getDeleteEntityButton()
                    .setEnabled(currentSession.hasPermission(RoleSessionPermission.delete()));
        }
    }

    public void clear() {
        entityStore.removeAll();
    }

    @Override
    public GwtQuery getFilterQuery() {
        return filterQuery;
    }

    @Override
    public void setFilterQuery(GwtQuery query) {
        this.filterQuery = query;
    }

}
