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

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeGridEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class TopicsTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private GwtSession currentSession;
    private TreeGrid<GwtTopic> topicInfoGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtTopic>> listeners = new ArrayList<SelectionChangedListener<GwtTopic>>();
    private TreeStore<GwtTopic> store;

    AsyncCallback<List<GwtTopic>> topicsCallback;

    public TopicsTable(GwtSession currentGwtSession) {
        this.currentSession = currentGwtSession;
        topicsCallback = new AsyncCallback<List<GwtTopic>>() {

            @Override
            public void onSuccess(List<GwtTopic> topics) {
                store.add(topics, true);
                store.sort("topicName", Style.SortDir.ASC);
                updateTimestamps(new ArrayList<ModelData>(topics));
                topicInfoGrid.unmask();
            }

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
                topicInfoGrid.unmask();
            }
        };
    }

    public GwtTopic getSelectedTopic() {
        if (topicInfoGrid != null) {
            return topicInfoGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    public void addSelectionChangedListener(SelectionChangedListener<GwtTopic> listener) {
        listeners.add(listener);
    }

    public void refresh() {
        topicInfoGrid.getSelectionModel().deselect(getSelectedTopic());
        clearTable();
        topicInfoGrid.mask(GXT.MESSAGES.loadMask_msg());
        dataService.findTopicsTree(currentSession.getSelectedAccountId(), topicsCallback);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        initTopicInfoTable();
        add(tableContainer);

        topicInfoGrid.mask(GXT.MESSAGES.loadMask_msg());
    }

    private void initTopicInfoTable() {
        initTopicInfoGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(true);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.topicInfoTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(topicInfoGrid);

        Button refreshButton = new Button(MSGS.refresh(), new KapuaIcon(IconSet.REFRESH), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                refresh();
            }
        });

        ToolBar tb = new ToolBar();
        tb.add(refreshButton);
        tableContainer.setTopComponent(tb);
    }

    private void initTopicInfoGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("topicName", MSGS.topicInfoTableTopicHeader(), 150);
        column.setRenderer(new TreeGridCellRenderer<GwtTopic>());
        configs.add(column);

        column = new ColumnConfig("timestamp", MSGS.topicInfoTableLastPostedHeader(), 150);
        column.setRenderer(new TopicTimestampCellRenderer(currentSession));
        configs.add(column);

        store = new TreeStore<GwtTopic>();
        store.setSortInfo(new SortInfo("topicName", Style.SortDir.ASC));
        dataService.findTopicsTree(currentSession.getSelectedAccountId(), topicsCallback);
        topicInfoGrid = new TreeGrid<GwtTopic>(store, new ColumnModel(configs));
        topicInfoGrid.setBorders(false);
        topicInfoGrid.setStateful(false);
        topicInfoGrid.setLoadMask(true);
        topicInfoGrid.setStripeRows(true);
        topicInfoGrid.getView().setAutoFill(true);
        topicInfoGrid.getView().setForceFit(true);
        topicInfoGrid.getView().setEmptyText(MSGS.topicInfoGridEmptyText());
        topicInfoGrid.disableTextSelection(false);

        GridSelectionModel<GwtTopic> selectionModel = new GridSelectionModel<GwtTopic>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        for (SelectionChangedListener<GwtTopic> listener : listeners) {
            selectionModel.addSelectionChangedListener(listener);
        }
        topicInfoGrid.setSelectionModel(selectionModel);

        topicInfoGrid.addListener(Events.BeforeExpand, new Listener<TreeGridEvent<GwtTopic>>() {

            @Override
            public void handleEvent(final TreeGridEvent<GwtTopic> tge) {
                updateTimestamps(tge.getModel().getChildren());
            }
        });
    }

    @Override
    public void onUnload() {
        super.onUnload();
    }

    public void clearTable() {
        topicInfoGrid.getStore().removeAll();
        topicInfoGrid.getTreeStore().removeAll();
    }

    private void updateTimestamps(List<ModelData> topics) {
        dataService.updateTopicTimestamps(currentSession.getSelectedAccountId(), topics, new AsyncCallback<List<GwtTopic>>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(List<GwtTopic> result) {
                for (GwtTopic topic : result) {
                    store.findModel(topic).setTimestamp(topic.getTimestamp() != null ? topic.getTimestamp() : GwtTopic.NO_TIMESTAMP);
                }
                topicInfoGrid.getTreeView().refresh(false);
            }
        });
    }
}
