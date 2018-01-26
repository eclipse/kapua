/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

public class RoleGrid extends EntityGrid<GwtRole> {

    private final static ConsoleRoleMessages ROLE_MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync GWT_ROLE_SERVICE = GWT.create(GwtRoleService.class);

    private GwtRoleQuery query;

    public RoleGrid(AbstractEntityView<GwtRole> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtRoleQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected RoleToolbarGrid getToolbar() {
        return new RoleToolbarGrid(currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtRole>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtRole>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtRole>> callback) {
                GWT_ROLE_SERVICE.query((PagingLoadConfig) loadConfig,
                        query,
                        callback);
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("name", ROLE_MSGS.gridRoleColumnHeaderName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedOnFormatted", ROLE_MSGS.gridRoleColumnHeaderModifiedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedByName", ROLE_MSGS.gridRoleColumnHeaderModifiedBy(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtRoleQuery) filterQuery;
    }

}
