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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.role;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class UserTabAccessRoleGrid extends EntityGrid<GwtAccessRole> {

    private static final GwtAccessRoleServiceAsync GWT_ACCESS_ROLE_SERVICE = GWT.create(GwtAccessRoleService.class);

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

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
                GWT_ACCESS_ROLE_SERVICE.findByUserId((PagingLoadConfig) loadConfig,
                        currentSession.getSelectedAccountId(),
                        userId,
                        callback);
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtAccessRole selectedItem) {
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

        ColumnConfig columnConfig = new ColumnConfig("roleId", MSGS.gridRoleColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("roleName", MSGS.gridRoleColumnHeaderName(), 400);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridRoleColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", MSGS.gridRoleColumnHeaderCreatedBy(), 200);
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
    public EntityCRUDToolbar<GwtAccessRole> getToolbar() {
        if (toolbar == null) {
            toolbar = new UserTabAccessRoleToolbar(currentSession);
            toolbar.setEditButtonVisible(false);
            toolbar.setBorders(false);
        }
        return toolbar;
    }
}
