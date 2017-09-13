/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.client.execution;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtExecution;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtExecutionQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtExecutionService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtExecutionServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobTabExecutionsGrid extends EntityGrid<GwtExecution> {

    private static final GwtExecutionServiceAsync EXECUTION_SERVICE = GWT.create(GwtExecutionService.class);
    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private String jobId;
    private GwtExecutionQuery query;

    private JobTabExecutionsToolbar toolbar;

    public JobTabExecutionsGrid(AbstractEntityView<GwtExecution> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtExecution>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtExecution>>() {

            @Override
            protected void load(Object o, AsyncCallback<PagingLoadResult<GwtExecution>> asyncCallback) {
                EXECUTION_SERVICE.findByJobId((PagingLoadConfig)o, currentSession.getSelectedAccountId(), jobId, asyncCallback);
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("startedOn", JOB_MSGS.gridJobExecutionsColumnHeaderStartedOnFormatted(), 200);
        columnConfig.setSortable(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("endedOn", JOB_MSGS.gridJobExecutionsColumnHeaderEndedOnFormatted(), 200);
        columnConfig.setSortable(true);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtExecutionQuery) filterQuery;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    protected JobTabExecutionsToolbar getToolbar() {
        if (toolbar == null) {
            toolbar = new JobTabExecutionsToolbar(currentSession);
            toolbar.setAddButtonVisible(false);
            toolbar.setEditButtonVisible(false);
            toolbar.setDeleteButtonVisible(false);
        }
        return toolbar;
    }

    @Override
    protected void selectionChangedEvent(final GwtExecution selectedItem) {
        super.selectionChangedEvent(selectedItem);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        entityLoader.setRemoteSort(true);
    }
}
