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
package org.eclipse.kapua.app.console.client.user;

import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.user.tabs.permission.UserTabItemPermission;
import org.eclipse.kapua.app.console.client.user.tabs.role.UserTabItemAccessRole;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;

import java.util.ArrayList;
import java.util.List;

public class UserView extends EntityView<GwtUser> {

    private UserGrid userGrid;

    private UserTabItemAccessRole accessRoleTab;
    private UserTabItemPermission permissionTab;
    
    public UserView(GwtSession gwtSession) {
        super(gwtSession);
    }

    @Override
    public List<KapuaTabItem<GwtUser>> getTabs(EntityView<GwtUser> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtUser>> tabs = new ArrayList<KapuaTabItem<GwtUser>>();
        if (accessRoleTab == null) {
            accessRoleTab = new UserTabItemAccessRole(currentSession);
            tabs.add(accessRoleTab);
        }
        if (permissionTab == null) {
            permissionTab = new UserTabItemPermission(currentSession);
            tabs.add(permissionTab);
        }
        // tabs.add(new UserTabCredentialGrid());
        return tabs;
    }

    @Override
    public EntityGrid<GwtUser> getEntityGrid(EntityView<GwtUser> entityView, GwtSession currentSession) {
        if (userGrid == null) {
            userGrid = new UserGrid(entityView, currentSession);
        }
        return userGrid;
    }

    @Override
    public EntityFilterPanel<GwtUser> getEntityFilterPanel(EntityView<GwtUser> entityView, GwtSession currentSession) {
        return new UserFilterPanel(this, currentSession);
    }

    public UserTabItemAccessRole getAccessRoleTab() {
        return accessRoleTab;
    }

    public UserTabItemPermission getPermissionTab() {
        return permissionTab;
    }

}