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
package org.eclipse.kapua.app.console.module.job.client.targets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGridCheckBoxSelectionModel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;

import java.util.ArrayList;
import java.util.List;

public class JobTargetAddGrid extends EntityGrid<GwtDevice> {

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);
    private static final ConsoleDeviceMessages DVC_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final EntityGridCheckBoxSelectionModel<GwtDevice> selectionModel = new EntityGridCheckBoxSelectionModel<GwtDevice>();

    private static final int ENTITY_PAGE_SIZE = 10;

    private final GwtJob gwtSelectedJob;
    private GwtDeviceQuery query;

    public JobTargetAddGrid(GwtSession currentSession, GwtJob gwtSelectedJob) {
        super(null, currentSession);

        this.gwtSelectedJob = gwtSelectedJob;

        query = new GwtDeviceQuery();
        query.setScopeId(currentSession.getSelectedAccountId());

        // Configure grid options
        selectionMode = Style.SelectionMode.SIMPLE;
        keepSelectedItemsAfterLoad = false;
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
                GWT_DEVICE_SERVICE.query((PagingLoadConfig) loadConfig, query, callback);
            }

        };
    }

    @Override
    protected PagingToolBar getPagingToolbar() {
        return new KapuaPagingToolBar(ENTITY_PAGE_SIZE);
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

        ColumnConfig column = selectionModel.getColumn();
        columnConfigs.add(column);

        column = new ColumnConfig("clientId", DVC_MSGS.deviceTableClientID(), 175);
        column.setSortable(true);
        columnConfigs.add(column);
        column.setRenderer(gridCellRenderer);

        column = new ColumnConfig("displayName", DVC_MSGS.deviceTableDisplayName(), 150);
        column.setSortable(true);
        columnConfigs.add(column);
        column.setRenderer(gridCellRenderer);

        return columnConfigs;
    }

    @Override
    protected void onRender(Element target, int index) {
        configureEntityGrid();

        entityGrid.addPlugin(selectionModel);
        entityGrid.setSelectionModel(selectionModel);

        super.onRender(target, index);
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtDeviceQuery) filterQuery;
    }

    GridCellRenderer<ModelData> gridCellRenderer = new GridCellRenderer<ModelData>() {

        @Override
        public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex,
                ListStore<ModelData> store, Grid<ModelData> grid) {
            String value = model.get(KapuaSafeHtmlUtils.htmlUnescape(property));
            if (value != null) {
                return "<tpl for=\".\"><div title=" + value + ">" + value + "</div></tpl>";
            }
            return value;
        }
    };

}
