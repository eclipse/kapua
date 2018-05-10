/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class RoleGrid extends EntityGrid<GwtRole> {

    private static final ConsoleRoleMessages ROLE_MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync GWT_ROLE_SERVICE = GWT.create(GwtRoleService.class);

    private GwtRoleQuery query;
    private RoleToolbarGrid toolbar;

    public RoleGrid(AbstractEntityView<GwtRole> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtRoleQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected void selectionChangedEvent(GwtRole selectedItem) {
        super.selectionChangedEvent(selectedItem);
        getToolbar().getAddEntityButton().setEnabled(currentSession.hasPermission(RoleSessionPermission.write()));
        if (selectedItem != null) {
            getToolbar().getEditEntityButton().setEnabled(currentSession.hasPermission(RoleSessionPermission.write()));
            getToolbar().getDeleteEntityButton().setEnabled(currentSession.hasPermission(RoleSessionPermission.delete()));
        } else {
            getToolbar().getEditEntityButton().setEnabled(false);
            getToolbar().getDeleteEntityButton().setEnabled(false);
        }
    }

    @Override
    protected RoleToolbarGrid getToolbar() {
        if (toolbar == null) {
            toolbar = new RoleToolbarGrid(currentSession);
        }
        return toolbar;
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
