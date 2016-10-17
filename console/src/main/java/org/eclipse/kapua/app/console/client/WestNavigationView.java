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
package org.eclipse.kapua.app.console.client;

import java.util.Arrays;

import org.eclipse.kapua.app.console.client.account.AccountDetailsView;
import org.eclipse.kapua.app.console.client.account.AccountView;
import org.eclipse.kapua.app.console.client.device.DevicesView;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.overview.DashboardView;
import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.client.user.UserView;
import org.eclipse.kapua.app.console.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

public class WestNavigationView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private LayoutContainer m_centerPanel;
    private ContentPanel m_accountPanel;
    private ContentPanel m_accordionPanel;
    private ContentPanel m_managePanel;
    private AccordionLayout m_accordionLayout;

    private TreeStore<ModelData> m_accountStore;
    private TreeGrid<ModelData> m_accountTree;
    private TreeStore<ModelData> m_manageStore;
    private TreeGrid<ModelData> m_manageTree;

    private boolean dashboardSelected;
    private Label imgRefreshLabel;
    private DashboardView m_dashboardView;

    private GwtSession m_currentSession;

    public WestNavigationView(GwtSession currentSession, LayoutContainer center) {
        m_centerPanel = center;
        m_currentSession = currentSession;

        ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeaderVisible(false);
        m_dashboardView = new DashboardView(m_currentSession);
        panel.add(m_dashboardView);
        m_centerPanel.add(panel);
        m_centerPanel.layout();
        dashboardSelected = true;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        //
        // accordion
        m_accordionLayout = new AccordionLayout();
        m_accordionLayout.setFill(true);
        m_accordionPanel = new ContentPanel();
        m_accordionPanel.setBorders(false);
        m_accordionPanel.setBodyBorder(false);
        m_accordionPanel.setLayout(m_accordionLayout);
        m_accordionPanel.setHeaderVisible(false);
        m_accordionPanel.setId("accordion-panel");
        add(m_accordionPanel);

        //
        // Top managing panel
        m_accountPanel = new ContentPanel();
        m_accountPanel.setBorders(false);
        m_accountPanel.setBodyBorder(true);
        m_accountPanel.setAnimCollapse(true);
        m_accountPanel.setHeaderVisible(false);
        m_accountPanel.setScrollMode(Scroll.AUTOY);

        //
        // Bottom manage panel
        m_managePanel = new ContentPanel();
        m_managePanel.setBorders(false);
        m_managePanel.setBodyBorder(false);
        m_managePanel.setHeading(MSGS.manageHeading());
        m_managePanel.setHeaderVisible(true);

        m_accountStore = new TreeStore<ModelData>();
        m_manageStore = new TreeStore<ModelData>();

        //
        // Adding item to stores
        //
        addMenuItems();

        ColumnConfig name = new ColumnConfig("name", "Name", 100);
        name.setRenderer(new WidgetTreeGridCellRenderer<ModelData>() {

            @Override
            public Widget getWidget(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
                Label label = new Label((String) model.get(property));
                label.setStyleAttribute("padding-left", "5px");

                if (((String) model.get(property)).equals("Dashboard")) {

                    // Adding refresh button
                    label.setStyleAttribute("padding-right", "50px");
                    label.setStyleAttribute("background-color", "transparent");

                    ContentPanel dashboardLabelPanel = new ContentPanel();
                    dashboardLabelPanel.setHeaderVisible(false);
                    dashboardLabelPanel.setBorders(false);
                    dashboardLabelPanel.setBodyBorder(false);
                    dashboardLabelPanel.setLayout(new FitLayout());
                    dashboardLabelPanel.setStyleAttribute("background-color", "transparent");

                    dashboardLabelPanel.add(label);

                    imgRefreshLabel = new Label("<image src=\"eclipse/org/eclipse/kapua/app/console/icon/refresh.png\" "
                            + "width=\"15\" height=\"15\" "
                            + "style=\"vertical-align: middle\" title=\"" + MSGS.refreshButton() + "\"/>");

                    dashboardLabelPanel.add(imgRefreshLabel);
                    dashboardLabelPanel.setBodyStyle("background-color:transparent");

                    imgRefreshLabel.addListener(Events.OnClick, new Listener<BaseEvent>() {

                        @Override
                        public void handleEvent(BaseEvent be) {
                            if (dashboardSelected) {
                                m_dashboardView.refresh();
                            }
                        }
                    });

                    return dashboardLabelPanel;
                }
                return label;
            }
        });

        ColumnModel cm = new ColumnModel(Arrays.asList(name));

        m_accountTree = new TreeGrid<ModelData>(m_accountStore, cm);
        m_accountTree.setBorders(false);
        m_accountTree.setHideHeaders(true);
        m_accountTree.setAutoExpandColumn("name");
        m_accountTree.getTreeView().setRowHeight(36);
        m_accountTree.getTreeView().setForceFit(true);
        m_accountTree.setIconProvider(new ModelIconProvider<ModelData>() {

            public AbstractImagePrototype getIcon(ModelData model) {
                if (model.get("icon") != null) {
                    ImageResource ir = (ImageResource) model.get("icon");
                    return AbstractImagePrototype.create(ir);
                } else {
                    return null;
                }
            }
        });

        m_accountTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        m_accountTree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                ModelData selected = se.getSelectedItem();
                if (selected == null)
                    return;

                if (dashboardSelected && ((String) selected.get("id")).equals("dashboard")) {
                    return;
                }

                m_manageTree.getSelectionModel().deselectAll();

                m_centerPanel.removeAll();

                ContentPanel panel = new ContentPanel(new FitLayout());
                panel.setBorders(false);
                panel.setBodyBorder(false);

                String selectedId = (String) selected.get("id");
                if ("dashboard".equals(selectedId)) {

                    panel.setHeaderVisible(false);
                    m_dashboardView = new DashboardView(m_currentSession);
                    panel.add(m_dashboardView);
                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    dashboardSelected = true;

                } else if ("devices".equals(selectedId)) {

                    panel.setHeaderVisible(false);
                    panel.add(new DevicesView(m_currentSession));
                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    dashboardSelected = false;

                } else if ("user".equals(selectedId)) {

                    panel.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.users16()));
                    panel.setHeading(MSGS.users());
                    UserView userView = new UserView(m_currentSession);
                    userView.setAccount(m_currentSession.getSelectedAccount());
                    panel.add(userView);
                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    userView.refresh();
                    dashboardSelected = false;

                } else if ("mysettings".equals(selectedId)) {

                    panel.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.settings()));
                    panel.setHeading(MSGS.settings());
                    AccountDetailsView settingView = new AccountDetailsView(null, m_currentSession);
                    settingView.setAccount(m_currentSession.getSelectedAccount());
                    panel.add(settingView);
                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    settingView.refresh();
                }

                imgRefreshLabel.setVisible(dashboardSelected);
            }
        });

        ColumnConfig name1 = new ColumnConfig("name", "Name", 100);
        name1.setRenderer(new WidgetTreeGridCellRenderer<ModelData>() {

            @Override
            public Widget getWidget(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
                Label label = new Label((String) model.get(property));
                label.setStyleAttribute("padding-left", "5px");
                return label;
            }
        });
        ColumnModel cm1 = new ColumnModel(Arrays.asList(name1));

        m_manageTree = new TreeGrid<ModelData>(m_manageStore, cm1);
        m_manageTree.setBorders(false);
        m_manageTree.setHideHeaders(true);
        m_manageTree.setAutoExpandColumn("name");
        m_manageTree.getTreeView().setRowHeight(36);
        m_manageTree.getTreeView().setForceFit(true);
        m_manageTree.setIconProvider(new ModelIconProvider<ModelData>() {

            public AbstractImagePrototype getIcon(ModelData model) {
                if (model.get("icon") != null) {
                    ImageResource ir = (ImageResource) model.get("icon");
                    return AbstractImagePrototype.create(ir);
                } else {
                    return null;
                }
            }
        });

        m_manageTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        m_manageTree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {

                ModelData selected = se.getSelectedItem();
                if (selected == null)
                    return;

                m_accountTree.getSelectionModel().deselectAll();

                m_centerPanel.removeAll();
                ContentPanel panel = new ContentPanel(new FitLayout());
                panel.setBorders(false);
                panel.setBodyBorder(false);

                String selectedId = (String) selected.get("id");
                if ("childaccounts".equals(selectedId)) {
                    panel.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.childAccounts16()));
                    panel.setHeading(MSGS.childaccounts());
                    AccountView accountView = new AccountView(m_currentSession);
                    panel.add(accountView);
                    dashboardSelected = false;
                }
                imgRefreshLabel.setVisible(dashboardSelected);

                m_centerPanel.add(panel);
                m_centerPanel.layout();
            }
        });

        m_accountPanel.add(m_accountTree);
        m_accountPanel.add(m_managePanel);
        m_accountPanel.add(m_manageTree);

        m_accountTree.getSelectionModel().select(0, false);

        m_accordionPanel.add(m_accountPanel);
    }

    public void addMenuItems() {
        ModelData selectedAccountItem = null;
        ModelData selectedManageItem = null;

        if (m_accountTree != null && m_manageTree != null) {
            selectedAccountItem = m_accountTree.getSelectionModel().getSelectedItem();
            selectedManageItem = m_manageTree.getSelectionModel().getSelectedItem();
        }

        m_accountStore.removeAll();
        m_manageStore.removeAll();

        GwtAccount selectedAccount = m_currentSession.getSelectedAccount();

        if (selectedAccount != null) {

            m_accountStore.add(newItem("dashboard", MSGS.dashboard(), Resources.INSTANCE.dashboard32()), false);

            if (m_currentSession.hasDeviceReadPermission()) {
                m_accountStore.add(newItem("devices", MSGS.devices(), Resources.INSTANCE.devices32()), false);
            }
            //
            // if (m_currentSession.hasDataReadPermission()) {
            // m_accountStore.add(newItem("data", MSGS.data(), Resources.INSTANCE.data32()), false);
            // }

            // if (m_currentSession.hasDataReadPermission()) {
            // m_accountStore.add(newItem("usage", MSGS.usages(), Resources.INSTANCE.usages32()), false);
            // }

            if (m_currentSession.hasUserReadPermission()) {
                m_accountStore.add(newItem("user", MSGS.users(), Resources.INSTANCE.users32()), false);
            }
            if (m_currentSession.hasAccountReadPermission()) {
                m_accountStore.add(newItem("mysettings", MSGS.settings(), Resources.INSTANCE.settings32()), false);
            }

            //
            // Cloud menu
            if (m_currentSession.hasAccountReadPermission()) {
                m_manageStore.add(newItem("childaccounts", MSGS.childaccounts(), Resources.INSTANCE.childAccounts32()), false);
            }
        }

        if (selectedAccountItem != null) {
            String searchFor = (String) selectedAccountItem.get("id");

            for (int i = 0; i < m_accountStore.getAllItems().size(); i++) {
                String compareTo = (String) m_accountStore.getChild(i).get("id");
                if (searchFor.compareTo(compareTo) == 0) {
                    m_accountTree.getSelectionModel().select(i, false);
                    break;
                }
            }
        } else if (selectedManageItem != null) {
            String searchFor = (String) selectedManageItem.get("id");

            for (int i = 0; i < m_manageStore.getAllItems().size(); i++) {
                String compareTo = (String) m_manageStore.getChild(i).get("id");
                if (searchFor.compareTo(compareTo) == 0) {
                    m_manageTree.getSelectionModel().select(i, false);
                    break;
                }
            }
        }
    }

    public void setDashboardSelected(boolean isSelected) {
        this.dashboardSelected = isSelected;
    }

    private ModelData newItem(String id, String text, Object iconStyle) {
        ModelData m = new BaseModelData();
        m.set("id", id);
        m.set("name", text);
        m.set("icon", iconStyle);
        return m;
    }
}
