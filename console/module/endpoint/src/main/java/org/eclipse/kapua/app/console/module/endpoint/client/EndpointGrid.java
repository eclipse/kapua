/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.client;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class EndpointGrid extends EntityGrid<GwtEndpoint> {

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    protected static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    protected GwtEndpointQuery query;

    protected EndpointGrid(AbstractEntityView<GwtEndpoint> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtEndpointQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtEndpoint>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtEndpoint>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtEndpoint>> callback) {
                GWT_ENDPOINT_SERVICE.query((PagingLoadConfig) loadConfig, query, callback);

            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridEndpointColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("schema", MSGS.gridEndpointColumnHeaderEndpointSchema(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("dns", MSGS.gridEndpointColumnHeaderEndpointDns(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("port", MSGS.gridEndpointColumnHeaderEndpointPort(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("secure", MSGS.gridEndpointColumnHeaderEndpointSecure(), 150);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridEndpointColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", MSGS.gridEndpointColumnHeaderCreatedBy(), 200);
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
        this.query = (GwtEndpointQuery) filterQuery;
    }

    @Override
    protected EndpointToolbarGrid getToolbar() {
        return new EndpointToolbarGrid(currentSession);
    }

}
