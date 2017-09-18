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
package org.eclipse.kapua.app.console.module.device.client.device.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

public class DeviceTabProfile extends KapuaTabItem<GwtDevice> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private boolean initialized;

    private Grid<GwtGroupedNVPair> grid;
    private GroupingStore<GwtGroupedNVPair> store;
    private BaseListLoader<ListLoadResult<GwtGroupedNVPair>> loader;

    public DeviceTabProfile() {
        super(DEVICE_MSGS.deviceTabDescription(), new KapuaIcon(IconSet.INFO));
        initialized = false;
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);
        doRefresh();
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

        RpcProxy<ListLoadResult<GwtGroupedNVPair>> proxy = new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                if (selectedEntity != null) {
                    gwtDeviceService.findDeviceProfile(selectedEntity.getScopeId(), selectedEntity.getClientId(), callback);
                }
            }
        };

        loader = new BaseListLoader<ListLoadResult<GwtGroupedNVPair>>(proxy);
        loader.addLoadListener(new DataLoadListener());

        store = new GroupingStore<GwtGroupedNVPair>(loader);
        store.groupBy("groupLoc");

        ColumnConfig name = new ColumnConfig("nameLoc", DEVICE_MSGS.devicePropName(), 50);
        ColumnConfig value = new ColumnConfig("value", DEVICE_MSGS.devicePropValue(), 50);

        GridCellRenderer<GwtGroupedNVPair> renderer = new GridCellRenderer<GwtGroupedNVPair>() {

            @Override
            public Object render(GwtGroupedNVPair model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtGroupedNVPair> store, Grid<GwtGroupedNVPair> grid) {
                Object value = model.getValue();
                if (value != null) {
                    if ("devLastEventOn".equals(model.getName()) && !"N/A".equals(value)) {
                        return DateUtils.formatDateTime(new Date((Long) value));
                    } else if ("devSecurityAllowCredentialsChange".equals(model.getName())) {
                        return (Boolean) value ? MSGS.yes() : MSGS.no();
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
        view.setEmptyText(DEVICE_MSGS.deviceNoDeviceSelected());

        grid = new Grid<GwtGroupedNVPair>(store, cm);
        grid.setView(view);
        grid.setBorders(false);
        grid.setLoadMask(true);
        grid.setStripeRows(true);
        grid.disableTextSelection(false);

        tabProfileContentPanel.add(grid);

        add(tabProfileContentPanel);
        initialized = true;
    }

    @Override
    public void doRefresh() {
        if (initialized) {
            if (selectedEntity != null) {
                loader.load();
            } else {
                grid.getStore().removeAll();
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
