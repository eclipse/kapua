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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.permission;

import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.commons.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;

public class UserTabItemPermission extends KapuaTabItem<GwtAccessPermission> {

    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);

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
    public void setEntity(GwtAccessPermission gwtAccessPermission) {
        super.setEntity(gwtAccessPermission);
        if (gwtAccessPermission != null) {
            permissionGrid.setUserId(gwtAccessPermission.getAccessInfoId());
            ((UserTabPermissionToolbar) permissionGrid.getToolbar()).setUserId(gwtAccessPermission.getId());
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