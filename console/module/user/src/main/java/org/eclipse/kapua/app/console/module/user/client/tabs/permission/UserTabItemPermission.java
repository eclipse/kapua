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
package org.eclipse.kapua.app.console.module.user.client.tabs.permission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.client.tabs.permission.UserTabPermissionGrid;
import org.eclipse.kapua.app.console.module.authorization.client.tabs.permission.UserTabPermissionToolbar;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.DomainSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserTabItemPermission extends KapuaTabItem<GwtUser> {

    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);

    private UserTabPermissionGrid permissionGrid;

    public UserTabItemPermission(GwtSession currentSession) {
        super(currentSession, MSGS.gridUserTabPermissionsLabel(), new KapuaIcon(IconSet.CHECK_CIRCLE));

        permissionGrid = new UserTabPermissionGrid(null, currentSession);
        permissionGrid.setRefreshOnRender(false);
        setEnabled(false);
    }

    public UserTabPermissionGrid getPermissionGrid() {
        return permissionGrid;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
        add(permissionGrid);
    }

    @Override
    public void setEntity(GwtUser gwtUser) {
        super.setEntity(gwtUser);
        if (gwtUser != null) {
            setEnabled(true);
            permissionGrid.setUserId(gwtUser.getId());
            ((UserTabPermissionToolbar) permissionGrid.getToolbar()).setUserId(gwtUser.getId());
        } else {
            setEnabled(false);
            permissionGrid.setUserId(null);
            ((UserTabPermissionToolbar) permissionGrid.getToolbar()).setUserId(null);
        }
    }

    @Override
    protected void doRefresh() {
        permissionGrid.refresh();
        permissionGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null && currentSession.hasPermission(AccessInfoSessionPermission.write()) &&
              currentSession.hasPermission(DomainSessionPermission.read()) && currentSession.hasPermission(DomainSessionPermission.write()));
        permissionGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
