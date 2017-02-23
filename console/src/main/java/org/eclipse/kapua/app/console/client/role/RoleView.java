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
package org.eclipse.kapua.app.console.client.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;

public class RoleView extends EntityView<GwtRole> {

    private RoleGrid roleGrid;
    private RoleTabDescription roleTabDescription;
    private RoleTabPermissionGrid roleTabPermissionGrid;
    private RoleTabSubjectGrid roleTabSubjectGrid;

    public RoleView(GwtSession gwtSession) {
        super(gwtSession);
        roleTabDescription = new RoleTabDescription();
        roleTabPermissionGrid = new RoleTabPermissionGrid(null, gwtSession);
        roleTabSubjectGrid = new RoleTabSubjectGrid(gwtSession);
    }

    @Override
    public List<KapuaTabItem<GwtRole>> getTabs(EntityView<GwtRole> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtRole>> tabs = new ArrayList<KapuaTabItem<GwtRole>>();
        tabs.add(roleTabDescription);
        tabs.add(roleTabPermissionGrid);
        tabs.add(roleTabSubjectGrid);
        return tabs;
    }

    @Override
    public EntityGrid<GwtRole> getEntityGrid(EntityView<GwtRole> entityView, GwtSession currentSession) {
        if (roleGrid == null) {
            roleGrid = new RoleGrid(entityView, currentSession);
        }
        return roleGrid;
    }

    @Override
    public EntityFilterPanel<GwtRole> getEntityFilterPanel(EntityView<GwtRole> entityView, GwtSession currentSession) {
        return new RoleFilterPanel(this, currentSession);
    }

}