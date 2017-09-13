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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class RoleTabPermissionGrid extends KapuaTabItem<GwtRole> {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    RolePermissionGrid rolePermissionGrid;

    public RoleTabPermissionGrid(AbstractEntityView<GwtRolePermission> entityView, GwtSession session) {
        super(MSGS.roleTabPermissionGridTitle(), new KapuaIcon(IconSet.TASKS));

        rolePermissionGrid = new RolePermissionGrid(entityView, session);

    }

    public RoleTabPermissionGrid(GwtSession session) {
        this(null, session);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        add(rolePermissionGrid);
    }

    @Override
    public void setEntity(GwtRole gwtRole) {
        super.setEntity(gwtRole);
        rolePermissionGrid.setSelectedRole(gwtRole);
    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
            rolePermissionGrid.refresh();
        } else {
            rolePermissionGrid.clear();
        }
    }

}
