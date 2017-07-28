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
 *******************************************************************************/
package org.eclipse.kapua.app.console.commons.client.ui.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.commons.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.commons.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.commons.client.ui.panel.KapuaBorderLayoutData;
import org.eclipse.kapua.app.console.commons.client.ui.panel.KapuaTabPanel;
import org.eclipse.kapua.app.console.commons.client.views.EntityView;
import org.eclipse.kapua.app.console.commons.client.views.TabDescriptor;
import org.eclipse.kapua.app.console.commons.shared.model.GwtEntityModel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.commons.shared.service.GwtConsoleService;
import org.eclipse.kapua.app.console.commons.shared.service.GwtConsoleServiceAsync;

public abstract class AbstractEntityView<M extends GwtEntityModel> extends AbstractView implements EntityView<M> {

    private EntityFilterPanel<M> filterPanel;
    private EntityGrid<M> entityGrid;
    private KapuaTabPanel<M> tabsPanel;

    private static final GwtConsoleServiceAsync CONSOLE_SERVICE = GWT.create(GwtConsoleService.class);

    public AbstractEntityView() {
        super();

        setLayout(new BorderLayout());
        setBorders(false);
    }

    public AbstractEntityView(GwtSession currentSession) {
        this();
        this.currentSession = currentSession;
    }

    public void setSelectedEntity(M entity) {
        tabsPanel.setEntity(entity);
    }

    protected void onRender(final Element parent, int index) {

        super.onRender(parent, index);

        //
        // East Panel: Filtering menu
        filterPanel = getEntityFilterPanel(this, currentSession);
        if (filterPanel != null) {
            KapuaBorderLayoutData eastData = new KapuaBorderLayoutData(LayoutRegion.EAST, 250);
            eastData.setMarginLeft(5);
            add(filterPanel, eastData);
        }

        //
        // Center Main panel:
        final LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
        resultContainer.setBorders(false);

        KapuaBorderLayoutData centerMainPanel = new KapuaBorderLayoutData(LayoutRegion.CENTER);
        add(resultContainer, centerMainPanel);

        //
        // North sub panel: Entity grid
        entityGrid = getEntityGrid(this, currentSession);

        if (filterPanel != null) {
            filterPanel.setEntityGrid(entityGrid);
        }

        BorderLayoutData northData = new KapuaBorderLayoutData(LayoutRegion.NORTH, .45F);
        resultContainer.add(entityGrid, northData);

        CONSOLE_SERVICE.getCustomTabsForView(getClass().getName(), new AsyncCallback<List<TabDescriptor>>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Manage
                System.out.println("Failure!");
            }

            @Override
            public void onSuccess(List<TabDescriptor> result) {
                tabsPanel = new KapuaTabPanel<M>();

                for (TabDescriptor tabDescriptor : result) {
                    if (tabDescriptor.isEnabled(currentSession)) {
                        tabsPanel.add(tabDescriptor.getTabViewInstance(AbstractEntityView.this, currentSession));
                    }
                }

                KapuaBorderLayoutData centerData = new KapuaBorderLayoutData(LayoutRegion.CENTER);
                centerData.setMarginTop(5);

                resultContainer.add(tabsPanel, centerData);

                layout(true);
            }
        });

    }

    public abstract EntityGrid<M> getEntityGrid(AbstractEntityView<M> entityView, GwtSession currentSession);

    public abstract EntityFilterPanel<M> getEntityFilterPanel(AbstractEntityView<M> entityView, GwtSession currentSession2);

    public void onUnload() {
        super.onUnload();
    }

}
