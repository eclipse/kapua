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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.button.AddButton;
import org.eclipse.kapua.app.console.client.ui.button.Button;
import org.eclipse.kapua.app.console.client.ui.button.DeleteButton;
import org.eclipse.kapua.app.console.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.client.ui.button.ExportButton;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.ui.misc.color.Color;
import org.eclipse.kapua.app.console.client.ui.widget.KapuaMenuItem;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DevicesTable extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    private static final int DEVICE_PAGE_SIZE = 250;

    private GwtSession currentSession;
    private GwtDevice selectedDevice;

    private DevicesView devicesView;
    private ContentPanel tableContainer;
    private ToolBar devicesToolBar;

    private Button addDeviceButton;
    private Button editDeviceButton;

    private Button refreshButton;
    private boolean refreshProcess;
    private Button deleteDeviceButton;
    private SplitButton export;

    private ToggleButton toggleFilter;

    private ContentPanel filterPanel;

    private Grid<GwtDevice> devicesGrid;
    private PagingToolBar pagingToolBar;
    private BasePagingLoader<PagingLoadResult<GwtDevice>> loader;
    private GwtDeviceQueryPredicates filterPredicates;

    public DevicesTable(DevicesView deviceView,
            GwtSession currentSession,
            ContentPanel filterPanel) {
        devicesView = deviceView;
        this.currentSession = currentSession;
        filterPredicates = new GwtDeviceQueryPredicates();
        this.filterPanel = filterPanel;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        // FitLayout that expands to the whole screen
        setLayout(new FitLayout());
        setBorders(false);

        // init tab content
        initDevicesTable();
        add(tableContainer);
    }

    // --------------------------------------------------------------------------------------
    //
    // Device List Management
    //
    // --------------------------------------------------------------------------------------

    private void initDevicesTable() {
        // init components
        initDevicesToolBar();
        initDevicesGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(false);
        tableContainer.setHeaderVisible(false);
        tableContainer.setScrollMode(Scroll.AUTO);
        tableContainer.setLayout(new FitLayout());
        tableContainer.setTopComponent(devicesToolBar);
        tableContainer.add(devicesGrid);
        tableContainer.setBottomComponent(pagingToolBar);
    }

    private void initDevicesToolBar() {

        devicesToolBar = new ToolBar();

        // Edit Device Button
        if (currentSession.hasDeviceCreatePermission()) {
            addDeviceButton = new AddButton(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    DeviceForm deviceForm = new DeviceForm(currentSession);
                    deviceForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                        public void handleEvent(ComponentEvent be) {
                            refresh(filterPredicates);
                        }
                    });
                    deviceForm.show();
                }

            });
            devicesToolBar.add(addDeviceButton);
            devicesToolBar.add(new SeparatorToolItem());
        }

        //
        // Edit User Button
        editDeviceButton = new EditButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (devicesGrid != null) {
                    final GwtDevice gwtDevice = devicesGrid.getSelectionModel().getSelectedItem();
                    if (gwtDevice != null) {
                        DeviceForm deviceForm = new DeviceForm(gwtDevice, currentSession);
                        deviceForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                            public void handleEvent(ComponentEvent be) {
                                refresh();
                            }
                        });
                        deviceForm.show();
                    }
                }
            }

        });

        editDeviceButton.setEnabled(false);
        devicesToolBar.add(editDeviceButton);
        devicesToolBar.add(new SeparatorToolItem());

        //
        // Refresh Button
        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshProcess = true;

                    refresh(filterPredicates);

                    refreshProcess = false;
                }
            }
        });
        refreshButton.setEnabled(true);
        devicesToolBar.add(refreshButton);
        devicesToolBar.add(new SeparatorToolItem());

        //
        // Export
        Menu menu = new Menu();
        menu.add(new KapuaMenuItem(MSGS.exportToExcel(), IconSet.FILE_EXCEL_O,
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("xls");
                    }
                }));
        menu.add(new KapuaMenuItem(MSGS.exportToCSV(), IconSet.FILE_TEXT_O,
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("csv");
                    }
                }));

        export = new ExportButton();
        export.setMenu(menu);

        devicesToolBar.add(export);
        devicesToolBar.add(new SeparatorToolItem());

        //
        // Delete Device Button
        deleteDeviceButton = new DeleteButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (devicesGrid != null) {
                    final GwtDevice gwtDevice = devicesGrid.getSelectionModel().getSelectedItem();
                    if (gwtDevice != null) {
                        // delete the selected device
                        // ask for confirmation
                        MessageBox.confirm(MSGS.confirm(), MSGS.deviceDeleteConfirmation(gwtDevice.getClientId()),
                                new Listener<MessageBoxEvent>() {

                                    public void handleEvent(MessageBoxEvent ce) {
                                        // if confirmed, delete
                                        Dialog dialog = ce.getDialog();
                                        if (dialog.yesText.equals(ce.getButtonClicked().getText())) {
                                            deleteDevice(gwtDevice);
                                        }
                                    }
                                });
                    }
                }
            }
        });
        deleteDeviceButton.setEnabled(false);
        devicesToolBar.add(deleteDeviceButton);
        devicesToolBar.add(new FillToolItem());

        //
        // Live Button
        toggleFilter = new ToggleButton(MSGS.deviceTableToolbarCloseFilter(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (toggleFilter.isPressed()) {
                    filterPanel.show();
                    toggleFilter.setText(MSGS.deviceTableToolbarCloseFilter());
                } else {
                    filterPanel.hide();
                    toggleFilter.setText(MSGS.deviceTableToolbarOpenFilter());
                }
            }
        });
        toggleFilter.toggle(true);
        devicesToolBar.add(toggleFilter);
    }

    private void initDevicesGrid() {
        //
        // Column Configuration
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

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
        configs.add(column);

        column = new ColumnConfig("clientId", MSGS.deviceTableClientID(), 175);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig("displayName", MSGS.deviceTableDisplayName(), 150);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig("lastEventOnFormatted", MSGS.deviceTableLastReportedDate(), 130);
        column.setSortable(true);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("lastEventType", MSGS.deviceTableLastEventType(), 130);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("serialNumber", MSGS.deviceTableSerialNumber(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

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
        configs.add(column);

        column = new ColumnConfig("iotFrameworkVersion", MSGS.deviceTableEsfKuraVersion(), 80);
        column.setSortable(false);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("customAttribute1", MSGS.deviceTableCustomAttribute1(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("customAttribute2", MSGS.deviceTableCustomAttribute2(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("connectionIp", MSGS.deviceTableIpAddress(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("modelId", MSGS.deviceTableModelId(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("firmwareVersion", MSGS.deviceTableFirmwareVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("biosVersion", MSGS.deviceTableBiosVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("osVersion", MSGS.deviceTableOsVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("jvmVersion", MSGS.deviceTableJvmVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("osgiFrameworkVersion", MSGS.deviceTableOsgiVersion(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("imei", MSGS.deviceTableImei(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("imsi", MSGS.deviceTableImsi(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        column = new ColumnConfig("iccid", MSGS.deviceTableIccid(), 100);
        column.setSortable(false);
        column.setHidden(true);
        configs.add(column);

        // signedCertificateId
        column = new ColumnConfig("signedCertificateId", MSGS.deviceTableCertificate(), 100);
        column.setSortable(false);
        column.setHidden(true);

        configs.add(column);

        // loader and store
        RpcProxy<PagingLoadResult<GwtDevice>> proxy = new RpcProxy<PagingLoadResult<GwtDevice>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtDevice>> callback) {
                PagingLoadConfig pagingConfig = (BasePagingLoadConfig) loadConfig;
                ((BasePagingLoadConfig) pagingConfig).setLimit(DEVICE_PAGE_SIZE);
                gwtDeviceService.findDevices(pagingConfig,
                        currentSession.getSelectedAccount().getId(),
                        filterPredicates,
                        callback);
            }
        };
        loader = new BasePagingLoader<PagingLoadResult<GwtDevice>>(proxy);
        loader.setSortDir(SortDir.ASC);
        loader.setSortField("clientId");
        loader.setRemoteSort(true);

        SwappableListStore<GwtDevice> store = new SwappableListStore<GwtDevice>(loader);
        store.setKeyProvider(new ModelKeyProvider<GwtDevice>() {

            public String getKey(GwtDevice device) {
                return device.getScopeId() + ":" + device.getClientId();
            }
        });

        devicesGrid = new Grid<GwtDevice>(store, new ColumnModel(configs));
        devicesGrid.setBorders(false);
        devicesGrid.setStateful(false);
        devicesGrid.setLoadMask(true);
        devicesGrid.setStripeRows(true);
        devicesGrid.setAutoExpandColumn("displayName");
        devicesGrid.getView().setAutoFill(true);
        devicesGrid.getView().setEmptyText(MSGS.deviceTableNoDevices());
        devicesGrid.disableTextSelection(false);
        devicesGrid.addListener(Events.HeaderClick, new Listener<GridEvent<GwtDevice>>() {

            @Override
            public void handleEvent(GridEvent<GwtDevice> be) {

                if (be.getColIndex() == 1) {

                    if (filterPredicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.CLIENT_ID.name())) {
                        if (filterPredicates.getSortOrderEnum().equals(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING)) {
                            filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.DESCENDING.name());

                        } else {
                            filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                        }
                    } else {
                        filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                    }
                    filterPredicates.setSortAttribute(GwtDeviceQueryPredicates.GwtSortAttribute.CLIENT_ID.name());

                } else if (be.getColIndex() == 2) {
                    if (filterPredicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.DISPLAY_NAME.name())) {
                        if (filterPredicates.getSortOrderEnum().equals(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING)) {
                            filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.DESCENDING.name());
                        } else {
                            filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                        }
                    } else {
                        filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                    }
                    filterPredicates.setSortAttribute(GwtDeviceQueryPredicates.GwtSortAttribute.DISPLAY_NAME.name());

                } else if (be.getColIndex() == 3) {
                    if (filterPredicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.LAST_EVENT_ON.name())) {
                        if (filterPredicates.getSortOrderEnum().equals(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING)) {
                            filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.DESCENDING.name());
                        } else {
                            filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                        }
                    } else {
                        filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                    }
                    filterPredicates.setSortAttribute(GwtDeviceQueryPredicates.GwtSortAttribute.LAST_EVENT_ON.name());

                } else {
                    return;
                }

                refresh(filterPredicates);
            }
        });

        loader.addLoadListener(new DataLoadListener(devicesGrid));

        pagingToolBar = new PagingToolBar(DEVICE_PAGE_SIZE);
        pagingToolBar.bind(loader);
        pagingToolBar.enable();

        GridSelectionModel<GwtDevice> selectionModel = new GridSelectionModel<GwtDevice>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        devicesGrid.setSelectionModel(selectionModel);
        devicesGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtDevice>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDevice> se) {
                selectedDevice = se.getSelectedItem();
                if (selectedDevice != null) {
                    if (currentSession.hasDeviceUpdatePermission()) {
                        deleteDeviceButton.setEnabled(true);
                    }
                    if (currentSession.hasDeviceDeletePermission()) {
                        editDeviceButton.setEnabled(true);
                    }
                    devicesView.setDevice(selectedDevice);
                } else {
                    editDeviceButton.setEnabled(false);
                    deleteDeviceButton.setEnabled(false);
                }
            }
        });
    }

    // --------------------------------------------------------------------------------------
    //
    // Device List Management
    //
    // --------------------------------------------------------------------------------------

    public void refreshAll(GwtDeviceQueryPredicates predicates) {
        loader.setSortDir(SortDir.ASC);
        loader.setSortField("clientId");
        refresh(predicates);
    }

    public void refresh(GwtDeviceQueryPredicates predicates) {
        filterPredicates = predicates;
        loader.load();
        pagingToolBar.enable();
    }

    public void refresh() {
        loader.load();
        pagingToolBar.enable();
    }

    private void deleteDevice(GwtDevice device) {
        final GwtDevice toDeleteDevice = device;

        //
        // Getting XSRF token
        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

            @Override
            public void onFailure(Throwable ex) {
                FailureHandler.handle(ex);
            }

            @Override
            public void onSuccess(GwtXSRFToken token) {
                gwtDeviceService.deleteDevice(token,
                        currentSession.getSelectedAccount().getId(),
                        toDeleteDevice.getClientId(),
                        new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                FailureHandler.handle(caught);
                            }

                            public void onSuccess(Void arg0) {
                                refresh(filterPredicates);
                            }
                        });
            }
        });

    }

    private void export(String format) {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append("exporter_device?");
        sbUrl.append("format=")
                .append(format)
                .append("&scopeIdString=")
                .append(URL.encodeQueryString(currentSession.getSelectedAccount().getId()));

        //
        // Adding filtering parameter if specified
        Long tag = filterPredicates.getTag();
        if (tag != null) {
            sbUrl.append("&tag=")
                    .append(tag);
        }

        String clientId = filterPredicates.getClientId();
        if (clientId != null && !clientId.isEmpty()) {
            sbUrl.append("&clientId=")
                    .append(clientId);
        }

        String displayName = filterPredicates.getDisplayName();
        if (displayName != null && !displayName.isEmpty()) {
            sbUrl.append("&displayName=")
                    .append(displayName);
        }

        String serialNumber = filterPredicates.getSerialNumber();
        if (serialNumber != null && !serialNumber.isEmpty()) {
            sbUrl.append("&serialNumber=")
                    .append(serialNumber);
        }

        String deviceStatus = filterPredicates.getDeviceStatus();
        if (deviceStatus != null && !deviceStatus.isEmpty()) {
            sbUrl.append("&deviceStatus=")
                    .append(deviceStatus);
        }

        String deviceConnectionStatus = filterPredicates.getDeviceConnectionStatus();
        if (deviceConnectionStatus != null && !deviceConnectionStatus.isEmpty()) {
            sbUrl.append("&deviceConnectionStatus=")
                    .append(deviceConnectionStatus);
        }

        String esfVersion = filterPredicates.getIotFrameworkVersion();
        if (esfVersion != null && !esfVersion.isEmpty()) {
            sbUrl.append("&esfVersion=")
                    .append(esfVersion);
        }

        String applicationIdentifiers = filterPredicates.getApplicationIdentifiers();
        if (applicationIdentifiers != null && !applicationIdentifiers.isEmpty()) {
            sbUrl.append("&applicationIdentifiers=")
                    .append(applicationIdentifiers);
        }

        String imei = filterPredicates.getImei();
        if (imei != null && !imei.isEmpty()) {
            sbUrl.append("&imei=")
                    .append(imei);
        }

        String imsi = filterPredicates.getImsi();
        if (imsi != null && !imsi.isEmpty()) {
            sbUrl.append("&imsi=")
                    .append(imsi);
        }

        String iccid = filterPredicates.getIccid();
        if (iccid != null && !iccid.isEmpty()) {
            sbUrl.append("&iccid=")
                    .append(iccid);
        }

        String customAttribute1 = filterPredicates.getCustomAttribute1();
        if (customAttribute1 != null && !customAttribute1.isEmpty()) {
            sbUrl.append("&customAttribute1=")
                    .append(customAttribute1);
        }

        String customAttribute2 = filterPredicates.getCustomAttribute2();
        if (customAttribute2 != null && !customAttribute2.isEmpty()) {
            sbUrl.append("&customAttribute2=")
                    .append(customAttribute2);
        }

        Long signedCertificateId = filterPredicates.getSignedCertificateId();
        if (signedCertificateId != null) {
            sbUrl.append("&signedCertificateId=")
                    .append(signedCertificateId);
        }

        String sortOrder = filterPredicates.getSortOrder();
        if (sortOrder != null && !sortOrder.isEmpty()) {
            sbUrl.append("&sortOrder=")
                    .append(sortOrder);
        }

        String sortAttribute = filterPredicates.getSortAttribute();
        if (sortAttribute != null && !sortAttribute.isEmpty()) {
            sbUrl.append("&sortAttribute=")
                    .append(sortAttribute);
        }

        Window.open(sbUrl.toString(), "_blank", "location=no");
    }

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        private Grid<GwtDevice> devicesGrid;
        private GwtDevice selectedDevice;

        public DataLoadListener(Grid<GwtDevice> devicesGrid) {
            this.devicesGrid = devicesGrid;
            this.selectedDevice = null;
        }

        public void loaderBeforeLoad(LoadEvent le) {
            this.selectedDevice = this.devicesGrid.getSelectionModel().getSelectedItem();
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

            if (this.selectedDevice != null) {
                ListStore<GwtDevice> store = this.devicesGrid.getStore();
                GwtDevice modelDevice = store.findModel(this.selectedDevice.getScopeId() + ":" + this.selectedDevice.getClientId());
                if (modelDevice != null) {
                    this.devicesGrid.getSelectionModel().select(modelDevice, false);
                    this.devicesGrid.getView().focusRow(store.indexOf(modelDevice));
                } else {
                    devicesView.setDevice(null);
                }
            }
        }
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------

    public void onUnload() {
        super.onUnload();
    }
}
