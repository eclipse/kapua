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
package org.eclipse.kapua.app.console.client.job;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.shared.model.job.GwtJobQuery;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.shared.service.GwtJobServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobGrid extends EntityGrid<GwtJob> {

    private static final GwtJobServiceAsync GWT_JOB_SERVICE = GWT.create(GwtJobService.class);
    private static final ConsoleJobMessages MSGS = GWT.create(ConsoleJobMessages.class);

    private GwtJobQuery query;

    private JobGridToolbar toolbar;

    public JobGrid(EntityView<GwtJob> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtJobQuery();
        query.setScopeId(currentSession.getSelectedAccount().getId());
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
        if (selectedItem != null) {
            if (currentSession.hasJobUpdatePermission()) {
                getToolbar().getEditEntityButton().setEnabled(true);
            }
            if (currentSession.hasJobDeletePermission()) {
                getToolbar().getDeleteEntityButton().setEnabled(true);
            }
            ((JobGridToolbar)getToolbar()).getStartJobButton().setEnabled(true);
        } else {
            getToolbar().getEditEntityButton().setEnabled(false);
            getToolbar().getDeleteEntityButton().setEnabled(false);
            ((JobGridToolbar)getToolbar()).getStartJobButton().setEnabled(false);
        }
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", MSGS.gridJobColumnHeaderStatus(), 50);
//        GridCellRenderer<GwtUser> setStatusIcon = new GridCellRenderer<GwtUser>() {
//
//            public String render(GwtUser gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtUser> deviceList, Grid<GwtUser> grid) {
//
//                KapuaIcon icon;
//                if (gwtUser.getStatusEnum() != null) {
//                    switch (gwtUser.getStatusEnum()) {
//                    case DISABLED:
//                        icon = new KapuaIcon(IconSet.USER);
//                        icon.setColor(Color.RED);
//                        break;
//                    case ENABLED:
//                        icon = new KapuaIcon(IconSet.USER);
//                        icon.setColor(Color.GREEN);
//                        break;
//                    default:
//                        icon = new KapuaIcon(IconSet.USER);
//                        icon.setColor(Color.GREY);
//                        break;
//                    }
//                } else {
//                    icon = new KapuaIcon(IconSet.USER);
//                    icon.setColor(Color.GREY);
//                }
//
//                return icon.getInlineHTML();
//            }
//        };
//        columnConfig.setRenderer(setStatusIcon);
//        columnConfig.setAlignment(HorizontalAlignment.CENTER);
//        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("id", MSGS.gridJobColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("jobName", MSGS.gridJobColumnHeaderName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("description", MSGS.gridJobColumnHeaderDescription(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdBy", MSGS.gridJobColumnHeaderCreatedBy(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOn", MSGS.gridJobColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtJobQuery)filterQuery;
    }

    @Override
    protected EntityCRUDToolbar<GwtJob> getToolbar() {
        if (toolbar == null) {
            toolbar = new JobGridToolbar(currentSession);
        }
        return toolbar;
    }
}
