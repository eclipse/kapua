/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.keystore;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.SplitButton;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaMenuItem;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.certificate.shared.service.GwtCertificateInfoService;
import org.eclipse.kapua.app.console.module.certificate.shared.service.GwtCertificateInfoServiceAsync;
import org.eclipse.kapua.app.console.module.device.client.device.DeviceView;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.button.KeystoreItemCsrButton;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.button.KeystoreItemDeleteButton;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.button.KeystoreItemDetailsButton;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog.KeystoreItemAddCertificateInfoDialog;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog.KeystoreItemAddCertificateRawDialog;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog.KeystoreItemAddCsrDialog;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog.KeystoreItemAddKeypairDialog;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog.KeystoreItemDeleteDialog;
import org.eclipse.kapua.app.console.module.device.client.device.keystore.dialog.KeystoreItemDetailsDialog;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreItem;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceManagementSessionPermission;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceKeystoreManagementServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceTabKeystore extends KapuaTabItem<GwtDevice> {

    private static final ConsoleMessages CONSOLE_MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceKeystoreManagementServiceAsync GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceKeystoreManagementService.class);
    private static final GwtCertificateInfoServiceAsync GWT_CERTIFICATE_INFO_SERVICE = GWT.create(GwtCertificateInfoService.class);

    private Grid<GwtDeviceKeystoreItem> grid;

    SplitButton addItemButton;
    KapuaButton detailsButton;
    KapuaButton csrButton;
    RefreshButton refreshButton;
    KapuaButton deleteButton;

    private boolean initialized;

    public DeviceTabKeystore(GwtSession currentSession, DeviceView deviceTabs) {
        super(currentSession, "Keystore", new KapuaIcon(IconSet.KEY));

        setEnabled(false);
        getHeader().setVisible(false);
    }

    @Override
    public void setEntity(GwtDevice gwtDevice) {
        super.setEntity(gwtDevice);

        if (gwtDevice != null) {
            setEnabled(gwtDevice.isOnline() && currentSession.hasPermission(DeviceManagementSessionPermission.read()));
            getHeader().setVisible(gwtDevice.hasApplication(GwtDevice.GwtDeviceApplication.APP_KEYS_V1));
        } else {
            setEnabled(false);
            getHeader().setVisible(false);
        }

        refresh();
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(true);

        // Main Panel
        final ContentPanel deviceKeystorePanel = new ContentPanel();
        deviceKeystorePanel.setBorders(false);
        deviceKeystorePanel.setBodyBorder(false);
        deviceKeystorePanel.setHeaderVisible(false);
        deviceKeystorePanel.setLayout(new FitLayout());
        add(deviceKeystorePanel);

        // Main grid
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("keystoreId");
        column.setHeader("Keystore Id");
        columns.add(column);

        column = new ColumnConfig();
        column.setId("alias");
        column.setHeader("Alias");
        columns.add(column);

        column = new ColumnConfig();
        column.setId("itemType");
        column.setHeader("Type");
        columns.add(column);

        column = new ColumnConfig();
        column.setId("algorithm");
        column.setHeader("Algorithm");
        columns.add(column);

        column = new ColumnConfig();
        column.setId("size");
        column.setHeader("Size");
        columns.add(column);

        grid = new Grid<GwtDeviceKeystoreItem>(new ListStore<GwtDeviceKeystoreItem>(), new ColumnModel(columns));
        grid.setBorders(false);
        grid.setStateful(false);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(false);
        grid.disableTextSelection(false);
        grid.setAutoExpandColumn("alias");
        grid.getView().setAutoFill(true);
        grid.getView().setEmptyText("No items found...");
        deviceKeystorePanel.add(grid);

        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtDeviceKeystoreItem>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDeviceKeystoreItem> selectionChangedEvent) {
                csrButton.setEnabled(grid.getSelectionModel().getSelectedItem() != null && grid.getSelectionModel().getSelectedItem().getItemType().equals("PRIVATE_KEY"));
                detailsButton.setEnabled(grid.getSelectionModel().getSelectedItem() != null);
                deleteButton.setEnabled(grid.getSelectionModel().getSelectedItem() != null);
            }
        });

        //
        // Add Menu
        addItemButton = new SplitButton("Add", new KapuaIcon(IconSet.PLUS));
        addItemButton.setEnabled(getSelectedEntity() != null);

        Menu addItemMenu = new Menu();
        addItemButton.setMenu(addItemMenu);

        //
        // Add Certificate Raw Menu Item
        KapuaMenuItem addCertificateRawMenuItem = new KapuaMenuItem("Certificate Raw", IconSet.EDIT, new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                KeystoreItemAddCertificateRawDialog addCertificateRawDialog = new KeystoreItemAddCertificateRawDialog(getSelectedEntity());

                addCertificateRawDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                addCertificateRawDialog.show();
            }
        });
        addItemMenu.add(addCertificateRawMenuItem);

        //
        // Add Certificate Raw Menu Item
        final KapuaMenuItem addCertificateInfoMenuItem = new KapuaMenuItem("Certificate", IconSet.CERTIFICATE, new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                KeystoreItemAddCertificateInfoDialog addCertificateInfoDialog = new KeystoreItemAddCertificateInfoDialog(getSelectedEntity());

                addCertificateInfoDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                addCertificateInfoDialog.show();
            }
        });
        addItemMenu.add(addCertificateInfoMenuItem);

        GWT_CERTIFICATE_INFO_SERVICE.isFindSupported(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                addCertificateInfoMenuItem.hide();
            }

            @Override
            public void onSuccess(Boolean result) {
                addCertificateInfoMenuItem.setVisible(result);
            }
        });

        //
        // Add Keypair Menu Item
        KapuaMenuItem addKeypairMenuItem = new KapuaMenuItem("Keypair", IconSet.KEY, new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                KeystoreItemAddKeypairDialog addKeypairDialog = new KeystoreItemAddKeypairDialog(getSelectedEntity());

                addKeypairDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                addKeypairDialog.show();
            }
        });
        addItemMenu.add(addKeypairMenuItem);

        //
        // Details button
        detailsButton = new KeystoreItemDetailsButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                KeystoreItemDetailsDialog detailsDialog = new KeystoreItemDetailsDialog(getSelectedEntity(), grid.getSelectionModel().getSelectedItem());
                detailsDialog.show();
            }
        });
        detailsButton.disable();

        //
        // CSR Button
        csrButton = new KeystoreItemCsrButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                KeystoreItemAddCsrDialog requestCsrDialog = new KeystoreItemAddCsrDialog(getSelectedEntity(), grid.getSelectionModel().getSelectedItem());

                requestCsrDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                requestCsrDialog.show();
            }
        });
        csrButton.disable();

        //
        // Refresh button
        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                refreshButton.disable();
                setDirty(true);
                refresh();
            }
        });
        refreshButton.setEnabled(getSelectedEntity() != null);

        //
        // Delete button
        deleteButton = new KeystoreItemDeleteButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                KeystoreItemDeleteDialog deleteDialog = new KeystoreItemDeleteDialog(getSelectedEntity(), grid.getSelectionModel().getSelectedItem());

                deleteDialog.addListener(Events.Hide, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent baseEvent) {
                        setDirty(true);
                        refresh();
                    }
                });

                deleteDialog.show();
            }
        });
        deleteButton.disable();

        //
        // Toolbar
        ToolBar toolBar = new ToolBar();
        toolBar.setBorders(true);
        toolBar.add(addItemButton);
        toolBar.add(detailsButton);
        toolBar.add(csrButton);
        toolBar.add(refreshButton);
        toolBar.add(deleteButton);
        deviceKeystorePanel.setTopComponent(toolBar);

        initialized = true;

        //
        // Init
        if (getSelectedEntity() != null) {
            setDirty(true);
            refresh();
        }
    }

    //
    // REFRESHER
    //
    @Override
    public void doRefresh() {
        if (initialized && getSelectedEntity() != null) {

            // Clean and mask grid before loading
            grid.mask("Loading keystore items...");
            grid.getStore().removeAll();

            refreshButton.disable();

            GWT_DEVICE_KEYSTORE_MANAGEMENT_SERVICE.getKeystoreItems(getSelectedEntity().getScopeId(), getSelectedEntity().getId(), new AsyncCallback<List<GwtDeviceKeystoreItem>>() {

                @Override
                public void onFailure(Throwable caught) {
                    ConsoleInfo.display(CONSOLE_MSGS.popupError(), DEVICE_MSGS.deviceConnectionError());
                    refreshButton.enable();
                    grid.unmask();

                    refreshButton.enable();
                }

                @Override
                public void onSuccess(List<GwtDeviceKeystoreItem> result) {
                    refreshButton.enable();

                    grid.getStore().add(result);
                    grid.unmask();

                    refreshButton.enable();

                    layout();
                }
            });
        }
    }
}
