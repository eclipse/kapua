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
package org.eclipse.kapua.app.console.client.connection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleConnectionMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.misc.color.Color;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceConnectionService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceConnectionServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ConnectionGrid extends EntityGrid<GwtDeviceConnection> {

    private static final ConsoleConnectionMessages MSGS = GWT.create(ConsoleConnectionMessages.class);

    private final GwtDeviceConnectionServiceAsync gwtDeviceConnectionService = GWT.create(GwtDeviceConnectionService.class);

    private GwtDeviceConnectionQuery filterQuery;
    private ConnectionGridToolbar toolbar;

    ConnectionGrid(EntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        filterQuery = new GwtDeviceConnectionQuery();
        filterQuery.setScopeId(currentSession.getSelectedAccount().getId());
    }

    @Override
    protected EntityCRUDToolbar<GwtDeviceConnection> getToolbar() {
        if (toolbar == null) {
            toolbar = new ConnectionGridToolbar(currentSession);
        }
        return toolbar;
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtDeviceConnection>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtDeviceConnection>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDeviceConnection>> callback) {
                gwtDeviceConnectionService.query((PagingLoadConfig) loadConfig, filterQuery, callback);
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtDeviceConnection selectedItem) {
        super.selectionChangedEvent(selectedItem);
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        ColumnConfig column = null;
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        column = new ColumnConfig("connectionStatus", MSGS.connectionTableStatus(), 50);
        column.setAlignment(HorizontalAlignment.CENTER);
        GridCellRenderer<GwtDeviceConnection> setStatusIcon = new GridCellRenderer<GwtDeviceConnection>() {

            public String render(GwtDeviceConnection gwtDeviceConnection, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtDeviceConnection> deviceList,
                    Grid<GwtDeviceConnection> grid) {

                KapuaIcon icon;
                if (gwtDeviceConnection.getConnectionStatusEnum() != null) {
                    switch (gwtDeviceConnection.getConnectionStatusEnum()) {
                    case CONNECTED:
                        icon = new KapuaIcon(IconSet.PLUG);
                        icon.setColor(Color.GREEN);
                        break;
                    case DISCONNECTED:
                        icon = new KapuaIcon(IconSet.PLUG);
                        icon.setColor(Color.YELLOW);
                        break;
                    case MISSING:
                        icon = new KapuaIcon(IconSet.PLUG);
                        icon.setColor(Color.RED);
                        break;
                    default:
                        icon = new KapuaIcon(IconSet.PLUG);
                        icon.setColor(Color.GREY);
                        break;
                    }
                } else {
                    icon = new KapuaIcon(IconSet.PLUG);
                    icon.setColor(Color.GREY);
                }

                return icon.getInlineHTML();
            }
        };
        column.setRenderer(setStatusIcon);
        column.setAlignment(HorizontalAlignment.CENTER);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("clientId", 120);
        column.setHeader(MSGS.connectionTableClientId());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("userId", 120);
        column.setHeader(MSGS.connectionTableUserId());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("protocol", 120);
        column.setHeader(MSGS.connectionTableProtocol());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("connectionUserCouplingMode", 120);
        column.setHeader(MSGS.connectionTableUserCouplingMode());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("reservedUserId", 120);
        column.setHeader(MSGS.connectionTableReservedUserId());
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig("modifiedOnFormatted", MSGS.connectionTableModifiedOn(), 130);
        configs.add(column);
        return configs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return filterQuery;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        this.filterQuery = (GwtDeviceConnectionQuery) filterQuery;
    }
}
