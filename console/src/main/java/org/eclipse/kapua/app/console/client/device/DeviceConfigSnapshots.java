/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.device.button.SnapshotDownloadButton;
import org.eclipse.kapua.app.console.client.device.button.SnapshotRollbackButton;
import org.eclipse.kapua.app.console.client.device.button.SnapshotUploadButton;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.ui.dialog.FileUploadDialog;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtSnapshot;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

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

public class DeviceConfigSnapshots extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT.create(GwtDeviceManagementService.class);
    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    private final static String SERVLET_URL = "console/file/configuration/snapshot";

    private GwtSession m_currentSession;

    private boolean m_dirty;
    private boolean m_initialized;
    private GwtDevice m_selectedDevice;
    private DeviceTabConfiguration m_tabConfig;

    private ToolBar m_toolBar;

    private Button m_refreshButton;
    private boolean refreshProcess;

    private Button m_downloadButton;
    private Button m_rollbackButton;
    private Button m_uploadButton;

    private ListStore<GwtSnapshot> m_store;
    private Grid<GwtSnapshot> m_grid;
    private BaseListLoader<ListLoadResult<GwtSnapshot>> m_loader;
    private FileUploadDialog m_fileUpload;

    protected boolean downloadProcess;
    protected boolean uploadProcess;
    protected boolean rollbackProcess;

    public DeviceConfigSnapshots(GwtSession currentSession,
            DeviceTabConfiguration tabConfig) {
        m_currentSession = currentSession;
        m_tabConfig = tabConfig;
        m_dirty = false;
        m_initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        m_dirty = true;
        m_selectedDevice = selectedDevice;
    }

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
        devicesHistoryPanel.setTopComponent(m_toolBar);
        devicesHistoryPanel.add(m_grid);

        add(devicesHistoryPanel);
        m_initialized = true;
    }

    private void initToolBar() {
        m_toolBar = new ToolBar();
        m_toolBar.setEnabled(false);

        //
        // Refresh Button
        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshProcess = true;
                    m_refreshButton.setEnabled(false);

                    m_dirty = true;
                    reload();

                    m_refreshButton.setEnabled(true);
                    refreshProcess = false;
                }
            }
        });
        m_refreshButton.setEnabled(false);
        m_toolBar.add(m_refreshButton);
        m_toolBar.add(new SeparatorToolItem());

        m_downloadButton = new SnapshotDownloadButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!downloadProcess) {
                    downloadProcess = true;
                    m_downloadButton.setEnabled(false);

                    downloadSnapshot();

                    m_downloadButton.setEnabled(true);
                    downloadProcess = false;
                }
            }
        });
        m_downloadButton.setEnabled(false);

        m_uploadButton = new SnapshotUploadButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!uploadProcess) {
                    uploadProcess = true;
                    m_uploadButton.setEnabled(false);

                    uploadSnapshot();

                    m_uploadButton.setEnabled(true);
                    uploadProcess = false;
                }
            }
        });
        m_uploadButton.setEnabled(true);

        m_rollbackButton = new SnapshotRollbackButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!rollbackProcess) {
                    rollbackProcess = true;
                    m_rollbackButton.setEnabled(false);

                    rollbackSnapshot();

                    m_rollbackButton.setEnabled(true);
                    rollbackProcess = false;
                }
            }
        });
        m_rollbackButton.setEnabled(false);

        m_toolBar.add(m_downloadButton);
        m_toolBar.add(new SeparatorToolItem());
        m_toolBar.add(m_rollbackButton);
        m_toolBar.add(new SeparatorToolItem());
        m_toolBar.add(m_uploadButton);
    }

    private void initGrid() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("snapshotId", MSGS.deviceSnapshotId(), 25);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columns.add(column);

        column = new ColumnConfig("createdOnFormatted", MSGS.deviceSnapshotCreatedOn(), 75);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.LEFT);
        columns.add(column);

        // loader and store
        RpcProxy<ListLoadResult<GwtSnapshot>> proxy = new RpcProxy<ListLoadResult<GwtSnapshot>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtSnapshot>> callback) {
                if (m_selectedDevice != null && m_dirty && m_initialized) {
                    if (m_selectedDevice.isOnline()) {
                        gwtDeviceManagementService.findDeviceSnapshots(m_selectedDevice,
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
                m_dirty = false;
            }
        };
        m_loader = new BaseListLoader<ListLoadResult<GwtSnapshot>>(proxy);
        m_loader.setSortDir(SortDir.DESC);
        m_loader.setSortField("createdOnFormatted");
        m_loader.addLoadListener(new DataLoadListener());

        m_store = new ListStore<GwtSnapshot>(m_loader);
        m_grid = new Grid<GwtSnapshot>(m_store, new ColumnModel(columns));
        m_grid.setBorders(false);
        m_grid.setStateful(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.setTrackMouseOver(false);
        m_grid.getView().setAutoFill(true);
        m_grid.getView().setEmptyText(MSGS.deviceSnapshotsNone());

        GridSelectionModel<GwtSnapshot> selectionModel = new GridSelectionModel<GwtSnapshot>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        m_grid.setSelectionModel(selectionModel);
        m_grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtSnapshot>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtSnapshot> se) {
                if (se.getSelectedItem() != null) {
                    m_downloadButton.setEnabled(true);
                    m_rollbackButton.setEnabled(true);
                } else {
                    m_downloadButton.setEnabled(false);
                    m_rollbackButton.setEnabled(false);
                }
            }
        });
    }

    // --------------------------------------------------------------------------------------
    //
    // Device Event List Management
    //
    // --------------------------------------------------------------------------------------

    public void refreshWhenOnline() {
        final int PERIOD_MILLIS = 1000;

        Timer timer = new Timer() {

            private int TIMEOUT_MILLIS = 30000;
            private int countdownMillis = TIMEOUT_MILLIS;

            public void run() {
                if (m_selectedDevice != null) {
                    countdownMillis -= PERIOD_MILLIS;

                    //
                    // Poll the current status of the device until is online again or timeout.
                    gwtDeviceService.findDevice(m_selectedDevice.getScopeId(),
                            m_selectedDevice.getClientId(),
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
                                    m_tabConfig.setDevice(m_selectedDevice);
                                    refresh();
                                }
                            });
                }
            }
        };
        m_grid.mask(MSGS.waiting());
        timer.scheduleRepeating(PERIOD_MILLIS);
    }

    public void refresh() {
        if (m_dirty && m_initialized) {
            if (m_selectedDevice == null) {

                m_toolBar.disable();
                m_refreshButton.disable();

                // clear the table
                m_grid.getStore().removeAll();
            } else {
                m_toolBar.enable();
                m_refreshButton.enable();
                reload();
            }
        }
    }

    public void reload() {
        m_loader.load();
    }

    private void downloadSnapshot() {
        GwtSnapshot snapshot = m_grid.getSelectionModel().getSelectedItem();
        if (m_selectedDevice != null && snapshot != null) {

            StringBuilder sbUrl = new StringBuilder();

            if (UserAgentUtils.isSafari() || UserAgentUtils.isChrome()) {
                sbUrl.append("console/device_snapshots?");
            } else {
                sbUrl.append("device_snapshots?");
            }

            sbUrl.append("&scopeId=")
                    .append(URL.encodeQueryString(m_currentSession.getSelectedAccount().getId()))
                    .append("&deviceId=")
                    .append(URL.encodeQueryString(m_selectedDevice.getId()))
                    .append("&snapshotId=")
                    .append(snapshot.getSnapshotId());
            Window.open(sbUrl.toString(), "_blank", "location=no");
        }
    }

    private void uploadSnapshot() {
        if (m_selectedDevice != null) {
            HiddenField<String> accountField = new HiddenField<String>();
            accountField.setName("scopeIdString");
            accountField.setValue(m_selectedDevice.getScopeId());

            HiddenField<String> clientIdField = new HiddenField<String>();
            clientIdField.setName("deviceIdString");
            clientIdField.setValue(m_selectedDevice.getId());

            List<HiddenField<?>> hiddenFields = new ArrayList<HiddenField<?>>();
            hiddenFields.add(accountField);
            hiddenFields.add(clientIdField);

            m_fileUpload = new FileUploadDialog(SERVLET_URL, hiddenFields);
            m_fileUpload.addListener(Events.Hide, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    m_dirty = true;
                    m_grid.mask(MSGS.applying());
                    m_toolBar.disable();

                    refreshWhenOnline();
                }
            });

            m_fileUpload.setHeading(MSGS.upload());
            m_fileUpload.show();
        }
    }

    private void rollbackSnapshot() {
        final GwtSnapshot snapshot = m_grid.getSelectionModel().getSelectedItem();
        if (m_selectedDevice != null && snapshot != null) {

            MessageBox.confirm(MSGS.confirm(),
                    MSGS.deviceSnapshotRollbackConfirm(),
                    new Listener<MessageBoxEvent>() {

                        public void handleEvent(MessageBoxEvent ce) {
                            // if confirmed, delete
                            Dialog dialog = ce.getDialog();
                            if (dialog.yesText.equals(ce.getButtonClicked().getText())) {

                                m_dirty = true;
                                m_grid.mask(MSGS.rollingBack());
                                m_toolBar.disable();

                                // mark the whole config panel dirty and for reload
                                m_tabConfig.setDevice(m_selectedDevice);

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
                                                m_selectedDevice,
                                                snapshot,
                                                new AsyncCallback<Void>() {

                                                    public void onFailure(Throwable caught) {
                                                        FailureHandler.handle(caught);
                                                        m_dirty = true;
                                                    }

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

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
        }

        public void loaderLoadException(LoadEvent le) {

            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
            m_store.removeAll();
            m_grid.unmask();
            m_toolBar.enable();
        }
    }
}
