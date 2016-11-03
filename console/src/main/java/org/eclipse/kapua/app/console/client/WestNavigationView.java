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
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.client.user.UserView;
import org.eclipse.kapua.app.console.client.welcome.WelcomeView;
import org.eclipse.kapua.app.console.client.widget.color.Color;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class WestNavigationView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private LayoutContainer m_centerPanel;
    private ContentPanel m_accountPanel;
    private ContentPanel m_accordionPanel;
    private ContentPanel m_managePanel;

    private TreeStore<ModelData> m_accountStore;
    private TreeGrid<ModelData> m_accountTree;
    private TreeStore<ModelData> m_manageStore;
    private TreeGrid<ModelData> m_manageTree;

    private boolean dashboardSelected;
    private KapuaIcon imgRefreshLabel;
    private WelcomeView m_welcomeView;

    private GwtSession m_currentSession;

    public WestNavigationView(GwtSession currentSession, LayoutContainer center) {
        m_currentSession = currentSession;

        m_welcomeView = new WelcomeView(m_currentSession);

        ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setBodyBorder(true);
        panel.setHeaderVisible(true);
        panel.setIcon(new KapuaIcon(IconSet.INFO));
        panel.setHeading(MSGS.welcome());
        panel.add(m_welcomeView);

        m_centerPanel = center;
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
        AccordionLayout accordionLayout = new AccordionLayout();
        accordionLayout.setFill(true);

        m_accordionPanel = new ContentPanel(accordionLayout);
        m_accordionPanel.setBorders(false);
        m_accordionPanel.setBodyBorder(false);
        m_accordionPanel.setHeaderVisible(false);
        add(m_accordionPanel);

        //
        // Top managing panel
        m_accountPanel = new ContentPanel();
        m_accountPanel.setBorders(false);
        m_accountPanel.setBodyBorder(true);
        m_accountPanel.setHeaderVisible(false);
        m_accountPanel.setScrollMode(Scroll.AUTOY);

        //
        // Bottom manage panel
        m_managePanel = new ContentPanel();
        m_managePanel.setBorders(false);
        m_managePanel.setBodyBorder(false);
        m_managePanel.setHeading(MSGS.manageHeading());

        m_accountStore = new TreeStore<ModelData>();
        m_manageStore = new TreeStore<ModelData>();

        //
        // Adding item to stores
        //
        addMenuItems();

        ColumnConfig name = new ColumnConfig("name", "Name", 200);
        name.setRenderer(treeCellRenderer);

        ColumnModel cm = new ColumnModel(Arrays.asList(name));

        m_accountTree = new TreeGrid<ModelData>(m_accountStore, cm);
        m_accountTree.setBorders(false);
        m_accountTree.setHideHeaders(true);
        m_accountTree.setAutoExpandColumn("name");
        m_accountTree.getTreeView().setRowHeight(36);
        m_accountTree.getTreeView().setForceFit(true);

        m_accountTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        m_accountTree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                ModelData selected = se.getSelectedItem();
                if (selected == null)
                    return;

                if (dashboardSelected && ((String) selected.get("id")).equals("welcome")) {
                    return;
                }

                m_manageTree.getSelectionModel().deselectAll();

                m_centerPanel.removeAll();

                ContentPanel panel = new ContentPanel(new FitLayout());
                panel.setBorders(false);
                panel.setBodyBorder(false);

                String selectedId = (String) selected.get("id");
                if ("welcome".equals(selectedId)) {

                    m_welcomeView = new WelcomeView(m_currentSession);

                    panel.setBodyBorder(true);
                    panel.setIcon(new KapuaIcon(IconSet.INFO));
                    panel.setHeading(MSGS.welcome());
                    panel.add(m_welcomeView);

                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    dashboardSelected = false;
                } else if ("devices".equals(selectedId)) {
                    DevicesView deviceView = new DevicesView(m_currentSession);

                    panel.setHeaderVisible(false);
                    panel.add(deviceView);

                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    dashboardSelected = false;
                } else if ("user".equals(selectedId)) {

                    UserView userView = new UserView(m_currentSession);
                    userView.setAccount(m_currentSession.getSelectedAccount());

                    panel.setIcon(new KapuaIcon(IconSet.USERS));
                    panel.setHeading(MSGS.users());
                    panel.add(userView);

                    m_centerPanel.add(panel);
                    m_centerPanel.layout();
                    dashboardSelected = false;

                    userView.refresh();
                } else if ("mysettings".equals(selectedId)) {

                    AccountDetailsView settingView = new AccountDetailsView(null, m_currentSession);
                    settingView.setAccount(m_currentSession.getSelectedAccount());

                    panel.setIcon(new KapuaIcon(IconSet.COG));
                    panel.setHeading(MSGS.settings());
                    panel.add(settingView);

                    m_centerPanel.add(panel);
                    m_centerPanel.layout();

                    settingView.refresh();
                }

                // imgRefreshLabel.setVisible(dashboardSelected);
            }
        });

        ColumnConfig name1 = new ColumnConfig("name", "Name", 200);
        name1.setRenderer(treeCellRenderer);

        ColumnModel cm1 = new ColumnModel(Arrays.asList(name1));

        m_manageTree = new TreeGrid<ModelData>(m_manageStore, cm1);
        m_manageTree.setBorders(false);
        m_manageTree.setHideHeaders(true);
        m_manageTree.setAutoExpandColumn("name");
        m_manageTree.getTreeView().setRowHeight(36);
        m_manageTree.getTreeView().setForceFit(true);
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
                    AccountView accountView = new AccountView(m_currentSession);

                    panel.setIcon(new KapuaIcon(IconSet.SITEMAP));
                    panel.setHeading(MSGS.childaccounts());
                    panel.add(accountView);

                    dashboardSelected = false;
                }
                // imgRefreshLabel.setVisible(dashboardSelected);

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

            m_accountStore.add(newItem("welcome", MSGS.welcome(), IconSet.INFO), false);

            if (m_currentSession.hasDeviceReadPermission()) {
                m_accountStore.add(newItem("devices", MSGS.devices(), IconSet.HDD_O), false);
            }

            if (m_currentSession.hasUserReadPermission()) {
                m_accountStore.add(newItem("user", MSGS.users(), IconSet.USERS), false);
            }
            if (m_currentSession.hasAccountReadPermission()) {
                m_accountStore.add(newItem("mysettings", MSGS.settings(), IconSet.COG), false);
            }

            //
            // Cloud menu
            if (m_currentSession.hasAccountReadPermission()) {
                m_manageStore.add(newItem("childaccounts", MSGS.childaccounts(), IconSet.SITEMAP), false);
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

    private ModelData newItem(String id, String text, IconSet icon) {
        ModelData m = new BaseModelData();
        m.set("id", id);
        m.set("name", text);
        m.set("icon", icon);
        return m;
    }

    private WidgetTreeGridCellRenderer<ModelData> treeCellRenderer = new WidgetTreeGridCellRenderer<ModelData>() {

        @Override
        public Widget getWidget(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {

            TableLayout layout = new TableLayout(3);
            layout.setWidth("100%");

            LayoutContainer lc = new LayoutContainer(layout);
            lc.setStyleAttribute("margin-top", "3px");
            lc.setWidth(170);
            lc.setScrollMode(Scroll.NONE);

            //
            // Icon
            KapuaIcon icon = new KapuaIcon((IconSet) model.get("icon"));
            icon.setEmSize(2);
            icon.setColor(Color.BLUE_KAPUA);

            TableData iconTableData = new TableData(Style.HorizontalAlignment.CENTER, Style.VerticalAlignment.MIDDLE);
            iconTableData.setWidth("35px");
            lc.add(icon, iconTableData);

            //
            // Label
            Label label = new Label((String) model.get(property));
            label.setStyleAttribute("margin-left", "5px");

            TableData labelTableData = new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.MIDDLE);
            lc.add(label, labelTableData);

            //
            // Refresh icon for dashboard view
            if (((String) model.get(property)).equals("Dashboard")) {
                imgRefreshLabel = new KapuaIcon(IconSet.REFRESH);
                imgRefreshLabel.addListener(Events.OnClick, new Listener<BaseEvent>() {

                    @Override
                    public void handleEvent(BaseEvent be) {
                        if (dashboardSelected) {
                            m_welcomeView.refresh();
                        }
                    }
                });

                lc.add(imgRefreshLabel, new TableData(Style.HorizontalAlignment.RIGHT, Style.VerticalAlignment.MIDDLE));
            }

            //
            // Return component
            return lc;
        }
    };
}
