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

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class RoleTabSubjectGrid extends KapuaTabItem<GwtRole> {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    RoleSubjectGrid rolePermissionGrid;

    public RoleTabSubjectGrid(GwtSession gwtSession) {
        super(MSGS.roleTabSubjectGridTitle(), new KapuaIcon(IconSet.SUPPORT));
        rolePermissionGrid = new RoleSubjectGrid(null, gwtSession);

    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
            rolePermissionGrid.refresh();
        }

    }

    @Override
    public void setEntity(GwtRole t) {
        super.setEntity(t);
        rolePermissionGrid.setEntity(t);

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(rolePermissionGrid);
    }

}
