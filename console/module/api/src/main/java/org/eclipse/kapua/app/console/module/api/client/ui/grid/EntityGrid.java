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
package org.eclipse.kapua.app.console.module.api.client.ui.grid;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

import java.util.List;

public abstract class EntityGrid<M extends GwtEntityModel> extends ContentPanel {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private static final int ENTITY_PAGE_SIZE = 100;

    protected GwtSession currentSession;
    private AbstractEntityView<M> parentEntityView;

    private EntityCRUDToolbar<M> entityCRUDToolbar;
    protected KapuaGrid<M> entityGrid;
    protected BasePagingLoader<PagingLoadResult<M>> entityLoader;
    protected ListStore<M> entityStore;
    protected PagingToolBar entityPagingToolbar;
    protected EntityFilterPanel<M> filterPanel;
    protected boolean refreshOnRender = true;

    protected EntityGrid(AbstractEntityView<M> entityView, GwtSession currentSession) {
        super(new FitLayout());
        //
        // Set other properties
        this.parentEntityView = entityView;
        this.currentSession = currentSession;

        //
        // Container borders
        setBorders(false);
        setBodyBorder(true);
        setHeaderVisible(false);

        //
        // CRUD toolbar
        entityCRUDToolbar = getToolbar();
        if (entityCRUDToolbar != null) {
            setTopComponent(entityCRUDToolbar);
        }
        //
        // Paging toolbar
        entityPagingToolbar = getPagingToolbar();
        if (entityPagingToolbar != null) {
            setBottomComponent(entityPagingToolbar);
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        //
        // Configure Entity Grid

        // Data Proxy
        RpcProxy<PagingLoadResult<M>> dataProxy = getDataProxy();

        // Data Loader
        entityLoader = new BasePagingLoader<PagingLoadResult<M>>(dataProxy);

        // Data Store
        entityStore = new ListStore<M>(entityLoader);

        //
        // Grid Data Load Listener
        entityLoader.addLoadListener(new EntityGridLoadListener<M>(this, entityStore));

        //
        // Bind Entity Paging Toolbar
        if (entityPagingToolbar != null) {
            entityPagingToolbar.bind(entityLoader);
        }

        //
        // Configure columns
        ColumnModel columnModel = new ColumnModel(getColumns());

        //
        // Set grid
        entityGrid = new KapuaGrid<M>(entityStore, columnModel);
        add(entityGrid);

        // Force layout so the entityGrid gets rendered and its listeners initialized
        layout();

        //
        // Bind the grid to CRUD toolbar
        entityCRUDToolbar.setEntityGrid(this);

        //
        // Grid selection mode
        GridSelectionModel<M> selectionModel = entityGrid.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<M>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<M> se) {
                selectionChangedEvent(se.getSelectedItem());
            }
        });

        //
        // Grid view options
        GridView gridView = entityGrid.getView();
        gridView.setEmptyText(MSGS.gridEmptyResult());

        //
        // Do first load
        if (refreshOnRender) {
            refresh();
        }
    }

    protected EntityCRUDToolbar<M> getToolbar() {
        return new EntityCRUDToolbar<M>(currentSession);
    }

    protected abstract RpcProxy<PagingLoadResult<M>> getDataProxy();

    protected PagingToolBar getPagingToolbar() {
        return new KapuaPagingToolBar(ENTITY_PAGE_SIZE);
    }

    protected abstract List<ColumnConfig> getColumns();

    public void clearGridElement() {
        entityStore.removeAll();
    }

    public void refresh() {
        entityLoader.load();
    }

    public void refresh(GwtQuery query) {
        setFilterQuery(query);
        entityLoader.load();
    }

    public void setFilterPanel(EntityFilterPanel<M> filterPanel) {
        this.filterPanel = filterPanel;
        entityCRUDToolbar.setFilterPanel(filterPanel);
    }

    protected void selectionChangedEvent(M selectedItem) {
        if (parentEntityView != null) {
            parentEntityView.setSelectedEntity(selectedItem);
        }

        if (entityCRUDToolbar != null) {
            entityCRUDToolbar.setSelectedEntity(selectedItem);
        }
    }

    public void setPagingToolbar(PagingToolBar entityPagingToolbar) {
        this.entityPagingToolbar = entityPagingToolbar;
    }

    public GridSelectionModel<M> getSelectionModel() {
        return entityGrid.getSelectionModel();
    }

    public boolean isRefreshOnRender() {
        return refreshOnRender;
    }

    public void setRefreshOnRender(boolean refreshOnRender) {
        this.refreshOnRender = refreshOnRender;
    }

    public abstract GwtQuery getFilterQuery();

    public abstract void setFilterQuery(GwtQuery filterQuery);
}
