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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user;

import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.role.RolePermissionGrid;
import org.eclipse.kapua.app.console.client.ui.tab.EntityDescriptionTabItem;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserTabPermissionGrid extends KapuaTabItem<GwtUser> {

//    UserPermissionGrid rolePermissionGrid = new UserPermissionGrid(null, null);

    public UserTabPermissionGrid(String title, KapuaIcon tabIcon) {
        super(title, tabIcon);
//      super("Permission", new KapuaIcon(IconSet.TASKS));
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

//        add(rolePermissionGrid);
    }

    @Override
    public void setEntity(GwtUser gwtRole) {
        super.setEntity(gwtRole);

//        rolePermissionGrid.setSelectedRole(gwtRole);
    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
//            rolePermissionGrid.refresh();
        } else {
//            rolePermissionGrid.clear();
        }
    }
}