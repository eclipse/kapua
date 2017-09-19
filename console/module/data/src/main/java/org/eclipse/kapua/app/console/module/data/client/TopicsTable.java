/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

public class TopicsTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private GwtSession currentSession;
    private TreeGrid<GwtTopic> topicInfoGrid;
    private ContentPanel tableContainer;
    private PagingToolBar pagingToolBar;
    private List<SelectionChangedListener<GwtTopic>> listeners = new ArrayList<SelectionChangedListener<GwtTopic>>();
    private TreeStore<GwtTopic> store;

    public TopicsTable(GwtSession currentGwtSession) {
        this.currentSession = currentGwtSession;
    }

    public GwtTopic getSelectedTopic() {
        if (topicInfoGrid != null) {
            return (GwtTopic) topicInfoGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    public void addSelectionChangedListener(SelectionChangedListener<GwtTopic> listener) {
        listeners.add(listener);
    }

    public void refresh() {
        topicInfoGrid.getSelectionModel().deselect(getSelectedTopic());
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        inittopicInfoTable();
        add(tableContainer);
    }

    private void inittopicInfoTable() {
        inittopicInfoGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(true);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.topicInfoTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(topicInfoGrid);
        tableContainer.setBottomComponent(pagingToolBar);
    }

    private void inittopicInfoGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("topicName", MSGS.topicInfoTableTopicHeader(), 150);
        column.setRenderer(new TreeGridCellRenderer<GwtTopic>());
        configs.add(column);

        column = new ColumnConfig("timestamp", MSGS.topicInfoTableLastPostedHeader(), 150);
        configs.add(column);

        store = new TreeStore<GwtTopic>();
        AsyncCallback<List<GwtTopic>> topicsCallback = new AsyncCallback<List<GwtTopic>>() {

            @Override
            public void onSuccess(List<GwtTopic> topics) {
                store.add(topics, true);
                topicInfoGrid.unmask();
            }

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
                topicInfoGrid.unmask();
            }
        };
        dataService.findTopicsTree(currentSession.getSelectedAccountId(), topicsCallback);
        topicInfoGrid = new TreeGrid<GwtTopic>(store, new ColumnModel(configs));
        topicInfoGrid.setBorders(false);
        topicInfoGrid.setStateful(false);
        topicInfoGrid.setLoadMask(true);
        topicInfoGrid.mask("Loading");
        topicInfoGrid.setStripeRows(true);
        topicInfoGrid.getView().setAutoFill(true);
        topicInfoGrid.getView().setEmptyText(MSGS.topicInfoGridEmptyText());
        topicInfoGrid.disableTextSelection(false);

        GridSelectionModel<GwtTopic> selectionModel = new GridSelectionModel<GwtTopic>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        for (SelectionChangedListener<GwtTopic> listener : listeners) {
            selectionModel.addSelectionChangedListener(listener);
        }
        topicInfoGrid.setSelectionModel(selectionModel);
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------
    public void onUnload() {
        super.onUnload();
    }
}
