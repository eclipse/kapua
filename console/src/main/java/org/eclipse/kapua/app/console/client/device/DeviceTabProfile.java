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
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.client.util.DateUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceTabProfile extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private GwtSession m_currentSession;
    private DevicesTable m_devicesTable;
    @SuppressWarnings("unused")
    private DeviceFilterPanel m_deviceFilterPanel;

    private boolean m_dirty;
    private boolean m_initialized;
    private GwtDevice m_selectedDevice;

    private Grid<GwtGroupedNVPair> m_grid;
    private GroupingStore<GwtGroupedNVPair> m_store;
    private BaseListLoader<ListLoadResult<GwtGroupedNVPair>> m_loader;

    private Button m_editButton;

    public DeviceTabProfile(DevicesTable devicesTable, DeviceFilterPanel deviceFilterPanel, GwtSession currentSession) {
        m_devicesTable = devicesTable;
        m_deviceFilterPanel = deviceFilterPanel;
        m_currentSession = currentSession;
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
        setBorders(false);

        ContentPanel tabProfileContentPanel = new ContentPanel();
        tabProfileContentPanel.setLayout(new FitLayout());
        tabProfileContentPanel.setBorders(false);
        tabProfileContentPanel.setBodyBorder(false);
        tabProfileContentPanel.setHeaderVisible(false);
        tabProfileContentPanel.setTopComponent(getToolbar());

        RpcProxy<ListLoadResult<GwtGroupedNVPair>> proxy = new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                if (m_selectedDevice != null) {
                    gwtDeviceService.findDeviceProfile(m_selectedDevice.getScopeId(), m_selectedDevice.getClientId(), callback);
                }
            }
        };

        m_loader = new BaseListLoader<ListLoadResult<GwtGroupedNVPair>>(proxy);
        m_loader.addLoadListener(new DataLoadListener());

        m_store = new GroupingStore<GwtGroupedNVPair>(m_loader);
        m_store.groupBy("groupLoc");

        ColumnConfig name = new ColumnConfig("nameLoc", MSGS.devicePropName(), 50);
        ColumnConfig value = new ColumnConfig("value", MSGS.devicePropValue(), 50);

        GridCellRenderer<GwtGroupedNVPair> renderer = new GridCellRenderer<GwtGroupedNVPair>() {

            @Override
            public Object render(GwtGroupedNVPair model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtGroupedNVPair> store, Grid<GwtGroupedNVPair> grid) {
                Object value = model.getValue();
                if (value != null && value instanceof String) {
                    String stringValue = (String) value;

                    if (model.getName().equals("devLastEventOn") && stringValue.compareTo("N/A") != 0) {
                        return DateUtils.formatDateTime(new Date(Long.parseLong(stringValue)));
                    }
                }
                return value;
            }
        };

        value.setRenderer(renderer);

        List<ColumnConfig> config = new ArrayList<ColumnConfig>();
        config.add(name);
        config.add(value);

        ColumnModel cm = new ColumnModel(config);
        GroupingView view = new GroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.setEmptyText(MSGS.deviceNoDeviceSelected());

        m_grid = new Grid<GwtGroupedNVPair>(m_store, cm);
        m_grid.setView(view);
        m_grid.setBorders(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.disableTextSelection(false);

        tabProfileContentPanel.add(m_grid);

        add(tabProfileContentPanel);
        m_initialized = true;
    }

    private ToolBar getToolbar() {

        ToolBar menuToolBar = new ToolBar();

        //
        // Edit User Button
        m_editButton = new EditButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_grid != null) {
                    GwtDevice gwtDevice = m_selectedDevice;
                    if (gwtDevice != null) {
                        DeviceForm deviceForm = new DeviceForm(gwtDevice, m_currentSession);
                        deviceForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                            public void handleEvent(ComponentEvent be) {
                                m_dirty = true;
                                m_devicesTable.refresh();
                                refresh();
                            }
                        });
                        deviceForm.show();
                    }
                }
            }

        });
        m_editButton.setEnabled(false);
        menuToolBar.add(m_editButton);

        return menuToolBar;
    }

    public void refresh() {
        if (m_dirty && m_initialized) {
            m_dirty = false;
            if (m_selectedDevice != null) {

                if (m_currentSession.hasDeviceUpdatePermission()) {
                    m_editButton.setEnabled(true);
                } else {
                    m_editButton.setTitle(MSGS.youDontHavePermissionTo("click", "button", "device:update"));
                }

                m_loader.load();
            } else {
                m_grid.getStore().removeAll();
                m_editButton.setEnabled(false);
            }
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
    }
}
