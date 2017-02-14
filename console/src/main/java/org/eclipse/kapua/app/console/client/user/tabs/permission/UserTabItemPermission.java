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
package org.eclipse.kapua.app.console.client.user.tabs.permission;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class UserTabItemPermission extends KapuaTabItem<GwtUser> {

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private UserTabPermissionGrid permissionGrid;

    public UserTabItemPermission(GwtSession currentSession) {
        super(MSGS.gridUserTabPermissionsLabel(), new KapuaIcon(IconSet.CHECK_CIRCLE));
        permissionGrid = new UserTabPermissionGrid(null, currentSession);
    }

    public UserTabPermissionGrid getPermissionGrid() {
        return permissionGrid;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(permissionGrid);
    }

    @Override
    public void setEntity(GwtUser gwtUser) {
        super.setEntity(gwtUser);
        if (gwtUser != null) {
            permissionGrid.setUserId(gwtUser.getId());
            ((UserTabPermissionToolbar) permissionGrid.getToolbar()).setUserId(gwtUser.getId());
        } else {
            permissionGrid.setUserId(null);
            ((UserTabPermissionToolbar) permissionGrid.getToolbar()).setUserId(null);
        }
    }

    @Override
    protected void doRefresh() {
        permissionGrid.refresh();
        permissionGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null);
        permissionGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}