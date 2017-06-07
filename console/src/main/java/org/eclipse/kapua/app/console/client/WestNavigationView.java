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
package org.eclipse.kapua.app.console.client;

import java.util.Arrays;

import org.eclipse.kapua.app.console.client.about.AboutView;
import org.eclipse.kapua.app.console.client.account.AccountDetailsView;
import org.eclipse.kapua.app.console.client.account.AccountView;
import org.eclipse.kapua.app.console.client.connection.ConnectionView;
import org.eclipse.kapua.app.console.client.data.DataView;
import org.eclipse.kapua.app.console.client.device.DevicesView;
import org.eclipse.kapua.app.console.client.group.GroupView;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.role.RoleView;
import org.eclipse.kapua.app.console.client.tag.TagView;
import org.eclipse.kapua.app.console.client.ui.misc.color.Color;
import org.eclipse.kapua.app.console.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.client.user.UserView;
import org.eclipse.kapua.app.console.client.welcome.WelcomeView;
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

    private final LayoutContainer centerPanel;
    private ContentPanel cloudResourcesPanel;
    private ContentPanel accordionPanel;
    private ContentPanel accountManagementPanel;

    private TreeStore<ModelData> cloudResourcesTreeStore;
    private TreeGrid<ModelData> cloudResourcesTreeGrid;
    private TreeStore<ModelData> accountManagementTreeStore;
    private TreeGrid<ModelData> accountManagementTreeGrid;

    private boolean dashboardSelected;
    private KapuaIcon imgRefreshLabel;
    private WelcomeView welcomeView;

    private final GwtSession currentSession;

    public WestNavigationView(GwtSession currentSession, LayoutContainer center) {
        this.currentSession = currentSession;

        welcomeView = new WelcomeView(this.currentSession);

        ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setBodyBorder(true);
        panel.setHeaderVisible(true);
        panel.setIcon(new KapuaIcon(IconSet.INFO));
        panel.setHeading(MSGS.welcome());
        panel.add(welcomeView);

        // set initial view

        centerPanel = center;
        centerPanel.add(panel);
        centerPanel.layout();

        dashboardSelected = true;
    }

    @Override
    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        //
        // Accordion Panel
        AccordionLayout accordionLayout = new AccordionLayout();
        accordionLayout.setFill(true);

        accordionPanel = new ContentPanel(accordionLayout);
        accordionPanel.setBorders(false);
        accordionPanel.setBodyBorder(false);
        accordionPanel.setHeaderVisible(false);
        add(accordionPanel);

        //
        // Top managing panel
        cloudResourcesPanel = new ContentPanel();
        cloudResourcesPanel.setAnimCollapse(false);
        cloudResourcesPanel.setBorders(false);
        cloudResourcesPanel.setBodyBorder(true);
        cloudResourcesPanel.setHeaderVisible(false);
        cloudResourcesPanel.setScrollMode(Scroll.AUTOY);

        //
        // Bottom manage panel
        accountManagementPanel = new ContentPanel();
        accountManagementPanel.setBorders(false);
        accountManagementPanel.setBodyBorder(false);
        accountManagementPanel.setHeading(MSGS.manageHeading());

        cloudResourcesTreeStore = new TreeStore<ModelData>();
        accountManagementTreeStore = new TreeStore<ModelData>();

        //
        // Adding item to stores
        //
        addMenuItems();

        ColumnConfig name = new ColumnConfig("name", "Name", 200);
        name.setRenderer(treeCellRenderer);

        ColumnModel cm = new ColumnModel(Arrays.asList(name));

        cloudResourcesTreeGrid = new TreeGrid<ModelData>(cloudResourcesTreeStore, cm);
        cloudResourcesTreeGrid.setBorders(false);
        cloudResourcesTreeGrid.setHideHeaders(true);
        cloudResourcesTreeGrid.setAutoExpandColumn("name");
        cloudResourcesTreeGrid.getTreeView().setRowHeight(36);
        cloudResourcesTreeGrid.getTreeView().setForceFit(true);

        cloudResourcesTreeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cloudResourcesTreeGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                ModelData selected = se.getSelectedItem();
                if (selected == null) {
                    return;
                }

                if (dashboardSelected && ((String) selected.get("id")).equals("welcome")) {
                    return;
                }

                accountManagementTreeGrid.getSelectionModel().deselectAll();

                centerPanel.removeAll();

                ContentPanel panel = new ContentPanel(new FitLayout());
                panel.setBorders(false);
                panel.setBodyBorder(false);

                String selectedId = (String) selected.get("id");
                if ("welcome".equals(selectedId)) {

                    welcomeView = new WelcomeView(currentSession);

                    panel.setBodyBorder(true);
                    panel.setIcon(new KapuaIcon(IconSet.INFO));
                    panel.setHeading(MSGS.welcome());
                    panel.add(welcomeView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("about".equals(selectedId)) {
                    AboutView aboutView = new AboutView();

                    panel.setBodyBorder(true);
                    panel.setIcon(new KapuaIcon(IconSet.INFO));
                    panel.setHeading(MSGS.about());
                    panel.add(aboutView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("devices".equals(selectedId)) {
                    DevicesView deviceView = new DevicesView(currentSession);

                    panel.setHeaderVisible(false);
                    panel.add(deviceView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("connection".equals(selectedId)) {
                    ConnectionView connectionView = new ConnectionView(currentSession);

                    panel.setHeaderVisible(false);
                    panel.add(connectionView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("data".equals(selectedId)) {
                    DataView dataView = new DataView(currentSession);
                    panel.setHeaderVisible(false);
                    panel.add(dataView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("tags".equals(selectedId)) {
                    panel.setIcon(new KapuaIcon(IconSet.TAGS));
                    panel.setHeading(MSGS.tags());

                    TagView groupView = new TagView(currentSession);
                    panel.add(groupView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("user".equals(selectedId)) {

                    UserView userView = new UserView(currentSession);
                    // userView.setAccount(m_currentSession.getSelectedAccount());

                    panel.setIcon(new KapuaIcon(IconSet.USERS));
                    panel.setHeading(MSGS.users());
                    panel.add(userView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;

                    // userView.refresh();
                } else if ("role".equals(selectedId)) {

                    panel.setIcon(new KapuaIcon(IconSet.STREET_VIEW));
                    panel.setHeading(MSGS.roles());

                    RoleView userView = new RoleView(currentSession);
                    panel.add(userView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("groups".equals(selectedId)) {
                    panel.setIcon(new KapuaIcon(IconSet.OBJECT_GROUP));
                    panel.setHeading(MSGS.groups());

                    GroupView groupView = new GroupView(currentSession);
                    panel.add(groupView);

                    centerPanel.add(panel);
                    centerPanel.layout();
                    dashboardSelected = false;
                } else if ("mysettings".equals(selectedId)) {

                    AccountDetailsView settingView = new AccountDetailsView(null, currentSession);
                    settingView.setAccount(currentSession.getSelectedAccount());

                    panel.setIcon(new KapuaIcon(IconSet.COG));
                    panel.setHeading(MSGS.settings());
                    panel.add(settingView);

                    centerPanel.add(panel);
                    centerPanel.layout();

                    settingView.refresh();
                }
            }
        });

        ColumnConfig name1 = new ColumnConfig("name", "Name", 200);
        name1.setRenderer(treeCellRenderer);

        ColumnModel cm1 = new ColumnModel(Arrays.asList(name1));

        accountManagementTreeGrid = new TreeGrid<ModelData>(accountManagementTreeStore, cm1);
        accountManagementTreeGrid.setBorders(false);
        accountManagementTreeGrid.setHideHeaders(true);
        accountManagementTreeGrid.setAutoExpandColumn("name");
        accountManagementTreeGrid.getTreeView().setRowHeight(36);
        accountManagementTreeGrid.getTreeView().setForceFit(true);
        accountManagementTreeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        accountManagementTreeGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {

                ModelData selected = se.getSelectedItem();
                if (selected == null) {
                    return;
                }

                cloudResourcesTreeGrid.getSelectionModel().deselectAll();

                centerPanel.removeAll();
                ContentPanel panel = new ContentPanel(new FitLayout());
                panel.setBorders(false);
                panel.setBodyBorder(false);

                String selectedId = (String) selected.get("id");
                if ("childaccounts".equals(selectedId)) {
                    AccountView accountView = new AccountView(currentSession);

                    panel.setIcon(new KapuaIcon(IconSet.SITEMAP));
                    panel.setHeading(MSGS.childaccounts());
                    panel.add(accountView);

                    dashboardSelected = false;
                }
                // imgRefreshLabel.setVisible(dashboardSelected);

                centerPanel.add(panel);
                centerPanel.layout();
            }
        });

        cloudResourcesPanel.add(cloudResourcesTreeGrid);
        cloudResourcesPanel.add(accountManagementPanel);
        cloudResourcesPanel.add(accountManagementTreeGrid);

        cloudResourcesTreeGrid.getSelectionModel().select(0, false);

        accordionPanel.add(cloudResourcesPanel);
    }

    public void addMenuItems() {
        ModelData selectedAccountItem = null;
        ModelData selectedManageItem = null;

        if (cloudResourcesTreeGrid != null && accountManagementTreeGrid != null) {
            selectedAccountItem = cloudResourcesTreeGrid.getSelectionModel().getSelectedItem();
            selectedManageItem = accountManagementTreeGrid.getSelectionModel().getSelectedItem();
        }

        cloudResourcesTreeStore.removeAll();
        accountManagementTreeStore.removeAll();

        GwtAccount selectedAccount = currentSession.getSelectedAccount();

        if (selectedAccount != null) {

            cloudResourcesTreeStore.add(newItem("welcome", MSGS.welcome(), IconSet.INFO), false);

            if (currentSession.hasDeviceReadPermission()) {
                cloudResourcesTreeStore.add(newItem("devices", MSGS.devices(), IconSet.HDD_O), false);
            }
            if (currentSession.hasConnectionReadPermission()) {
                cloudResourcesTreeStore.add(newItem("connection", MSGS.connections(), IconSet.PLUG), false);
            }
            if (currentSession.hasDataReadPermission()) {
                cloudResourcesTreeStore.add(newItem("data", "Data", IconSet.DATABASE), false);
            }
            if (currentSession.hasTagReadPermission()) {
                cloudResourcesTreeStore.add(newItem("tags", MSGS.tags(), IconSet.TAGS), false);
            }
            if (currentSession.hasUserReadPermission()) {
                cloudResourcesTreeStore.add(newItem("user", MSGS.users(), IconSet.USERS), false);
            }
            if (currentSession.hasRoleReadPermission()) {
                cloudResourcesTreeStore.add(newItem("role", MSGS.roles(), IconSet.STREET_VIEW), false);
            }
            if (currentSession.hasAccountReadPermission()) {
                cloudResourcesTreeStore.add(newItem("mysettings", MSGS.settings(), IconSet.COG), false);
            }
            if (currentSession.hasGroupReadPermission()) {
                cloudResourcesTreeStore.add(newItem("groups", MSGS.groups(), IconSet.OBJECT_GROUP), false);
            }

            //
            // Cloud menu
            if (currentSession.hasAccountReadPermission()) {
                accountManagementTreeStore.add(newItem("childaccounts", MSGS.childaccounts(), IconSet.SITEMAP), false);
            }

            cloudResourcesTreeStore.add(newItem("about", MSGS.about(), IconSet.INFO), false);
        }

        if (selectedAccountItem != null) {
            String searchFor = (String) selectedAccountItem.get("id");

            for (int i = 0; i < cloudResourcesTreeStore.getAllItems().size(); i++) {
                String compareTo = (String) cloudResourcesTreeStore.getChild(i).get("id");
                if (searchFor.compareTo(compareTo) == 0) {
                    cloudResourcesTreeGrid.getSelectionModel().select(i, false);
                    break;
                }
            }
        } else if (selectedManageItem != null) {
            String searchFor = (String) selectedManageItem.get("id");

            for (int i = 0; i < accountManagementTreeStore.getAllItems().size(); i++) {
                String compareTo = (String) accountManagementTreeStore.getChild(i).get("id");
                if (searchFor.compareTo(compareTo) == 0) {
                    accountManagementTreeGrid.getSelectionModel().select(i, false);
                    break;
                }
            }
        }
    }

    public void setDashboardSelected(boolean isSelected) {
        dashboardSelected = isSelected;
    }

    private ModelData newItem(String id, String text, IconSet icon) {
        ModelData m = new BaseModelData();
        m.set("id", id);
        m.set("name", text);
        m.set("icon", icon);
        return m;
    }

    private final WidgetTreeGridCellRenderer<ModelData> treeCellRenderer = new WidgetTreeGridCellRenderer<ModelData>() {

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
                            // FIXME: seems to be dead code
                            welcomeView.refresh();
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
