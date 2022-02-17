/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client.cors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.CreatedByNameCellRenderer;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;
import org.eclipse.kapua.service.endpoint.EndpointInfo;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CorsGrid extends EntityGrid<GwtEndpoint> {

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);
    private static final ConsoleMessages C_MSGS = GWT.create(ConsoleMessages.class);
    private static final String ENDPOINT = "endpoint";

    protected static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    protected GwtEndpointQuery query;
    private CorsToolbarGrid toolbar;

    protected CorsGrid(GwtSession currentSession, CorsView corsView) {
        super(corsView, currentSession);
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("CorsGrid");
        logger.log(java.util.logging.Level.INFO, corsView.getClass().getName());
        query = new GwtEndpointQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    public String getEmptyGridText() {
        return C_MSGS.gridNoResultFound(ENDPOINT);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return C_MSGS.specificPagingToolbarShowingPost(ENDPOINT);
            }

            @Override
            public String pagingToolbarNoResult() {
                return C_MSGS.specificPagingToolbarNoResult(ENDPOINT);
            }
        };
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtEndpoint>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtEndpoint>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtEndpoint>> callback) {
                GWT_ENDPOINT_SERVICE.query((PagingLoadConfig) loadConfig, query, EndpointInfo.ENDPOINT_TYPE_CORS, callback);

            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridEndpointColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfig.setSortable(false);
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
        columnConfig.setRenderer(new CreatedByNameCellRenderer<GwtEndpoint>());
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
    protected CorsToolbarGrid getToolbar() {
        if (toolbar == null) {
            toolbar = new CorsToolbarGrid(currentSession);
        }
        return toolbar;
    }

}
