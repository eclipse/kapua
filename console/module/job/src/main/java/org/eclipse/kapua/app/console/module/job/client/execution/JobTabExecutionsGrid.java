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
package org.eclipse.kapua.app.console.module.job.client.execution;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtExecution;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtExecutionQuery;
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
                if (jobId != null) {
                    EXECUTION_SERVICE.findByJobId((PagingLoadConfig) o, currentSession.getSelectedAccountId(), jobId, asyncCallback);
                } else {
                    asyncCallback.onSuccess(new BasePagingLoadResult<GwtExecution>(new ArrayList<GwtExecution>()));
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", JOB_MSGS.gridJobExecutionColumnHeaderStatus(), 30);
        columnConfig.setSortable(false);
        columnConfig.setRenderer(new GridCellRenderer<GwtExecution>() {

            @Override
            public Object render(GwtExecution gwtExecution, String s, ColumnData columnData, int i, int i1, ListStore listStore, Grid grid) {
                KapuaIcon icon;

                if (gwtExecution.getEndedOn() == null) {
                    icon = new KapuaIcon(IconSet.CIRCLE_O_NOTCH);
                    icon.setSpin(true);
                } else {
                    icon = new KapuaIcon(IconSet.FLAG_CHECKERED);
                }

                return icon.getInlineHTML();
            }
        });
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("startedOnFormatted", JOB_MSGS.gridJobExecutionsColumnHeaderStartedOnFormatted(), 200);
        columnConfig.setSortable(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("endedOnFormatted", JOB_MSGS.gridJobExecutionsColumnHeaderEndedOnFormatted(), 200);
        columnConfig.setSortable(true);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
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
    protected void selectionChangedEvent(GwtExecution selectedItem) {
        super.selectionChangedEvent(selectedItem);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        entityLoader.setRemoteSort(true);
        entityLoader.setSortField("startedOn");
        entityLoader.setSortDir(SortDir.DESC);

        /* Despite this grid, being a "slave" grid (i.e. a grid that depends on the value
         * selected in another grid) and so not refreshed on render (see comment in
         * EntityGrid class), it should be refreshed anyway on render if no item is
         * selected on the master grid, otherwise the paging toolbar will still be enabled
         * even if no results are actually available in this grid */
        if (jobId == null) {
            refresh();
        }
    }
}
