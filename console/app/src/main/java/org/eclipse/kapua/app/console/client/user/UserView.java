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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.commons.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.commons.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.commons.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.commons.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.client.user.tabs.credentials.UserTabItemCredentials;
import org.eclipse.kapua.app.console.client.user.tabs.description.UserTabDescription;
import org.eclipse.kapua.app.console.client.user.tabs.permission.UserTabItemPermission;
import org.eclipse.kapua.app.console.client.user.tabs.role.UserTabItemAccessRole;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserView extends AbstractEntityView<GwtUser> {

    private UserGrid userGrid;

    private UserTabItemAccessRole accessRoleTab;
    private UserTabItemPermission permissionTab;
    private UserTabItemCredentials credentialsTab;
    private UserTabDescription descriptionTab;

    public UserView(GwtSession gwtSession) {
        super(gwtSession);
    }

    @Override
    public List<KapuaTabItem<GwtUser>> getTabs(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtUser>> tabs = new ArrayList<KapuaTabItem<GwtUser>>();
        if (descriptionTab == null) {
            descriptionTab = new UserTabDescription();
            tabs.add(descriptionTab);
        }
        if (accessRoleTab == null) {
            accessRoleTab = new UserTabItemAccessRole(currentSession);
            tabs.add(accessRoleTab);
        }
        if (permissionTab == null) {
            permissionTab = new UserTabItemPermission(currentSession);
            tabs.add(permissionTab);
        }
        if (credentialsTab == null) {
            credentialsTab = new UserTabItemCredentials(currentSession);
            tabs.add(credentialsTab);
        }
        return tabs;
    }

    @Override
    public EntityGrid<GwtUser> getEntityGrid(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        if (userGrid == null) {
            userGrid = new UserGrid(entityView, currentSession);
        }
        return userGrid;
    }

    @Override
    public EntityFilterPanel<GwtUser> getEntityFilterPanel(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        return new UserFilterPanel(this, currentSession);
    }
}