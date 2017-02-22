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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.group;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

public class GroupView  extends EntityView<GwtGroup>{

        private GroupGrid groupGrid;
        
        public GroupView(GwtSession gwtSession) {
            super(gwtSession);
        }
    @Override
    public List<KapuaTabItem<GwtGroup>> getTabs(EntityView<GwtGroup> entityView,
            GwtSession currentSession) {
        List<KapuaTabItem<GwtGroup>> tabs = new ArrayList<KapuaTabItem<GwtGroup>>();
        tabs.add(new GroupTabDescription());
        return tabs;
    }

    @Override
    public EntityGrid<GwtGroup> getEntityGrid(EntityView<GwtGroup> entityView,
            GwtSession currentSession) {
        if (groupGrid == null) {
            groupGrid = new GroupGrid(entityView, currentSession);
        }
        return groupGrid;
    }

    @Override
    public EntityFilterPanel<GwtGroup> getEntityFilterPanel(EntityView<GwtGroup> entityView,
            GwtSession currentSession2) {
       
        return new GroupFilterPanel(this, currentSession2);
    }

}