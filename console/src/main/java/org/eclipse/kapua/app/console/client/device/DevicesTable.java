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

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.button.AddButton;
import org.eclipse.kapua.app.console.client.ui.button.Button;
import org.eclipse.kapua.app.console.client.ui.button.DeleteButton;
import org.eclipse.kapua.app.console.client.ui.button.ExportButton;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.ui.misc.color.Color;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.ImageUtils;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.client.util.UserAgentUtils;
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
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DevicesTable extends LayoutContainer {

    private final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    private static final int DEVICE_PAGE_SIZE = 250;

    private GwtSession m_currentSession;
    private GwtDevice m_selectedDevice;

    private DevicesView m_devicesView;
    private ContentPanel m_tableContainer;
    private ToolBar m_devicesToolBar;

    private Button m_addDeviceButton;

    private Button m_refreshButton;
    private boolean refreshProcess;
    private Button m_deleteDeviceButton;
    private SplitButton m_export;

    private ToggleButton m_toggleFilter;

    private ContentPanel m_filterPanel;

    private Grid<GwtDevice> m_devicesGrid;
    private PagingToolBar m_pagingToolBar;
    private BasePagingLoader<PagingLoadResult<GwtDevice>> m_loader;
    private GwtDeviceQueryPredicates m_filterPredicates;

    public DevicesTable(DevicesView deviceView,
            GwtSession currentSession,
            ContentPanel filterPanel) {
        m_devicesView = deviceView;
        m_currentSession = currentSession;
        m_filterPredicates = new GwtDeviceQueryPredicates();
        m_filterPanel = filterPanel;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        // FitLayout that expands to the whole screen
        setLayout(new FitLayout());
        setBorders(false);

        // init tab content
        initDevicesTable();
        add(m_tableContainer);
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

        m_tableContainer = new ContentPanel();
        m_tableContainer.setBorders(false);
        m_tableContainer.setBodyBorder(false);
        m_tableContainer.setHeaderVisible(false);
        m_tableContainer.setScrollMode(Scroll.AUTO);
        m_tableContainer.setLayout(new FitLayout());
        m_tableContainer.setTopComponent(m_devicesToolBar);
        m_tableContainer.add(m_devicesGrid);
        m_tableContainer.setBottomComponent(m_pagingToolBar);
    }

    private void initDevicesToolBar() {

        m_devicesToolBar = new ToolBar();

        // Edit Device Button
        if (m_currentSession.hasDeviceCreatePermission()) {
            m_addDeviceButton = new AddButton(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    DeviceForm deviceForm = new DeviceForm(m_currentSession);
                    deviceForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                        public void handleEvent(ComponentEvent be) {
                            refresh(m_filterPredicates);
                        }
                    });
                    deviceForm.show();
                }

            });
            m_devicesToolBar.add(m_addDeviceButton);
            m_devicesToolBar.add(new SeparatorToolItem());
        }

        //
        // Refresh Button
        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshProcess = true;

                    refresh(m_filterPredicates);

                    refreshProcess = false;
                }
            }
        });
        m_refreshButton.setEnabled(true);
        m_devicesToolBar.add(m_refreshButton);
        m_devicesToolBar.add(new SeparatorToolItem());

        //
        // Export
        Menu menu = new Menu();
        menu.add(new MenuItem(MSGS.exportToExcel(), AbstractImagePrototype.create(Resources.INSTANCE.exportExcel()),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("xls");
                    }
                }));
        menu.add(new MenuItem(MSGS.exportToCSV(), AbstractImagePrototype.create(Resources.INSTANCE.exportCSV()),
                new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        export("csv");
                    }
                }));

        m_export = new ExportButton();
        m_export.setMenu(menu);

        m_devicesToolBar.add(m_export);
        m_devicesToolBar.add(new SeparatorToolItem());

        //
        // Delete Device Button
        m_deleteDeviceButton = new DeleteButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_devicesGrid != null) {
                    final GwtDevice gwtDevice = m_devicesGrid.getSelectionModel().getSelectedItem();
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
        m_deleteDeviceButton.setEnabled(false);
        m_devicesToolBar.add(m_deleteDeviceButton);
        m_devicesToolBar.add(new FillToolItem());

        //
        // Live Button
        m_toggleFilter = new ToggleButton(MSGS.deviceTableToolbarCloseFilter(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_toggleFilter.isPressed()) {
                    m_filterPanel.show();
                    m_toggleFilter.setText(MSGS.deviceTableToolbarCloseFilter());
                } else {
                    m_filterPanel.hide();
                    m_toggleFilter.setText(MSGS.deviceTableToolbarOpenFilter());
                }
            }
        });
        m_toggleFilter.toggle(true);
        m_devicesToolBar.add(m_toggleFilter);
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

        //
        // Device Management Certificate
        column = new ColumnConfig("Device Management Certificate Status", "DM", 50);
        column.setAlignment(HorizontalAlignment.CENTER);
        GridCellRenderer<GwtDevice> setDmStatusIcon = new GridCellRenderer<GwtDevice>() {

            public String render(GwtDevice gwtDevice, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtDevice> deviceList, Grid<GwtDevice> grid) {
                if (gwtDevice.getSignedCertificateId() == null) {
                    // Device Management Communication is not signed
                    return ImageUtils.toHTML(Resources.INSTANCE.dmUnlock16(), MSGS.deviceTableCertificateDMTooltipStatusNotSigned(), "14");
                } else {
                    // Device Management Communication is signed
                    return ImageUtils.toHTML(Resources.INSTANCE.lockGreen16(), MSGS.deviceTableCertificateDMTooltipStatusSigned(), "14");
                }
            }
        };
        column.setRenderer(setDmStatusIcon);
        column.setAlignment(HorizontalAlignment.CENTER);
        column.setSortable(false);
        configs.add(column);

        column = new ColumnConfig("applicationIdentifiers", MSGS.deviceTableApplications(), 100);
        column.setSortable(false);
        column.setHidden(false);
        configs.add(column);

        column = new ColumnConfig("esfKuraVersion", MSGS.deviceTableEsfKuraVersion(), 80);
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
                        m_currentSession.getSelectedAccount().getId(),
                        m_filterPredicates,
                        callback);
            }
        };
        m_loader = new BasePagingLoader<PagingLoadResult<GwtDevice>>(proxy);
        m_loader.setSortDir(SortDir.ASC);
        m_loader.setSortField("clientId");
        m_loader.setRemoteSort(true);

        SwappableListStore<GwtDevice> store = new SwappableListStore<GwtDevice>(m_loader);
        store.setKeyProvider(new ModelKeyProvider<GwtDevice>() {

            public String getKey(GwtDevice device) {
                return device.getScopeId() + ":" + device.getClientId();
            }
        });

        m_devicesGrid = new Grid<GwtDevice>(store, new ColumnModel(configs));
        m_devicesGrid.setBorders(false);
        m_devicesGrid.setStateful(false);
        m_devicesGrid.setLoadMask(true);
        m_devicesGrid.setStripeRows(true);
        m_devicesGrid.setAutoExpandColumn("displayName");
        m_devicesGrid.getView().setAutoFill(true);
        m_devicesGrid.getView().setEmptyText(MSGS.deviceTableNoDevices());
        m_devicesGrid.disableTextSelection(false);
        m_devicesGrid.addListener(Events.HeaderClick, new Listener<GridEvent<GwtDevice>>() {

            @Override
            public void handleEvent(GridEvent<GwtDevice> be) {

                if (be.getColIndex() == 1) {

                    if (m_filterPredicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.CLIENT_ID.name())) {
                        if (m_filterPredicates.getSortOrderEnum().equals(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING)) {
                            m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.DESCENDING.name());

                        } else {
                            m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                        }
                    } else {
                        m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                    }
                    m_filterPredicates.setSortAttribute(GwtDeviceQueryPredicates.GwtSortAttribute.CLIENT_ID.name());

                } else if (be.getColIndex() == 2) {
                    if (m_filterPredicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.DISPLAY_NAME.name())) {
                        if (m_filterPredicates.getSortOrderEnum().equals(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING)) {
                            m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.DESCENDING.name());
                        } else {
                            m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                        }
                    } else {
                        m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                    }
                    m_filterPredicates.setSortAttribute(GwtDeviceQueryPredicates.GwtSortAttribute.DISPLAY_NAME.name());

                } else if (be.getColIndex() == 3) {
                    if (m_filterPredicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.LAST_EVENT_ON.name())) {
                        if (m_filterPredicates.getSortOrderEnum().equals(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING)) {
                            m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.DESCENDING.name());
                        } else {
                            m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                        }
                    } else {
                        m_filterPredicates.setSortOrder(GwtDeviceQueryPredicates.GwtSortOrder.ASCENDING.name());
                    }
                    m_filterPredicates.setSortAttribute(GwtDeviceQueryPredicates.GwtSortAttribute.LAST_EVENT_ON.name());

                } else {
                    return;
                }

                refresh(m_filterPredicates);
            }
        });

        m_loader.addLoadListener(new DataLoadListener(m_devicesGrid));

        m_pagingToolBar = new PagingToolBar(DEVICE_PAGE_SIZE);
        m_pagingToolBar.bind(m_loader);
        m_pagingToolBar.enable();

        GridSelectionModel<GwtDevice> selectionModel = new GridSelectionModel<GwtDevice>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        m_devicesGrid.setSelectionModel(selectionModel);
        m_devicesGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtDevice>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDevice> se) {
                m_selectedDevice = se.getSelectedItem();
                if (m_selectedDevice != null) {
                    m_deleteDeviceButton.setEnabled(true);
                    m_devicesView.setDevice(m_selectedDevice);
                } else {
                    // m_editDeviceButton.setEnabled(false);
                    m_deleteDeviceButton.setEnabled(false);
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
        m_loader.setSortDir(SortDir.ASC);
        m_loader.setSortField("clientId");
        refresh(predicates);
    }

    public void refresh(GwtDeviceQueryPredicates predicates) {
        m_filterPredicates = predicates;
        m_loader.load();
        m_pagingToolBar.enable();
    }

    public void refresh() {
        m_loader.load();
        m_pagingToolBar.enable();
    };

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
                        m_currentSession.getSelectedAccount().getId(),
                        toDeleteDevice.getClientId(),
                        new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                FailureHandler.handle(caught);
                            }

                            public void onSuccess(Void arg0) {
                                refresh(m_filterPredicates);
                            }
                        });
            }
        });

    }

    private void export(String format) {
        StringBuilder sbUrl = new StringBuilder();
        if (UserAgentUtils.isSafari() || UserAgentUtils.isChrome()) {
            sbUrl.append("console/exporter_device?");
        } else {
            sbUrl.append("exporter_device?");
        }

        sbUrl.append("format=")
                .append(format)
                .append("&account=")
                .append(URL.encodeQueryString(m_currentSession.getSelectedAccount().getName()));

        //
        // Adding filtering parameter if specified
        Long tag = m_filterPredicates.getTag();
        if (tag != null) {
            sbUrl.append("&tag=")
                    .append(tag);
        }

        String clientId = m_filterPredicates.getClientId();
        if (clientId != null && !clientId.isEmpty()) {
            sbUrl.append("&clientId=")
                    .append(clientId);
        }

        String displayName = m_filterPredicates.getDisplayName();
        if (displayName != null && !displayName.isEmpty()) {
            sbUrl.append("&displayName=")
                    .append(displayName);
        }

        String serialNumber = m_filterPredicates.getSerialNumber();
        if (serialNumber != null && !serialNumber.isEmpty()) {
            sbUrl.append("&serialNumber=")
                    .append(serialNumber);
        }

        String deviceStatus = m_filterPredicates.getDeviceStatus();
        if (deviceStatus != null && !deviceStatus.isEmpty()) {
            sbUrl.append("&deviceStatus=")
                    .append(deviceStatus);
        }

        String deviceConnectionStatus = m_filterPredicates.getDeviceConnectionStatus();
        if (deviceConnectionStatus != null && !deviceConnectionStatus.isEmpty()) {
            sbUrl.append("&deviceConnectionStatus=")
                    .append(deviceConnectionStatus);
        }

        String esfVersion = m_filterPredicates.getEsfVersion();
        if (esfVersion != null && !esfVersion.isEmpty()) {
            sbUrl.append("&esfVersion=")
                    .append(esfVersion);
        }

        String applicationIdentifiers = m_filterPredicates.getApplicationIdentifiers();
        if (applicationIdentifiers != null && !applicationIdentifiers.isEmpty()) {
            sbUrl.append("&applicationIdentifiers=")
                    .append(applicationIdentifiers);
        }

        String imei = m_filterPredicates.getImei();
        if (imei != null && !imei.isEmpty()) {
            sbUrl.append("&imei=")
                    .append(imei);
        }

        String imsi = m_filterPredicates.getImsi();
        if (imsi != null && !imsi.isEmpty()) {
            sbUrl.append("&imsi=")
                    .append(imsi);
        }

        String iccid = m_filterPredicates.getIccid();
        if (iccid != null && !iccid.isEmpty()) {
            sbUrl.append("&iccid=")
                    .append(iccid);
        }

        String customAttribute1 = m_filterPredicates.getCustomAttribute1();
        if (customAttribute1 != null && !customAttribute1.isEmpty()) {
            sbUrl.append("&customAttribute1=")
                    .append(customAttribute1);
        }

        String customAttribute2 = m_filterPredicates.getCustomAttribute2();
        if (customAttribute2 != null && !customAttribute2.isEmpty()) {
            sbUrl.append("&customAttribute2=")
                    .append(customAttribute2);
        }

        Long signedCertificateId = m_filterPredicates.getSignedCertificateId();
        if (signedCertificateId != null) {
            sbUrl.append("&signedCertificateId=")
                    .append(signedCertificateId);
        }

        String sortOrder = m_filterPredicates.getSortOrder();
        if (sortOrder != null && !sortOrder.isEmpty()) {
            sbUrl.append("&sortOrder=")
                    .append(sortOrder);
        }

        String sortAttribute = m_filterPredicates.getSortAttribute();
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

        private Grid<GwtDevice> m_devicesGrid;
        private GwtDevice m_selectedDevice;

        public DataLoadListener(Grid<GwtDevice> devicesGrid) {
            m_devicesGrid = devicesGrid;
            m_selectedDevice = null;
        }

        public void loaderBeforeLoad(LoadEvent le) {
            m_selectedDevice = m_devicesGrid.getSelectionModel().getSelectedItem();
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

            if (m_selectedDevice != null) {
                ListStore<GwtDevice> store = m_devicesGrid.getStore();
                GwtDevice modelDevice = store.findModel(m_selectedDevice.getScopeId() + ":" + m_selectedDevice.getClientId());
                if (modelDevice != null) {
                    m_devicesGrid.getSelectionModel().select(modelDevice, false);
                    m_devicesGrid.getView().focusRow(store.indexOf(modelDevice));
                } else {
                    m_devicesView.setDevice(null);
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
