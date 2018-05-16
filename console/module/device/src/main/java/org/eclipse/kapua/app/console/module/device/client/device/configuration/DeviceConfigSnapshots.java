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
package org.eclipse.kapua.app.console.module.device.client.device.configuration;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.FileUploadDialog;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtSnapshot;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementServiceAsync;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceConfigSnapshots extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT.create(GwtDeviceManagementService.class);
    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    private static final String SERVLET_URL = "file/configuration/snapshot";

    private GwtSession currentSession;

    private boolean dirty;
    private boolean initialized;
    private GwtDevice selectedDevice;
    private DeviceTabConfiguration tabConfig;

    private ToolBar toolBar;

    private Button refreshButton;
    private boolean refreshProcess;

    private Button downloadButton;
    private Button rollbackButton;
    private Button uploadButton;

    private ListStore<GwtSnapshot> store;
    private Grid<GwtSnapshot> grid;
    private BaseListLoader<ListLoadResult<GwtSnapshot>> loader;
    private FileUploadDialog fileUpload;

    protected boolean downloadProcess;
    protected boolean uploadProcess;
    protected boolean rollbackProcess;

    public DeviceConfigSnapshots(GwtSession currentSession,
            DeviceTabConfiguration tabConfig) {
        this.currentSession = currentSession;
        this.tabConfig = tabConfig;
        dirty = false;
        initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        dirty = true;
        this.selectedDevice = selectedDevice;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        // init components
        initToolBar();
        initGrid();

        ContentPanel devicesHistoryPanel = new ContentPanel();
        devicesHistoryPanel.setBorders(false);
        devicesHistoryPanel.setBodyBorder(false);
        devicesHistoryPanel.setHeaderVisible(false);
        devicesHistoryPanel.setLayout(new FitLayout());
        devicesHistoryPanel.setScrollMode(Scroll.AUTO);
        devicesHistoryPanel.setTopComponent(toolBar);
        devicesHistoryPanel.add(grid);

        add(devicesHistoryPanel);
        layout(true);
        toolBar.setStyleAttribute("border-left", "0px none");
        toolBar.setStyleAttribute("border-right", "0px none");
        toolBar.setStyleAttribute("border-top", "0px none");
        toolBar.setStyleAttribute("border-bottom", "1px solid rgb(208, 208, 208)");
        initialized = true;
    }

    private void initToolBar() {
        toolBar = new ToolBar();
        toolBar.setEnabled(false);
        toolBar.setBorders(false);

        //
        // Refresh Button
        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshProcess = true;
                    refreshButton.setEnabled(false);

                    dirty = true;
                    reload();

                    refreshButton.setEnabled(true);
                    refreshProcess = false;
                }
            }
        });
        refreshButton.setEnabled(false);
        toolBar.add(refreshButton);
        toolBar.add(new SeparatorToolItem());

        downloadButton = new SnapshotDownloadButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!downloadProcess) {
                    downloadProcess = true;
                    downloadButton.setEnabled(false);

                    downloadSnapshot();

                    downloadButton.setEnabled(true);
                    downloadProcess = false;
                }
            }
        });
        downloadButton.setEnabled(false);

        uploadButton = new SnapshotUploadButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!uploadProcess) {
                    uploadProcess = true;
                    uploadButton.setEnabled(false);

                    uploadSnapshot();

                    uploadButton.setEnabled(true);
                    uploadProcess = false;
                }
            }
        });
        uploadButton.setEnabled(true);

        rollbackButton = new SnapshotRollbackButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!rollbackProcess) {
                    rollbackProcess = true;
                    rollbackButton.setEnabled(false);

                    rollbackSnapshot();

                    rollbackButton.setEnabled(true);
                    rollbackProcess = false;
                }
            }
        });
        rollbackButton.setEnabled(false);

        toolBar.add(downloadButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(rollbackButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(uploadButton);
    }

    private void initGrid() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("snapshotId", DEVICE_MSGS.deviceSnapshotId(), 25);
        column.setSortable(true);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("createdOnFormatted", DEVICE_MSGS.deviceSnapshotCreatedOn(), 75);
        column.setSortable(true);
        column.setAlignment(HorizontalAlignment.LEFT);
        columns.add(column);

        // loader and store
        RpcProxy<ListLoadResult<GwtSnapshot>> proxy = new RpcProxy<ListLoadResult<GwtSnapshot>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtSnapshot>> callback) {
                if (selectedDevice != null && dirty && initialized) {
                    if (selectedDevice.isOnline()) {
                        gwtDeviceManagementService.findDeviceSnapshots(selectedDevice,
                                callback);
                    } else {
                        ListLoadResult<GwtSnapshot> snapshotResults = new ListLoadResult<GwtSnapshot>() {

                            @Override
                            public List<GwtSnapshot> getData() {
                                List<GwtSnapshot> snapshots = new ArrayList<GwtSnapshot>();
                                return snapshots;
                            }
                        };

                        callback.onSuccess(snapshotResults);
                    }
                } else {
                    ListLoadResult<GwtSnapshot> snapshotResults = new ListLoadResult<GwtSnapshot>() {

                        @Override
                        public List<GwtSnapshot> getData() {
                            List<GwtSnapshot> snapshots = new ArrayList<GwtSnapshot>();
                            return snapshots;
                        }
                    };

                    callback.onSuccess(snapshotResults);
                }
                dirty = false;
            }
        };
        loader = new BaseListLoader<ListLoadResult<GwtSnapshot>>(proxy);
        loader.setSortDir(SortDir.DESC);
        loader.setSortField("createdOnFormatted");
        loader.addLoadListener(new DataLoadListener());

        store = new ListStore<GwtSnapshot>(loader);
        grid = new Grid<GwtSnapshot>(store, new ColumnModel(columns));
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.getView().setAutoFill(true);
        grid.getView().setEmptyText(DEVICE_MSGS.deviceSnapshotsNone());

        GridSelectionModel<GwtSnapshot> selectionModel = new GridSelectionModel<GwtSnapshot>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        grid.setSelectionModel(selectionModel);
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtSnapshot>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtSnapshot> se) {
                if (se.getSelectedItem() != null) {
                    downloadButton.setEnabled(true);
                    rollbackButton.setEnabled(true);
                } else {
                    downloadButton.setEnabled(false);
                    rollbackButton.setEnabled(false);
                }
            }
        });
    }

    // --------------------------------------------------------------------------------------
    //
    // Device Event List Management
    //
    // --------------------------------------------------------------------------------------

    private static final int PERIOD_MILLIS = 1000;

    public void refreshWhenOnline() {

        Timer timer = new Timer() {

            private static final int TIMEOUT_MILLIS = 30000;
            private int countdownMillis = TIMEOUT_MILLIS;

            @Override
            public void run() {
                if (selectedDevice != null) {
                    countdownMillis -= PERIOD_MILLIS;

                    //
                    // Poll the current status of the device until is online again or timeout.
                    gwtDeviceService.findDevice(selectedDevice.getScopeId(),
                            selectedDevice.getClientId(),
                            new AsyncCallback<GwtDevice>() {

                                @Override
                                public void onFailure(Throwable t) {
                                    done();
                                }

                                @Override
                                public void onSuccess(GwtDevice gwtDevice) {
                                    if (countdownMillis <= 0 ||
                                            // Allow the device to disconnect before checking if it's online again.
                                            ((TIMEOUT_MILLIS - countdownMillis) > 5000 && gwtDevice.isOnline())) {
                                        done();
                                    }
                                }

                                private void done() {
                                    cancel();
                                    // force a dirty on both tabs
                                    tabConfig.setEntity(selectedDevice);
                                    refresh();
                                }
                            });
                }
            }
        };
        grid.mask(MSGS.waiting());
        timer.scheduleRepeating(PERIOD_MILLIS);
    }

    public void refresh() {
        if (dirty && initialized) {
            if (selectedDevice == null || !selectedDevice.isOnline()) {

                toolBar.disable();
                refreshButton.disable();

                // clear the table
                grid.getStore().removeAll();
            } else {
                toolBar.enable();
                refreshButton.enable();
                downloadButton.disable();
                rollbackButton.disable();
                reload();
            }
        }
    }

    public void reload() {
        loader.load();
    }

    private void downloadSnapshot() {
        GwtSnapshot snapshot = grid.getSelectionModel().getSelectedItem();
        if (selectedDevice != null && snapshot != null) {

            StringBuilder sbUrl = new StringBuilder();
            sbUrl.append("device_snapshots?");
            sbUrl.append("&scopeId=")
                    .append(URL.encodeQueryString(currentSession.getSelectedAccountId()))
                    .append("&deviceId=")
                    .append(URL.encodeQueryString(selectedDevice.getId()))
                    .append("&snapshotId=")
                    .append(snapshot.getSnapshotId());
            Window.open(sbUrl.toString(), "_blank", "location=no");
        }
    }

    private void uploadSnapshot() {
        if (selectedDevice != null) {
            HiddenField<String> accountField = new HiddenField<String>();
            accountField.setName("scopeIdString");
            accountField.setValue(selectedDevice.getScopeId());

            HiddenField<String> clientIdField = new HiddenField<String>();
            clientIdField.setName("deviceIdString");
            clientIdField.setValue(selectedDevice.getId());

            List<HiddenField<?>> hiddenFields = new ArrayList<HiddenField<?>>();
            hiddenFields.add(accountField);
            hiddenFields.add(clientIdField);

            fileUpload = new FileUploadDialog(SERVLET_URL, hiddenFields);
            fileUpload.addListener(Events.Hide, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    dirty = true;
                    grid.mask(MSGS.applying());
                    toolBar.disable();

                    refreshWhenOnline();
                }
            });

            fileUpload.setHeading(MSGS.upload());
            fileUpload.show();
        }
    }

    private void rollbackSnapshot() {
        final GwtSnapshot snapshot = grid.getSelectionModel().getSelectedItem();
        if (selectedDevice != null && snapshot != null) {

            MessageBox.confirm(MSGS.confirm(),
                    DEVICE_MSGS.deviceSnapshotRollbackConfirm(),
                    new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent ce) {
                            // if confirmed, delete
                            Dialog dialog = ce.getDialog();
                            if (dialog.yesText.equals(ce.getButtonClicked().getText())) {

                                dirty = true;
                                grid.mask(MSGS.rollingBack());
                                toolBar.disable();

                                // mark the whole config panel dirty and for reload
                                tabConfig.setEntity(selectedDevice);

                                //
                                // Getting XSRF token
                                gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                    @Override
                                    public void onFailure(Throwable ex) {
                                        FailureHandler.handle(ex);
                                    }

                                    @Override
                                    public void onSuccess(GwtXSRFToken token) {
                                        // do the rollback
                                        gwtDeviceManagementService.rollbackDeviceSnapshot(token,
                                                selectedDevice,
                                                snapshot,
                                                new AsyncCallback<Void>() {

                                                    @Override
                                                    public void onFailure(Throwable caught) {
                                                        FailureHandler.handle(caught);
                                                        dirty = true;
                                                    }

                                                    @Override
                                                    public void onSuccess(Void arg0) {
                                                        refreshWhenOnline();
                                                    }
                                                });
                                    }
                                });

                            }
                        }
                    });
        }
    }

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        public DataLoadListener() {
        }

        @Override
        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
        }

        @Override
        public void loaderLoadException(LoadEvent le) {

            if (le.exception != null) {
                ConsoleInfo.display(MSGS.popupError(), DEVICE_MSGS.deviceConnectionError());
            }
            store.removeAll();
            grid.unmask();
            toolBar.enable();
        }
    }
}
