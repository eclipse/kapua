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

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecution;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecutionQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobExecutionService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobExecutionServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobTabExecutionsGrid extends EntityGrid<GwtJobExecution> {

    private static final GwtJobExecutionServiceAsync EXECUTION_SERVICE = GWT.create(GwtJobExecutionService.class);
    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final ConsoleMessages C_MSGS = GWT.create(ConsoleMessages.class);
    private static final String EXECUTION = "execution";

    private String jobId;
    private GwtJobExecutionQuery query;

    private JobTabExecutionsToolbar toolbar;

    public JobTabExecutionsGrid(AbstractEntityView<GwtJobExecution> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtJobExecution>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtJobExecution>>() {

            @Override
            protected void load(Object o, AsyncCallback<PagingLoadResult<GwtJobExecution>> asyncCallback) {
                if (jobId != null) {
                    EXECUTION_SERVICE.findByJobId((PagingLoadConfig) o, currentSession.getSelectedAccountId(), jobId, asyncCallback);
                } else {
                    asyncCallback.onSuccess(new BasePagingLoadResult<GwtJobExecution>(new ArrayList<GwtJobExecution>()));
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", JOB_MSGS.gridJobExecutionColumnHeaderStatus(), 30);
        columnConfig.setSortable(false);
        columnConfig.setRenderer(new GridCellRenderer<GwtJobExecution>() {

            @Override
            public Object render(GwtJobExecution gwtJobExecution, String s, ColumnData columnData, int i, int i1, ListStore listStore, Grid grid) {
                KapuaIcon icon;

                if (gwtJobExecution.getEndedOn() == null) {
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
        this.query = (GwtJobExecutionQuery) filterQuery;
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
    protected void selectionChangedEvent(GwtJobExecution selectedItem) {
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

    @Override
    public String getEmptyGridText() {
        return C_MSGS.gridNoResultAvailable(EXECUTION);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return C_MSGS.specificPagingToolbarShowingPost(EXECUTION);
            }

            @Override
            public String pagingToolbarNoResult() {
                return C_MSGS.specificPagingToolbarNoResult(EXECUTION);
            }
        };
    }
}
