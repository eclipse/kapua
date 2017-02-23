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

import org.eclipse.kapua.app.console.client.device.button.BundleStartButton;
import org.eclipse.kapua.app.console.client.device.button.BundleStopButton;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.device.management.bundles.GwtBundle;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceTabBundles extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT.create(GwtDeviceManagementService.class);
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    @SuppressWarnings("unused")
    private GwtSession m_currentSession;
    private DeviceTabs m_deviceTabs;

    private boolean m_dirty;
    private boolean m_initialized;
    private GwtDevice m_selectedDevice;

    private ToolBar m_toolBar;

    private Button m_refreshButton;
    private Button m_startButton;
    private Button m_stopButton;

    private Grid<GwtBundle> m_grid;
    private ListStore<GwtBundle> m_store;
    private BaseListLoader<ListLoadResult<GwtBundle>> m_loader;

    protected boolean refreshProcess;

    public DeviceTabBundles(GwtSession currentSession,
            DeviceTabs deviceTabs) {
        m_currentSession = currentSession;
        m_deviceTabs = deviceTabs;
        m_dirty = true;
        m_initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        m_dirty = true;
        m_selectedDevice = selectedDevice;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());

        // init components
        initToolBar();
        initGrid();

        ContentPanel devicesBundlesPanel = new ContentPanel();
        devicesBundlesPanel.setBorders(false);
        devicesBundlesPanel.setBodyBorder(false);
        devicesBundlesPanel.setHeaderVisible(false);
        devicesBundlesPanel.setLayout(new FitLayout());
        devicesBundlesPanel.setScrollMode(Scroll.AUTO);
        devicesBundlesPanel.setTopComponent(m_toolBar);
        devicesBundlesPanel.add(m_grid);

        add(devicesBundlesPanel);
        m_initialized = true;
    }

    private void initToolBar() {
        m_toolBar = new ToolBar();

        //
        // Refresh Button
        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshProcess = true;

                    if (m_selectedDevice.isOnline()) {
                        m_toolBar.disable();
                        m_dirty = true;
                        refresh();

                        refreshProcess = false;
                    } else {
                        MessageBox.alert(MSGS.dialogAlerts(), MSGS.deviceOffline(),
                                new Listener<MessageBoxEvent>() {

                                    @Override
                                    public void handleEvent(MessageBoxEvent be) {
                                        m_grid.unmask();

                                        refreshProcess = false;
                                    }
                                });
                    }
                }
            }
        });

        m_refreshButton.setEnabled(true);
        m_toolBar.add(m_refreshButton);
        m_toolBar.add(new SeparatorToolItem());

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
                m_dirty = true;
            }

            public void onSuccess(Void arg0) {
                // mark this panel dirty and also all the other pier panels
                m_deviceTabs.setDevice(m_selectedDevice);
                m_dirty = true;
                refresh();
            }
        };

        //
        // Start Button
        m_startButton = new BundleStartButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_selectedDevice.isOnline()) {
                    m_toolBar.disable();
                    m_grid.mask(MSGS.loading());

                    //
                    // Getting XSRF token
                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                        @Override
                        public void onFailure(Throwable ex) {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token) {
                            gwtDeviceManagementService.startBundle(token,
                                    m_selectedDevice,
                                    m_grid.getSelectionModel().getSelectedItem(),
                                    callback);
                        }
                    });
                } else {
                    MessageBox.alert(MSGS.dialogAlerts(), MSGS.deviceOffline(),
                            new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    m_grid.unmask();
                                }
                            });
                }
            }
        });
        m_startButton.setEnabled(true);
        m_toolBar.add(m_startButton);
        m_toolBar.add(new SeparatorToolItem());

        //
        // Stop Button
        m_stopButton = new BundleStopButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_selectedDevice.isOnline()) {
                    final GwtBundle gwtBundle = m_grid.getSelectionModel().getSelectedItem();
                    String bundleName = gwtBundle.getName();
                    MessageBox.confirm(MSGS.confirm(),
                            MSGS.deviceStopBundle(bundleName),
                            new Listener<MessageBoxEvent>() {

                                public void handleEvent(MessageBoxEvent ce) {
                                    // if confirmed, stop
                                    Dialog dialog = ce.getDialog();
                                    if (dialog.yesText.equals(ce.getButtonClicked().getText())) {
                                        m_toolBar.disable();
                                        m_grid.mask(MSGS.loading());
                                        //
                                        // Getting XSRF token
                                        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                            @Override
                                            public void onFailure(Throwable ex) {
                                                FailureHandler.handle(ex);
                                            }

                                            @Override
                                            public void onSuccess(GwtXSRFToken token) {
                                                gwtDeviceManagementService.stopBundle(token,
                                                        m_selectedDevice,
                                                        gwtBundle,
                                                        callback);
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    MessageBox.alert(MSGS.dialogAlerts(), MSGS.deviceOffline(),
                            new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    m_grid.unmask();
                                }
                            });
                }
            }
        });
        m_stopButton.setEnabled(true);
        m_toolBar.add(m_stopButton);
        m_toolBar.add(new SeparatorToolItem());

        m_toolBar.disable();
    }

    private void initGrid() {
        RpcProxy<ListLoadResult<GwtBundle>> proxy = new RpcProxy<ListLoadResult<GwtBundle>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtBundle>> callback) {
                if (m_selectedDevice != null) {
                    if (m_selectedDevice.isOnline()) {
                        gwtDeviceManagementService.findBundles(m_selectedDevice, callback);
                    } else {
                        m_grid.getStore().removeAll();
                        m_grid.unmask();
                    }
                }
            }
        };
        m_loader = new BaseListLoader<ListLoadResult<GwtBundle>>(proxy);
        m_loader.addLoadListener(new DataLoadListener());

        m_store = new ListStore<GwtBundle>(m_loader);

        ColumnConfig id = new ColumnConfig("id", MSGS.deviceBndId(), 10);
        ColumnConfig name = new ColumnConfig("name", MSGS.deviceBndName(), 50);
        ColumnConfig status = new ColumnConfig("statusLoc", MSGS.deviceBndState(), 20);
        ColumnConfig version = new ColumnConfig("version", MSGS.deviceBndVersion(), 20);

        List<ColumnConfig> config = new ArrayList<ColumnConfig>();
        config.add(id);
        config.add(name);
        config.add(status);
        config.add(version);

        ColumnModel cm = new ColumnModel(config);

        GridView view = new GridView();
        view.setForceFit(true);
        view.setEmptyText(MSGS.deviceNoDeviceSelectedOrOffline());

        GridSelectionModel<GwtBundle> selectionModel = new GridSelectionModel<GwtBundle>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        m_grid = new Grid<GwtBundle>(m_store, cm);
        m_grid.setView(view);
        m_grid.setBorders(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.setSelectionModel(selectionModel);
        m_grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtBundle>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtBundle> se) {
                if (m_grid.getSelectionModel().getSelectedItem() != null) {
                    GwtBundle selectedBundle = m_grid.getSelectionModel().getSelectedItem();
                    if ("bndActive".equals(selectedBundle.getStatus())) {
                        m_startButton.disable();
                        m_stopButton.enable();
                    } else {
                        m_stopButton.disable();
                        m_startButton.enable();
                    }
                }
            }
        });
    }

    public void refresh() {
        if (m_dirty && m_initialized) {

            m_dirty = false;
            if (m_selectedDevice != null) {
                m_loader.load();
                m_toolBar.enable();
                m_startButton.disable();
                m_stopButton.disable();
            } else {
                m_grid.getStore().removeAll();
                m_toolBar.disable();
            }
        }
    }

    public void reload() {
        if (m_selectedDevice != null) {
            m_loader.load();
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

        public void loaderBeforeLoad(LoadEvent le) {
            m_grid.mask(MSGS.loading());
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
            m_startButton.disable();
            m_stopButton.disable();
            m_grid.unmask();
        }

        public void loaderLoadException(LoadEvent le) {

            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
            m_startButton.disable();
            m_stopButton.disable();
            m_grid.unmask();
        }
    }
}
