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
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobTargetAddGrid extends EntityGrid<GwtDevice> {

    private final String jobId;
    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);
    private static final ConsoleDeviceMessages DVC_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final CheckBoxSelectionModel<GwtDevice> selectionModel = new CheckBoxSelectionModel<GwtDevice>();

    public JobTargetAddGrid(GwtSession currentSession, String jobId) {
        super(null, currentSession);
        this.jobId = jobId;
        this.setPagingToolbar(new KapuaPagingToolBar(5));
    }

    @Override
    public CheckBoxSelectionModel<GwtDevice> getSelectionModel() {
        return selectionModel;
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtDevice>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtDevice>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDevice>> callback) {
                GWT_DEVICE_SERVICE.query((PagingLoadConfig) loadConfig,
                        new GwtDeviceQuery(currentSession.getSelectedAccountId()),
                        callback);
            }

        };
    }

    @Override
    protected EntityCRUDToolbar<GwtDevice> getToolbar() {
        EntityCRUDToolbar<GwtDevice> toolbar = super.getToolbar();
        toolbar.setAddButtonVisible(false);
        toolbar.setEditButtonVisible(false);
        toolbar.setDeleteButtonVisible(false);
        toolbar.setRefreshButtonVisible(true);
        return toolbar;
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        columnConfigs.add(selectionModel.getColumn());

        ColumnConfig column = new ColumnConfig("clientId", DVC_MSGS.deviceTableClientID(), 175);
        column.setSortable(true);
        columnConfigs.add(column);

        column = new ColumnConfig("displayName", DVC_MSGS.deviceTableDisplayName(), 150);
        column.setSortable(true);
        columnConfigs.add(column);

        return columnConfigs;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        entityGrid.addPlugin(selectionModel);
        entityGrid.setSelectionModel(selectionModel);
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return null;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {

    }

}
