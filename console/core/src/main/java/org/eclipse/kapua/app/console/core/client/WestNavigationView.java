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
package org.eclipse.kapua.app.console.core.client;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.MainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleServiceAsync;
import org.eclipse.kapua.app.console.module.account.client.AccountDetailsView;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractView;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleService;

import java.util.Arrays;
import java.util.List;

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

    private final GwtSession currentSession;

    private static final GwtAccountServiceAsync GWT_ACCOUNT_SERVICE = GWT.create(GwtAccountService.class);

    private static final GwtConsoleServiceAsync CONSOLE_SERVICE = GWT.create(GwtConsoleService.class);

    public WestNavigationView(GwtSession currentSession, LayoutContainer center) {
        this.currentSession = currentSession;
        centerPanel = center;
        dashboardSelected = true;
    }

    @Override
    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        CONSOLE_SERVICE.getCustomEntityViews(new AsyncCallback<List<MainViewDescriptor>>() {

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("");
            }

            @Override
            public void onSuccess(final List<MainViewDescriptor> additionalViewDescriptors) {

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
                addMenuItems(additionalViewDescriptors);

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

                        if (dashboardSelected && (selected.get("id")).equals("welcome")) {
                            return;
                        }

                        centerPanel.removeAll();

                        final ContentPanel panel = new ContentPanel(new FitLayout());

                        panel.setBorders(false);
                        panel.setBodyBorder(false);

                        String selectedId = selected.get("id");
                        if ("mysettings".equals(selectedId)) {
                            // TODO generalize!
                            GWT_ACCOUNT_SERVICE.find(currentSession.getSelectedAccountId(), new AsyncCallback<GwtAccount>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    FailureHandler.handle(caught);
                                }

                                @Override
                                public void onSuccess(GwtAccount result) {
                                    AccountDetailsView settingView = new AccountDetailsView(currentSession);
                                    settingView.setAccount(result);

                                    panel.setIcon(new KapuaIcon(IconSet.COG));
                                    panel.setHeading(MSGS.settings());
                                    panel.add(settingView);

                                    centerPanel.add(panel);
                                    centerPanel.layout();

                                    settingView.refresh();
                                }
                            });

                        } else {
                            for (MainViewDescriptor viewDescriptor : additionalViewDescriptors) {
                                if (viewDescriptor.getViewId().equals(selectedId)) {
                                    panel.setIcon(new KapuaIcon(viewDescriptor.getIcon()));
                                    panel.setHeading(viewDescriptor.getName());
                                    panel.add((AbstractView) viewDescriptor.getViewInstance(currentSession));

                                    centerPanel.add(panel);
                                    centerPanel.layout();
                                }
                            }
                        }

                        setDashboardSelected(false);
                    }
                });

                if (additionalViewDescriptors.size() > 0) {
                    centerPanel.removeAll();

                    final ContentPanel panel = new ContentPanel(new FitLayout());

                    MainViewDescriptor firstView = additionalViewDescriptors.get(0);
                    panel.setIcon(new KapuaIcon(firstView.getIcon()));
                    panel.setHeading(firstView.getName());
                    panel.add((AbstractView) firstView.getViewInstance(currentSession));

                    centerPanel.add(panel);
                    centerPanel.layout();
                }

                ColumnConfig name1 = new ColumnConfig("name", "Name", 200);
                name1.setRenderer(treeCellRenderer);

                cloudResourcesPanel.add(cloudResourcesTreeGrid);
                accordionPanel.add(cloudResourcesPanel);
                layout(true);
            }
        });

    }

    public void addMenuItems(List<MainViewDescriptor> additionalViewDescriptors) {

        ModelData selectedAccountItem = null;
        ModelData selectedManageItem = null;

        if (cloudResourcesTreeGrid != null && accountManagementTreeGrid != null) {
            selectedAccountItem = cloudResourcesTreeGrid.getSelectionModel().getSelectedItem();
            selectedManageItem = accountManagementTreeGrid.getSelectionModel().getSelectedItem();
        }

        cloudResourcesTreeStore.removeAll();
        accountManagementTreeStore.removeAll();

        if (additionalViewDescriptors != null && additionalViewDescriptors.size() > 0) {
            for (MainViewDescriptor entityView : additionalViewDescriptors) {
                if (entityView.isEnabled(currentSession)) {
                    cloudResourcesTreeStore.add(newItem(entityView.getViewId(), entityView.getName(), entityView.getIcon()), false);
                }
            }
        }

        if (selectedAccountItem != null) {
            String searchFor = selectedAccountItem.get("id");

            for (int i = 0; i < cloudResourcesTreeStore.getAllItems().size(); i++) {
                String compareTo = cloudResourcesTreeStore.getChild(i).get("id");
                if (searchFor.compareTo(compareTo) == 0) {
                    cloudResourcesTreeGrid.getSelectionModel().select(i, false);
                    break;
                }
            }
        } else if (selectedManageItem != null) {
            String searchFor = selectedManageItem.get("id");

            for (int i = 0; i < accountManagementTreeStore.getAllItems().size(); i++) {
                String compareTo = accountManagementTreeStore.getChild(i).get("id");
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
            // Return component
            return lc;
        }
    };
}
