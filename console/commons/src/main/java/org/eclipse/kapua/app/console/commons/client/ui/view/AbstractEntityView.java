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

import org.eclipse.kapua.app.console.commons.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.commons.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.commons.client.ui.panel.KapuaBorderLayoutData;
import org.eclipse.kapua.app.console.commons.client.ui.panel.KapuaTabPanel;
import org.eclipse.kapua.app.console.commons.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.commons.client.views.EntityView;
import org.eclipse.kapua.app.console.commons.shared.model.GwtEntityModel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;

public abstract class AbstractEntityView<M extends GwtEntityModel> extends AbstractView implements EntityView<M> {

    private EntityFilterPanel<M> filterPanel;
    private EntityGrid<M> entityGrid;
    private KapuaTabPanel<M> tabsPanel;

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
        LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
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

        //
        // Center sub panel: Entity sub tabs
        tabsPanel = new KapuaTabPanel<M>();

        List<KapuaTabItem<M>> tabItems = getTabs(this, currentSession);

        for (KapuaTabItem<M> kti : tabItems) {
            tabsPanel.add(kti);
        }

        KapuaBorderLayoutData centerData = new KapuaBorderLayoutData(LayoutRegion.CENTER);
        centerData.setMarginTop(5);

        resultContainer.add(tabsPanel, centerData);
    }

    public abstract List<KapuaTabItem<M>> getTabs(AbstractEntityView<M> entityView, GwtSession currentSession);

    public abstract EntityGrid<M> getEntityGrid(AbstractEntityView<M> entityView, GwtSession currentSession);

    public abstract EntityFilterPanel<M> getEntityFilterPanel(AbstractEntityView<M> entityView, GwtSession currentSession2);

    public void onUnload() {
        super.onUnload();
    }

}