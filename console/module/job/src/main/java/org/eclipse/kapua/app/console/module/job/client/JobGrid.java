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
package org.eclipse.kapua.app.console.module.job.client;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobQuery;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobGrid extends EntityGrid<GwtJob> {

    private static final ConsoleJobMessages MSGS = GWT.create(ConsoleJobMessages.class);

    private static final GwtJobServiceAsync GWT_JOB_SERVICE = GWT.create(GwtJobService.class);

    private GwtJobQuery query;

    private JobGridToolbar toolbar;

    public JobGrid(AbstractEntityView<GwtJob> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtJobQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtJob>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtJob>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtJob>> callback) {
                GWT_JOB_SERVICE.query((PagingLoadConfig) loadConfig,
                        query,
                        callback);
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtJob selectedItem) {
        super.selectionChangedEvent(selectedItem);

        getToolbar().getAddEntityButton().setEnabled(currentSession.hasPermission(JobSessionPermission.write()));
        getToolbar().getEditEntityButton().setEnabled(selectedItem != null && currentSession.hasPermission(JobSessionPermission.write()));
        getToolbar().getDeleteEntityButton().setEnabled(selectedItem != null && currentSession.hasPermission(JobSessionPermission.delete()));

        ((JobGridToolbar) getToolbar()).getStartJobButton().setEnabled(selectedItem != null && currentSession.hasPermission(JobSessionPermission.execute()));
        ((JobGridToolbar) getToolbar()).getStopJobButton().setEnabled(selectedItem != null && currentSession.hasPermission(JobSessionPermission.execute()));
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        //        ColumnConfig columnConfig = new ColumnConfig("status", MSGS.gridJobColumnHeaderStatus(), 50);
        //        columnConfigs.add(columnConfig);

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridJobColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("jobName", MSGS.gridJobColumnHeaderName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("description", MSGS.gridJobColumnHeaderDescription(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("userName", MSGS.gridJobColumnHeaderCreatedBy(), 200);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridJobColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtJobQuery) filterQuery;
    }

    @Override
    protected EntityCRUDToolbar<GwtJob> getToolbar() {
        if (toolbar == null) {
            toolbar = new JobGridToolbar(currentSession);
        }
        return toolbar;
    }
}
