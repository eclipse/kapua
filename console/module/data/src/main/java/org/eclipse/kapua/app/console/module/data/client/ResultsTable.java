/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.DateRangeSelector;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.DateRangeSelectorListener;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.client.util.GwtMessage;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultsTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);
    private static final ConsoleMessages C_MSGS = GWT.create(ConsoleMessages.class);
    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);
    private static final int RESULTSTABLE_PAGE_SIZE = 250;
    private static final String RESULT = "result";

    GwtSession currentSession;
    private ContentPanel tableContainer;
    private BasePagingLoader<PagingLoadResult<GwtMessage>> loader;
    private Grid<GwtMessage> resultsGrid;
    private KapuaPagingToolBar pagingToolBar;
    private ToolBar resultsToolBar;
    private Label warningLabel;
    private ArrayList<ColumnConfig> columnConfigs;
    private ColumnConfig timestampColumn;
    private ColumnConfig deviceColumn;
    private ColumnConfig topicColumn;
    private ColumnConfig channelColumn;
    private GwtTopic selectedTopic;
    private GwtDatastoreDevice selectedDevice;
    private GwtDatastoreAsset selectedAsset;
    private List<GwtHeader> selectedChannels;
    private List<GwtHeader> selectedMetrics;
    private Date startDate;
    private Date endDate;
    private KapuaButton exportButton;
    private DateRangeSelector dateRangeSelector;
    private KapuaButton queryButton;
    private List<GwtHeader> metrics;

    public ResultsTable(GwtSession currentSession, KapuaButton queryButton) {
        this.currentSession = currentSession;
        this.queryButton = queryButton;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        initResultsTable();
        add(tableContainer);

        queryButton.disable();
        dateRangeSelector.disable();
    }

    private void initResultsTable() {
        initResultsGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(false);
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(resultsGrid);
        tableContainer.setTopComponent(resultsToolBar);
        tableContainer.setBottomComponent(pagingToolBar);
    }

    private void initResultsGrid() {
        columnConfigs = new ArrayList<ColumnConfig>();

        timestampColumn = new ColumnConfig("timestampFormatted", MSGS.resultsTableTimestampHeader(), 140);
        deviceColumn = new ColumnConfig("clientId", MSGS.resultsTableDeviceHeader(), 90);
        topicColumn = new ColumnConfig("channel", MSGS.resultsTableTopicHeader(), 140);

        RpcProxy<PagingLoadResult<GwtMessage>> proxy = new RpcProxy<PagingLoadResult<GwtMessage>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtMessage>> callback) {
                if (selectedMetrics != null && !selectedMetrics.isEmpty()) {
                    if (selectedTopic != null) {
                        dataService.findMessagesByTopic((PagingLoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedTopic, selectedMetrics, startDate, endDate, callback);
                    } else if (selectedDevice != null) {
                        dataService.findMessagesByDevice((PagingLoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedDevice, selectedMetrics, startDate, endDate, callback);
                    }
                } else if (selectedDevice != null && selectedAsset != null && selectedChannels != null && !selectedChannels.isEmpty()) {
                    dataService.findMessagesByAssets((PagingLoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedDevice, selectedAsset, selectedChannels, startDate, endDate, callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtMessage>(new ArrayList<GwtMessage>()));
                }
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<GwtMessage>>(proxy);
        loader.addListener(Loader.Load, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (loader.getTotalCount() == 0) {
                    exportButton.disable();
                } else {
                    exportButton.enable();
                }

                if (loader.getTotalCount() >= 10000) {
                    warningLabel.show();
                } else {
                    warningLabel.hide();
                }
            }
        });
        loader.addListener(Loader.BeforeLoad, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent baseEvent) {
                if (queryButton != null) {
                    queryButton.disable();
                }
                pagingToolBar.enable();
            }
        });
        loader.addListener(Loader.Load, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent baseEvent) {
                if (queryButton != null && !metrics.isEmpty()) {
                    queryButton.enable();
                }
                pagingToolBar.enable();
            }
        });
        loader.addListener(Loader.LoadException, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent baseEvent) {
                if (queryButton != null) {
                    queryButton.enable();
                }
                pagingToolBar.enable();
            }
        });

        loader.setSortField("timestampFormatted");
        loader.setSortDir(SortDir.DESC);
        loader.setRemoteSort(true);

        SwappableListStore<GwtMessage> store = new SwappableListStore<GwtMessage>(loader);
        store.setStoreSorter(new StoreSorter<GwtMessage>());
        resultsGrid = new Grid<GwtMessage>(store, new ColumnModel(columnConfigs));
        resultsGrid.setBorders(false);
        resultsGrid.setStateful(false);
        resultsGrid.setLoadMask(true);
        resultsGrid.setStripeRows(true);
        resultsGrid.getView().setAutoFill(true);
        resultsGrid.getView().setForceFit(true);
        resultsGrid.getView().setEmptyText(C_MSGS.specificPagingToolbarNoResult(RESULT));
        resultsGrid.disableTextSelection(false);

        resultsToolBar = new ToolBar();

        exportButton = new KapuaButton(MSGS.resultsTableExportToCSV(), new KapuaIcon(IconSet.FILE_TEXT_O),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        export("csv");
                    }
                });

        exportButton.disable();
        resultsToolBar.add(exportButton);
        resultsToolBar.add(new SeparatorToolItem());

        warningLabel = new Label();
        warningLabel.setText(MSGS.warningLimitReached());
        warningLabel.setStyleAttribute("margin-left", "10px");
        warningLabel.setStyleAttribute("font-weight", "bold");
        warningLabel.setStyleAttribute("color", "grey");
        warningLabel.hide();
        resultsToolBar.add(warningLabel);
        resultsToolBar.add(new FillToolItem());

        Label label = new Label(MSGS.dateRange());
        Label emptySpace = new Label("&nbsp;&nbsp;&nbsp;");
        resultsToolBar.add(label);
        resultsToolBar.add(emptySpace);

        dateRangeSelector = new DateRangeSelector();
        dateRangeSelector.setListener(new DateRangeSelectorListener() {

            @Override
            public void onUpdate() {
                refresh(selectedMetrics);
            }
        });
        resultsToolBar.add(dateRangeSelector);

        pagingToolBar = new KapuaPagingToolBar(RESULTSTABLE_PAGE_SIZE);
        pagingToolBar.setKapuaPagingToolbarMessages(getKapuaPagingToolbarMessages());
        pagingToolBar.bind(loader);
        pagingToolBar.disable();
    }

    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return C_MSGS.specificPagingToolbarShowingPost(RESULT);
            }

            @Override
            public String pagingToolbarNoResult() {
                return C_MSGS.specificPagingToolbarNoResult(RESULT);
            }
        };
    }

    public void refresh() {
        resultsGrid.getStore().removeAll();
    }

    public void refresh(List<GwtHeader> headers) {
        resultsGrid.getColumnModel().getColumns().clear();
        resultsGrid.getColumnModel().getColumns().add(timestampColumn);
        selectedMetrics = headers;
        if (headers != null && !headers.isEmpty()) {
            resultsGrid.getColumnModel().getColumns().add(deviceColumn);
            resultsGrid.getColumnModel().getColumns().add(topicColumn);
            for (GwtHeader header : headers) {
                ColumnConfig headerColumn = new ColumnConfig(header.getName(), header.getName(), 100);
                headerColumn.setSortable(false);
                resultsGrid.getColumnModel().getColumns().add(headerColumn);
            }
            resultsGrid.getView().refresh(true);
            startDate = dateRangeSelector.getStartDate();
            endDate = dateRangeSelector.getEndDate();
        }
        loader.load();
    }

    public void refresh(GwtTopic topic, List<GwtHeader> headers) {
        this.selectedTopic = topic;
        refresh(headers);
    }

    public void refresh(GwtDatastoreDevice device, List<GwtHeader> headers) {
        this.selectedDevice = device;
        refresh(headers);
    }

    public void refresh(GwtDatastoreDevice device, GwtDatastoreAsset asset, List<GwtHeader> channels) {
        this.selectedAsset = asset;
        this.selectedDevice = device;
        refresh(channels);
    }

    private void export(String format) {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append("exporter_data?");
        sbUrl.append("format=")
                .append(format).append("&scopeIdString=")
                .append(URL.encodeQueryString(currentSession.getSelectedAccountId()));

        if (selectedTopic != null) {
            sbUrl.append("&topic=").append(URL.encodeQueryString(selectedTopic.getSemanticTopic()));
        }

        if (selectedDevice != null) {
            sbUrl.append("&device=").append(URL.encodeQueryString(selectedDevice.getDevice()));
        }

        if (selectedAsset != null) {
            sbUrl.append("&asset=").append(URL.encodeQueryString(selectedAsset.getTopick()));
        }

        if (selectedChannels != null && !selectedChannels.isEmpty()) {
            for (GwtHeader channel : selectedChannels) {
                sbUrl.append("&channels=").append(channel.getName());
            }
        }

        if (selectedMetrics != null && !selectedMetrics.isEmpty()) {
            for (GwtHeader metric : selectedMetrics) {
                sbUrl.append("&headers=").append(metric.getName());
            }
        }

        if (startDate != null) {
            sbUrl.append("&startDate=").append(startDate.getTime());
        }

        if (endDate != null) {
            sbUrl.append("&endDate=").append(endDate.getTime());
        }

        Window.open(sbUrl.toString(), "_blank", "location=no");
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------
    @Override
    public void onUnload() {
        super.onUnload();
    }

    public void clearTable() {
        resultsGrid.getStore().removeAll();
    }

    public List<GwtHeader> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<GwtHeader> metrics) {
        this.metrics = metrics;
    }

    public DateRangeSelector getDateRangeSelector() {
        return dateRangeSelector;
    }
}
