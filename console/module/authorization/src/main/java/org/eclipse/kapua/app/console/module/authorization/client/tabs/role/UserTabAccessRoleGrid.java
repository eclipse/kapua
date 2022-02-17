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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.role;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
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
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class UserTabAccessRoleGrid extends EntityGrid<GwtAccessRole> {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    private static final ConsoleMessages C_MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtAccessRoleServiceAsync GWT_ACCESS_ROLE_SERVICE = GWT.create(GwtAccessRoleService.class);
    private static final String ASSIGNED_ROLE = "assigned role";

    private String userId;

    private UserTabAccessRoleToolbar toolbar;

    public UserTabAccessRoleGrid(GwtSession currentSession, AbstractEntityView<GwtAccessRole> entityView) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtAccessRole>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtAccessRole>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtAccessRole>> callback) {
                if (userId == null) {
                    callback.onSuccess(new BasePagingLoadResult<GwtAccessRole>(new ArrayList<GwtAccessRole>()));
                } else {
                    GWT_ACCESS_ROLE_SERVICE.findByUserId((PagingLoadConfig) loadConfig,
                            currentSession.getSelectedAccountId(),
                            userId,
                            callback);
                }
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtAccessRole selectedItem) {
        super.selectionChangedEvent(selectedItem);
        if (selectedItem == null) {
            toolbar.getDeleteEntityButton().disable();
        } else {
            toolbar.getDeleteEntityButton().setEnabled(currentSession.hasPermission(AccessInfoSessionPermission.delete()));
            toolbar.getAddEntityButton().setEnabled(currentSession.hasPermission(AccessInfoSessionPermission.write()));
        }
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("roleId", MSGS.gridRoleColumnHeaderId(), 100);
        columnConfig.setSortable(false);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("roleName", MSGS.gridRoleColumnHeaderName(), 400);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("roleDescription", MSGS.gridRoleColumnHeaderDescription(), 400);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridRoleColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", MSGS.gridRoleColumnHeaderGrantedBy(), 200);
        columnConfig.setSortable(false);
        columnConfig.setRenderer(new CreatedByNameCellRenderer<GwtAccessRole>());
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
    public EntityCRUDToolbar<GwtAccessRole> getToolbar() {
        if (toolbar == null) {
            toolbar = new UserTabAccessRoleToolbar(currentSession);
            toolbar.setEditButtonVisible(false);
            toolbar.setBorders(true);
        }
        return toolbar;
    }

    @Override
    public String getEmptyGridText() {
        return C_MSGS.gridNoResultAvailable(ASSIGNED_ROLE);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return C_MSGS.specificPagingToolbarShowingPost(ASSIGNED_ROLE);
            }

            @Override
            public String pagingToolbarNoResult() {
                return C_MSGS.specificPagingToolbarNoResult(ASSIGNED_ROLE);
            }
        };
    }
}
