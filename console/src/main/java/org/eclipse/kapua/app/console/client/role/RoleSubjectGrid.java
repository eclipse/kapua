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
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtAccessInfoService;
import org.eclipse.kapua.app.console.shared.service.GwtAccessInfoServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleSubjectGrid extends EntityGrid<GwtUser> {

    private GwtRole selectedRole;
    private static final GwtAccessInfoServiceAsync service = GWT.create(GwtAccessInfoService.class);
    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    private GwtAccessRoleQuery query;

    protected RoleSubjectGrid(EntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtAccessRoleQuery();
        query.setScopeId(currentSession.getSelectedAccount().getId());

    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtUser>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtUser>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtUser>> callback) {
                if (selectedRole != null) {
                    service.query((PagingLoadConfig) loadConfig, query, callback);
                }
            }

        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridRoleSubjectColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", MSGS.gridRoleSubjectColumnHeaderName(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("type", MSGS.gridRoleSubjectColumnHeaderType(), 100);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {

        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtAccessRoleQuery) filterQuery;

    }

    @Override
    protected EntityCRUDToolbar<GwtUser> getToolbar() {
        EntityCRUDToolbar<GwtUser> toolbar = super.getToolbar();
        toolbar.setRefreshButtonVisible(true);
        toolbar.setAddButtonVisible(false);
        toolbar.setEditButtonVisible(false);
        toolbar.setDeleteButtonVisible(false);

        return toolbar;
    }

    public void setEntity(GwtRole gwtRole) {
        if (gwtRole != null) {
            selectedRole = gwtRole;
            query.setRoleId(selectedRole.getId());
        }
        refresh();
    }

    @Override
    public void refresh() {
        if (super.rendered) {
            super.refresh();
        }
    }

}
