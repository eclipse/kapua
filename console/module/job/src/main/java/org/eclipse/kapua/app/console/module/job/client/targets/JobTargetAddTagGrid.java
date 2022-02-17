/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.job.client.targets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGridCheckBoxSelectionModel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

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

public class JobTargetAddTagGrid extends EntityGrid<GwtTag> {

    private static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);
    private static final ConsoleTagMessages TAG_MSGS = GWT.create(ConsoleTagMessages.class);

    private final EntityGridCheckBoxSelectionModel<GwtTag> selectionModel = new EntityGridCheckBoxSelectionModel<GwtTag>();

    private static final int ENTITY_PAGE_SIZE = 10;

    private final GwtJob gwtSelectedJob;
    private GwtTagQuery query;

    public JobTargetAddTagGrid(GwtSession currentSession, GwtJob gwtSelectedJob) {
        super(null, currentSession);

        this.gwtSelectedJob = gwtSelectedJob;

        query = new GwtTagQuery();
        query.setScopeId(currentSession.getSelectedAccountId());

        // Configure grid options
        selectionMode = Style.SelectionMode.SIMPLE;
        keepSelectedItemsAfterLoad = false;
    }

    @Override
    public CheckBoxSelectionModel<GwtTag> getSelectionModel() {
        return selectionModel;
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtTag>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtTag>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtTag>> callback) {
                GWT_TAG_SERVICE.query((PagingLoadConfig) loadConfig, query, callback);
            }

        };
    }

    @Override
    protected PagingToolBar getPagingToolbar() {
        return new KapuaPagingToolBar(ENTITY_PAGE_SIZE);
    }

    @Override
    protected EntityCRUDToolbar<GwtTag> getToolbar() {
        EntityCRUDToolbar<GwtTag> toolbar = super.getToolbar();
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

        column = new ColumnConfig("tagName", TAG_MSGS.gridTagColumnHeaderTagName(), 175);
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
        this.query = (GwtTagQuery) filterQuery;
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
