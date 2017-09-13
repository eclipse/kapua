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
package org.eclipse.kapua.app.console.module.job.client.targets;

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
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTargetQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobTabTargetsGrid extends EntityGrid<GwtJobTarget> {

    private GwtJobTargetQuery query;

    private static final ConsoleJobMessages MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobTargetServiceAsync GWT_JOB_TARGET_SERVICE = GWT.create(GwtJobTargetService.class);
    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    private JobTabTargetsToolbar toolbar;

    private String jobId;

    protected JobTabTargetsGrid(AbstractEntityView<GwtJobTarget> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtJobTarget>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtJobTarget>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtJobTarget>> callback) {
                GWT_JOB_TARGET_SERVICE.findByJobId((PagingLoadConfig) loadConfig,
                        currentSession.getSelectedAccountId(),
                        jobId,
                        callback);
            }

        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("jobTargetId", MSGS.gridJobTargetColumnHeaderJobTargetId(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("stepIndex", MSGS.gridJobTargetColumnHeaderJobStepIndex(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("status", MSGS.gridJobTargetColumnHeaderStatus(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("errorMessage", MSGS.gridJobTargetColumnHeaderErrorMessage(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtJobTargetQuery) filterQuery;
    }

    public Object getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    protected JobTabTargetsToolbar getToolbar() {
        if (toolbar == null) {
            toolbar = new JobTabTargetsToolbar(currentSession);
            toolbar.setEditButtonVisible(false);
        }
        return toolbar;
    }

    @Override
    protected void selectionChangedEvent(final GwtJobTarget selectedItem) {
        super.selectionChangedEvent(selectedItem);
        JOB_SERVICE.find(currentSession.getSelectedAccountId(), jobId, new AsyncCallback<GwtJob>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(GwtJob result) {
                if (result.getJobXmlDefinition() == null) {
                    JobTabTargetsGrid.this.toolbar.getAddEntityButton().enable();
                    if (selectedItem == null) {
                        JobTabTargetsGrid.this.toolbar.getEditEntityButton().disable();
                        JobTabTargetsGrid.this.toolbar.getDeleteEntityButton().disable();
                    } else {
                        JobTabTargetsGrid.this.toolbar.getEditEntityButton().enable();
                        JobTabTargetsGrid.this.toolbar.getDeleteEntityButton().enable();
                    }
                } else {
                    JobTabTargetsGrid.this.toolbar.getAddEntityButton().disable();
                    JobTabTargetsGrid.this.toolbar.getEditEntityButton().disable();
                    JobTabTargetsGrid.this.toolbar.getDeleteEntityButton().disable();
                }
            }
        });
    }

}
