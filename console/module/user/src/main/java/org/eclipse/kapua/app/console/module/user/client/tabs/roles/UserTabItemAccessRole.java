/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client.tabs.roles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.client.tabs.role.UserTabAccessRoleGrid;
import org.eclipse.kapua.app.console.module.authorization.client.tabs.role.UserTabAccessRoleToolbar;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserTabItemAccessRole extends KapuaTabItem<GwtUser> {

    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);

    private UserTabAccessRoleGrid accessRoleGrid;

    public UserTabItemAccessRole(GwtSession currentSession) {
        super(currentSession, MSGS.gridUserTabRolesLabel(), new KapuaIcon(IconSet.STREET_VIEW));
        accessRoleGrid = new UserTabAccessRoleGrid(currentSession, null);
        accessRoleGrid.setRefreshOnRender(false);
        setEnabled(false);
    }

    public UserTabAccessRoleGrid getAccessRoleGrid() {
        return accessRoleGrid;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
        add(accessRoleGrid);
    }

    @Override
    public void setEntity(GwtUser gwtUser) {
        super.setEntity(gwtUser);
        if (gwtUser != null) {
            setEnabled(true);
            accessRoleGrid.setUserId(gwtUser.getId());
            ((UserTabAccessRoleToolbar) accessRoleGrid.getToolbar()).setUserId(gwtUser.getId());
        } else {
            setEnabled(false);
            accessRoleGrid.setUserId(null);
            ((UserTabAccessRoleToolbar) accessRoleGrid.getToolbar()).setUserId(null);
        }
    }

    @Override
    protected void doRefresh() {
        accessRoleGrid.refresh();
        accessRoleGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null && currentSession.hasPermission(AccessInfoSessionPermission.write()));
        accessRoleGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
