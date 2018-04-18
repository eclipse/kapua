/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.group;

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
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.GroupSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class GroupGrid extends EntityGrid<GwtGroup> {

    private static final GwtGroupServiceAsync GWT_GROUP_SERVICE = GWT.create(GwtGroupService.class);
    private static final ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);
    private GwtGroupQuery query;
    private GroupToolbarGrid toolbar;

    protected GroupGrid(AbstractEntityView<GwtGroup> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtGroupQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected void selectionChangedEvent(GwtGroup selectedItem) {
        super.selectionChangedEvent(selectedItem);
        if (selectedItem != null) {
            getToolbar().getEditEntityButton().setEnabled(currentSession.hasPermission(GroupSessionPermission.write()));
            getToolbar().getAddEntityButton().setEnabled(currentSession.hasPermission(GroupSessionPermission.write()));
            getToolbar().getDeleteEntityButton().setEnabled(currentSession.hasPermission(GroupSessionPermission.delete()));
        } else {
            getToolbar().getEditEntityButton().setEnabled(false);
            getToolbar().getDeleteEntityButton().setEnabled(false);
        }
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtGroup>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtGroup>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtGroup>> callback) {
                GWT_GROUP_SERVICE.query((PagingLoadConfig) loadConfig, query, callback);

            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridGroupColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("groupName", MSGS.gridGroupColumnHeaderGroupName(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridGroupColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("userName", MSGS.gridGroupColumnHeaderCreatedBy(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtGroupQuery) filterQuery;

    }

    @Override
    protected GroupToolbarGrid getToolbar() {
        if (toolbar == null) {
            toolbar = new GroupToolbarGrid(currentSession);
        }
        return toolbar;
    }

}
