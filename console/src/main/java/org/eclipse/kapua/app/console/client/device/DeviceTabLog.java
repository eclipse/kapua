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
 *     Dario Maranta <dariomaranta@gmail.com>
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.kapua.app.console.client.device.button.BundleStartButton;
import org.eclipse.kapua.app.console.client.device.button.BundleStopButton;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
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
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceTabLog extends LayoutContainer {

    private ToolBar toolBar;
    private Button startButton;

    private TextArea result;
    private String logs = "";
    private GwtDevice selectedDevice;

    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtBundle bundle;

    private final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT
            .create(GwtDeviceManagementService.class);
    private BundleStopButton stopButton;

    private String msgStop = "ATTENZIONE: Fermando il bundle non sarà più possibile visualizzare i nuovi log";
    protected Grid<GwtBundle> grid;
    private BaseListLoader<ListLoadResult<GwtBundle>> loader;
    private ListStore<GwtBundle> store;
    private String bundleName = "org.eclipse.kura.livelog";
    private ContentPanel devicesBundlesPanel;
    private int clientId;

    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }

        public void onSuccess(Void arg0) {

            stopButton.disable();
            startButton.enable();
        }
    };
    private TextField<String> commandField;

    public void refresh() {
        logs = "";
        refreshContent();
        toolBar.disable();
        if (selectedDevice != null && selectedDevice.isOnline()) {
            loader.load();
            toolBar.enable();
            connect(new Random().nextInt(), selectedDevice.getClientId(), this);
        }
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());

        // init components
        initToolBar();
        initGrid();
        initContent();

    }

    private void initToolBar() {
        toolBar = new ToolBar();

        final AsyncCallback<Void> callbackStart = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {

            }

            public void onSuccess(Void arg0) {
                result.clear();
                stopButton.enable();
                startButton.disable();
            }
        };

        // Start Button
        startButton = new BundleStartButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedDevice.isOnline()) {

                    //
                    // Getting XSRF token
                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                        @Override
                        public void onFailure(Throwable ex) {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token) {
                            gwtDeviceManagementService.startBundle(token, selectedDevice, store.findModel("name", bundleName), callbackStart);
                        }
                    });
                } else {
                    MessageBox.alert(MSGS.dialogAlerts(), MSGS.deviceOffline(), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                        }
                    });
                }
            }
        });
        startButton.setEnabled(true);
        toolBar.add(startButton);
        toolBar.add(new SeparatorToolItem());

        final AsyncCallback<Void> callbackStop = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {

            }

            public void onSuccess(Void arg0) {

                stopButton.disable();
                startButton.enable();
            }
        };

        // Stop Button
        stopButton = new BundleStopButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectedDevice.isOnline()) {
                    MessageBox.confirm(MSGS.confirm(), msgStop,
                            new Listener<MessageBoxEvent>() {

                                public void handleEvent(MessageBoxEvent ce) {
                                    // if confirmed, stop
                                    Dialog dialog = ce.getDialog();
                                    if (dialog.yesText.equals(ce.getButtonClicked().getText())) {
                                        // Getting XSRF token
                                        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                            @Override
                                            public void onFailure(Throwable ex) {
                                                FailureHandler.handle(ex);
                                            }

                                            @Override
                                            public void onSuccess(GwtXSRFToken token) {
                                                gwtDeviceManagementService.stopBundle(token, selectedDevice, store.findModel("name", bundleName), callbackStop);
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    MessageBox.alert(MSGS.dialogAlerts(), MSGS.deviceOffline(), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                        }
                    });
                }
            }
        });
        stopButton.setEnabled(true);
        toolBar.add(stopButton);

        Label lbl = new Label("This is just text.  It will not be interpreted "
                + "as <html>.");

        toolBar.add(lbl);

        toolBar.disable();

    }

    public void printLog(String log) {
        logs = (log + "\n\n" + logs);
        result.setValue(logs);
        result.repaint();
    }

    public void setDevice(GwtDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    private void initGrid() {
        RpcProxy<ListLoadResult<GwtBundle>> proxy = new RpcProxy<ListLoadResult<GwtBundle>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtBundle>> callback) {
                if (selectedDevice != null) {
                    if (selectedDevice.isOnline()) {
                        gwtDeviceManagementService.findBundles(selectedDevice, callback);
                    } else {
                        grid.getStore().removeAll();
                        grid.unmask();
                    }
                }
            }
        };
        loader = new BaseListLoader<ListLoadResult<GwtBundle>>(proxy);
        loader.addLoadListener(new DataLoadListener());
        store = new ListStore<GwtBundle>(loader);

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

        grid = new Grid<GwtBundle>(store, cm);
        grid.setView(view);
        grid.setBorders(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.setSelectionModel(selectionModel);
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtBundle>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtBundle> se) {
                if (grid.getSelectionModel().getSelectedItem() != null) {
                    GwtBundle selectedBundle = grid.getSelectionModel().getSelectedItem();
                    if ("bndActive".equals(selectedBundle.getStatus())) {
                        startButton.disable();
                        stopButton.enable();
                    } else {
                        stopButton.disable();
                        startButton.enable();
                    }
                }
            }
        });
    }

    private void initContent() {
        devicesBundlesPanel = new ContentPanel();
        devicesBundlesPanel.setBorders(false);
        devicesBundlesPanel.setBodyBorder(false);
        devicesBundlesPanel.setHeaderVisible(false);
        devicesBundlesPanel.setLayout(new FitLayout());
        devicesBundlesPanel.setScrollMode(Scroll.AUTO);
        devicesBundlesPanel.setTopComponent(toolBar);

        result = new TextArea();
        result.setBorders(false);
        result.setReadOnly(true);
        result.setEmptyText("");
        result.clear();
        result.setValue(Integer.toString(result.hashCode()));

        devicesBundlesPanel.add(result);

        add(devicesBundlesPanel);

    }

    private void refreshContent() {

        devicesBundlesPanel.remove(result);

        result = new TextArea();
        result.setBorders(false);
        result.setReadOnly(true);
        result.setValue(Integer.toString(result.hashCode()));
        result.enable();
        devicesBundlesPanel.add(result);
        devicesBundlesPanel.layout();
    }

    private class DataLoadListener extends KapuaLoadListener {

        public DataLoadListener() {
        }

        public void loaderBeforeLoad(LoadEvent le) {
            devicesBundlesPanel.mask(MSGS.loading());
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
            devicesBundlesPanel.unmask();
        }

        public void loaderLoadException(LoadEvent le) {

            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
            devicesBundlesPanel.unmask();

        }
    }

    public native void connect(int i, String clientId, DeviceTabLog deviceTabLog) /*-{

        var client = new window.parent.Paho.MQTT.Client("127.0.0.1",
                Number(61614), "/broker", "clientId" + i)

        client.onMessageArrived = function(message) {
            try {
                var bytes = message.payloadBytes
                if (bytes[0] == 31 && bytes[1] == 139 && bytes[2] == 8
                        && bytes[3] == 0) {
                    bytes = window.parent.pako.inflate(bytes)
                }
                var decoded = window.parent.KuraPayload.decode(bytes)
                deviceTabLog.@org.eclipse.kapua.app.console.client.device.DeviceTabLog::printLog(Ljava/lang/String;)(decoded.metric["0"].stringValue)
            } catch (err) {
                console.log(err)
            }
        }

        client.onConnectionLost = function(responseObject) {
            console.log("Connection Lost: " + responseObject.errorMessage);
        }

        client.connect({
            userName : 'kapua-broker',
            password : 'kapua-password',
            onSuccess : function() {
                client.subscribe("kapua-sys/" + clientId + "/LIVE_LOG/log");
            }
        })

        setTimeout(
                function() {
                    client.disconnect()
                    deviceTabLog.@org.eclipse.kapua.app.console.client.device.DeviceTabLog::stopBundle()()

                }, 1000 * 60);

    }-*/;

    private void stopBundle() {
        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

            @Override
            public void onFailure(Throwable ex) {
                Window.alert(ex.toString());
            }

            @Override
            public void onSuccess(GwtXSRFToken token) {
                gwtDeviceManagementService.stopBundle(token, selectedDevice, store.findModel("name", bundleName), callback);
            }
        });
    }

}
