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
package org.eclipse.kapua.app.console.client.device.management.packages;

import java.util.Arrays;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.misc.color.Color;
import org.eclipse.kapua.app.console.client.ui.tab.TabItem;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtBundleInfo;
import org.eclipse.kapua.app.console.shared.model.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceManagementServiceAsync;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceTabPackagesInstalled extends TabItem {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final GwtDeviceManagementServiceAsync gwtDeviceManagementService = GWT.create(GwtDeviceManagementService.class);

    private boolean m_initialized = false;
    private boolean m_dirty = true;

    private DeviceTabPackages m_rootTabPanel;
    private TreeGrid<ModelData> m_treeGrid;
    private TreeStore<ModelData> m_treeStore = new TreeStore<ModelData>();

    public DeviceTabPackagesInstalled(DeviceTabPackages rootTabPanel) {
        super(MSGS.deviceInstallTabInstalled(), null);

        KapuaIcon icon = new KapuaIcon(IconSet.INBOX);
        icon.setColor(Color.GREEN);
        setIcon(icon);

        m_rootTabPanel = rootTabPanel;
    }

    public GwtDeploymentPackage getSelectedDeploymentPackage() {
        ModelData selectedItem = m_treeGrid.getSelectionModel().getSelectedItem();

        if (selectedItem instanceof GwtDeploymentPackage) {
            return (GwtDeploymentPackage) selectedItem;
        }
        return null;
    }

    private GwtDevice getSelectedDevice() {
        return m_rootTabPanel.getSelectedDevice();
    }

    public void setDirty(boolean isDirty) {
        m_dirty = isDirty;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        ColumnConfig name = new ColumnConfig("name", "Name", 100);
        name.setRenderer(new TreeGridCellRenderer<ModelData>());

        ColumnConfig version = new ColumnConfig("version", "Version", 150);
        version.setSortable(false);

        ColumnModel cm = new ColumnModel(Arrays.asList(name, version));

        m_treeGrid = new TreeGrid<ModelData>(m_treeStore, cm);
        m_treeGrid.setBorders(false);
        m_treeGrid.setLoadMask(true);
        m_treeGrid.setAutoExpandColumn("name");
        m_treeGrid.setTrackMouseOver(false);
        m_treeGrid.getAriaSupport().setLabelledBy(getHeader().getId() + "-label");
        m_treeGrid.getView().setAutoFill(true);
        m_treeGrid.getView().setEmptyText(MSGS.deviceNoDeviceSelectedOrOffline());

        m_treeGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                ModelData selectedItem = se.getSelectedItem();

                // Check if it's a package or a bundle
                if (selectedItem instanceof GwtDeploymentPackage) {
                    m_rootTabPanel.getUninstallButton().enable();
                } else {
                    m_rootTabPanel.getUninstallButton().disable();
                }
            }
        });

        add(m_treeGrid);

        m_initialized = true;
    }

    public void refresh() {

        if (m_dirty && m_initialized) {

            GwtDevice selectedDevice = getSelectedDevice();
            if (selectedDevice == null || !selectedDevice.isOnline()) {
                m_treeStore.removeAll();
                m_treeGrid.unmask();
                m_treeGrid.getView().setEmptyText(MSGS.deviceNoDeviceSelectedOrOffline());
            } else {
                m_treeGrid.mask(MSGS.loading());

                gwtDeviceManagementService.findDevicePackages(selectedDevice.getScopeId(), selectedDevice.getId(), new AsyncCallback<List<GwtDeploymentPackage>>() {

                    @Override
                    public void onSuccess(List<GwtDeploymentPackage> packages) {
                        m_treeStore.removeAll();
                        if (packages != null && !packages.isEmpty()) {
                            for (GwtDeploymentPackage pkg : packages) {
                                m_treeStore.add(pkg, false);

                                if (pkg.getBundleInfos() != null) {
                                    for (GwtBundleInfo bundle : pkg.getBundleInfos()) {
                                        m_treeStore.add(pkg, bundle, false);
                                    }
                                }
                            }
                        } else {
                            m_rootTabPanel.getUninstallButton().disable();
                            m_treeGrid.getView().setEmptyText(MSGS.deviceNoPackagesInstalled());
                            m_treeGrid.getView().refresh(false);
                        }

                        m_treeGrid.unmask();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        FailureHandler.handle(caught);
                        m_treeGrid.unmask();
                    }
                });
            }

            m_dirty = false;
        }
    }
}
