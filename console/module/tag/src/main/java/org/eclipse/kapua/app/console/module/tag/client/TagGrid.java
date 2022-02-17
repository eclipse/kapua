/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.tag.client;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.CreatedByNameCellRenderer;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;
import org.eclipse.kapua.app.console.module.tag.shared.model.permission.TagSessionPermission;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class TagGrid extends EntityGrid<GwtTag> {

    private static final ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);
    private static final ConsoleMessages C_MSGS = GWT.create(ConsoleMessages.class);
    private static final String TAG = "tag";

    private TagToolbarGrid toolbar;

    protected static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);
    protected GwtTagQuery query;

    protected TagGrid(AbstractEntityView<GwtTag> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtTagQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected void selectionChangedEvent(GwtTag selectedItem) {
        super.selectionChangedEvent(selectedItem);
        if (getToolbar().getEditEntityButton() != null && getToolbar().getAddEntityButton() != null && getToolbar().getDeleteEntityButton() != null) {
            getToolbar().getAddEntityButton().setEnabled(currentSession.hasPermission(TagSessionPermission.write()));
            if (selectedItem != null) {
                getToolbar().getEditEntityButton().setEnabled(currentSession.hasPermission(TagSessionPermission.write()));
                getToolbar().getDeleteEntityButton().setEnabled(currentSession.hasPermission(TagSessionPermission.delete()));
            } else {
                getToolbar().getEditEntityButton().setEnabled(false);
                getToolbar().getDeleteEntityButton().setEnabled(false);
            }
        }
    }

    @Override
    public String getEmptyGridText() {
        return C_MSGS.gridNoResultFound(TAG);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return C_MSGS.specificPagingToolbarShowingPost(TAG);
            }

            @Override
            public String pagingToolbarNoResult() {
                return C_MSGS.specificPagingToolbarNoResult(TAG);
            }
        };
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtTag>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtTag>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtTag>> callback) {
                GWT_TAG_SERVICE.query((PagingLoadConfig) loadConfig, query, callback);

            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridTagColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("tagName", MSGS.gridTagColumnHeaderTagName(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("description", MSGS.gridTagColumnHeaderTagDescription(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridTagColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", MSGS.gridTagColumnHeaderCreatedBy(), 200);
        columnConfig.setRenderer(new CreatedByNameCellRenderer<GwtTag>());
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtTagQuery) filterQuery;
    }

    @Override
    protected TagToolbarGrid getToolbar() {
        if (toolbar == null) {
            toolbar = new TagToolbarGrid(currentSession);
        }
        return toolbar;
    }

}
