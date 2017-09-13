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
package org.eclipse.kapua.app.console.module.device.client.device;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

public class DeviceGrid extends EntityGrid<GwtDevice> {

    private GwtDeviceQuery query;
    private DeviceGridToolbar toolbar;

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    public DeviceGrid(AbstractEntityView<GwtDevice> entityView, final GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtDeviceQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setPredicates(new GwtDeviceQueryPredicates());
    }

    @Override
    protected void selectionChangedEvent(GwtDevice selectedItem) {
        super.selectionChangedEvent(selectedItem);
        if (selectedItem != null) {
            if (currentSession.hasDeviceUpdatePermission()) {
                getToolbar().getDeleteEntityButton().setEnabled(true);
            }
            if (currentSession.hasDeviceDeletePermission()) {
                getToolbar().getEditEntityButton().setEnabled(true);
            }
        } else {
            getToolbar().getEditEntityButton().setEnabled(false);
            getToolbar().getDeleteEntityButton().setEnabled(false);
        }

    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtDevice>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtDevice>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDevice>> callback) {
                GWT_DEVICE_SERVICE.query((PagingLoadConfig) loadConfig,
                        query,
                        callback);
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        //
        // Column Configuration
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig("status", MSGS.deviceTableStatus(), 50);
        column.setAlignment(HorizontalAlignment.CENTER);
        GridCellRenderer<GwtDevice> setStatusIcon = new GridCellRenderer<GwtDevice>() {

            public String render(GwtDevice gwtDevice, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtDevice> deviceList, Grid<GwtDevice> grid) {

                KapuaIcon icon;
                if (gwtDevice.getGwtDeviceConnectionStatusEnum() != null) {
                    switch (gwtDevice.getGwtDeviceConnectionStatusEnum()) {
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
                    case ANY:
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
        columnConfigs.add(column);

        column = new ColumnConfig("clientId", MSGS.deviceTableClientID(), 175);
        column.setSortable(true);
        columnConfigs.add(column);

        column = new ColumnConfig("displayName", MSGS.deviceTableDisplayName(), 150);
        column.setSortable(true);
        columnConfigs.add(column);

        column = new ColumnConfig("lastEventOnFormatted", MSGS.deviceTableLastReportedDate(), 130);
        column.setSortable(true);
        column.setAlignment(HorizontalAlignment.CENTER);
        columnConfigs.add(column);

        column = new ColumnConfig("lastEventType", MSGS.deviceTableLastEventType(), 130);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columnConfigs.add(column);

        column = new ColumnConfig("serialNumber", MSGS.deviceTableSerialNumber(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        // //
        // // Device Management Certificate
        // column = new ColumnConfig("Device Management Certificate Status", "DM", 50);
        // column.setAlignment(HorizontalAlignment.CENTER);
        // GridCellRenderer<GwtDevice> setDmStatusIcon = new GridCellRenderer<GwtDevice>() {
        //
        // public String render(GwtDevice gwtDevice, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtDevice> deviceList, Grid<GwtDevice> grid) {
        // if (gwtDevice.getSignedCertificateId() == null) {
        // // Device Management Communication is not signed
        // return ImageUtils.toHTML(Resources.INSTANCE.dmUnlock16(), MSGS.deviceTableCertificateDMTooltipStatusNotSigned(), "14");
        // } else {
        // // Device Management Communication is signed
        // return ImageUtils.toHTML(Resources.INSTANCE.lockGreen16(), MSGS.deviceTableCertificateDMTooltipStatusSigned(), "14");
        // }
        // }
        // };
        // column.setRenderer(setDmStatusIcon);
        // column.setAlignment(HorizontalAlignment.CENTER);
        // column.setSortable(false);
        // configs.add(column);

        column = new ColumnConfig("applicationIdentifiers", MSGS.deviceTableApplications(), 100);
        column.setSortable(false);
        column.setHidden(false);
        columnConfigs.add(column);

        column = new ColumnConfig("iotFrameworkVersion", MSGS.deviceTableEsfKuraVersion(), 80);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        columnConfigs.add(column);

        column = new ColumnConfig("customAttribute1", MSGS.deviceTableCustomAttribute1(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("customAttribute2", MSGS.deviceTableCustomAttribute2(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("connectionIp", MSGS.deviceTableIpAddress(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("modelId", MSGS.deviceTableModelId(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("firmwareVersion", MSGS.deviceTableFirmwareVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("biosVersion", MSGS.deviceTableBiosVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("osVersion", MSGS.deviceTableOsVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("jvmVersion", MSGS.deviceTableJvmVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("osgiFrameworkVersion", MSGS.deviceTableOsgiVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("imei", MSGS.deviceTableImei(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("imsi", MSGS.deviceTableImsi(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        column = new ColumnConfig("iccid", MSGS.deviceTableIccid(), 100);
        column.setSortable(false);
        column.setHidden(true);
        columnConfigs.add(column);

        // signedCertificateId
        column = new ColumnConfig("signedCertificateId", MSGS.deviceTableCertificate(), 100);
        column.setSortable(false);
        column.setHidden(true);

        columnConfigs.add(column);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtDeviceQuery) filterQuery;
    }

    @Override
    protected EntityCRUDToolbar<GwtDevice> getToolbar() {
        if (toolbar == null) {
            toolbar = new DeviceGridToolbar(currentSession);
        }
        return toolbar;
    }

}
