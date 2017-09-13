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
package org.eclipse.kapua.app.console.module.job.client.steps;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobStepServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobTabStepsGrid extends EntityGrid<GwtJobStep> {

    private GwtJobStepQuery query;

    private static final ConsoleJobMessages MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobStepServiceAsync JOB_STEP_SERVICE = GWT.create(GwtJobStepService.class);
    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    private JobTabStepsToolbar toolbar;

    private String jobId;

    protected JobTabStepsGrid(AbstractEntityView<GwtJobStep> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtJobStep>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtJobStep>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtJobStep>> callback) {
                    JOB_STEP_SERVICE.findByJobId((PagingLoadConfig) loadConfig,
                            currentSession.getSelectedAccountId(),
                            jobId,
                            callback);
                }

        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("stepIndex", MSGS.gridJobStepColumnHeaderStepIndex(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("jobStepName", MSGS.gridJobStepColumnHeaderJobName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("description", MSGS.gridJobStepColumnHeaderDescription(), 400);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtJobStepQuery) filterQuery;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    protected JobTabStepsToolbar getToolbar() {
        if (toolbar == null) {
            toolbar = new JobTabStepsToolbar(currentSession);
        }
        return toolbar;
    }

    @Override
    protected void selectionChangedEvent(final GwtJobStep selectedItem) {
        super.selectionChangedEvent(selectedItem);
        JOB_SERVICE.find(currentSession.getSelectedAccountId(), jobId, new AsyncCallback<GwtJob>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(GwtJob result) {
                if (result.getJobXmlDefinition() == null) {
                    JobTabStepsGrid.this.toolbar.getAddEntityButton().enable();
                    if (selectedItem == null) {
                        JobTabStepsGrid.this.toolbar.getEditEntityButton().disable();
                        JobTabStepsGrid.this.toolbar.getDeleteEntityButton().disable();
                    } else {
                        JobTabStepsGrid.this.toolbar.getEditEntityButton().enable();
                        JobTabStepsGrid.this.toolbar.getDeleteEntityButton().enable();
                    }
                } else {
                    JobTabStepsGrid.this.toolbar.getAddEntityButton().disable();
                    JobTabStepsGrid.this.toolbar.getEditEntityButton().disable();
                    JobTabStepsGrid.this.toolbar.getDeleteEntityButton().disable();
                }
            }
        });
    }

}
