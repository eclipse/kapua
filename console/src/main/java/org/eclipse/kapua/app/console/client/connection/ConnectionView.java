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
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.connection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection;

public class ConnectionView extends EntityView<GwtDeviceConnection> {

    private ConnectionGrid connectionGrid;
    private ConnectionDescriptionTab descriptionTab;
    private ConnectionFilterPanel filterPanel;

    public ConnectionView(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public List<KapuaTabItem<GwtDeviceConnection>> getTabs(EntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtDeviceConnection>> tabs = new ArrayList<KapuaTabItem<GwtDeviceConnection>>();
        if(descriptionTab == null){
            descriptionTab = new ConnectionDescriptionTab();
        }
        tabs.add(descriptionTab);
        return tabs;
    }

    @Override
    public EntityGrid<GwtDeviceConnection> getEntityGrid(EntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        if(connectionGrid == null){
            connectionGrid = new ConnectionGrid(this, currentSession);
        }
        return connectionGrid;
    }

    @Override
    public EntityFilterPanel<GwtDeviceConnection> getEntityFilterPanel(EntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        if(filterPanel == null){
            filterPanel = new ConnectionFilterPanel(entityView, currentSession);
        }
        return filterPanel;
    }
}
