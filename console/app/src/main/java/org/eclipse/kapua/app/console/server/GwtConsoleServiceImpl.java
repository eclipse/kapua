/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.commons.client.views.ViewDescriptor;
import org.eclipse.kapua.app.console.commons.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.about.client.about.AboutViewDescriptor;
import org.eclipse.kapua.app.console.module.authorization.client.group.GroupViewDescriptor;
import org.eclipse.kapua.app.console.module.authorization.client.role.RoleViewDescriptor;
import org.eclipse.kapua.app.console.module.data.client.DataViewDescriptor;
import org.eclipse.kapua.app.console.module.device.client.DevicesViewDescriptor;
import org.eclipse.kapua.app.console.module.device.client.connection.ConnectionViewDescriptor;
import org.eclipse.kapua.app.console.module.tag.client.TagViewDescriptor;
import org.eclipse.kapua.app.console.module.user.client.UserViewDescriptor;
import org.eclipse.kapua.app.console.module.welcome.client.WelcomeViewDescriptor;
import org.eclipse.kapua.app.console.shared.service.GwtConsoleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

     private static final String ABOUT_CLASSNAME = "org.eclipse.kapua.app.console.module.about.client.about.AboutViewDescriptor";
     private static final String ROLES_CLASSNAME = "org.eclipse.kapua.app.console.module.authorization.client.role.RoleViewDescriptor";
     private static final String USER_CLASSNAME = "org.eclipse.kapua.app.console.module.user.client.UserViewDescriptor";
     private static final String WELCOME_CLASSNAME = "org.eclipse.kapua.app.console.module.welcome.client.WelcomeViewDescriptor";
     private static final String TAG_CLASSNAME = "org.eclipse.kapua.app.console.module.tag.client.TagViewDescriptor";
     private static final String DEVICE_CLASSNAME = "org.eclipse.kapua.app.console.module.device.client.DevicesViewDescriptor";
     private static final String CONNECTION_CLASSNAME = "org.eclipse.kapua.app.console.module.device.client.connection.ConnectionViewDescriptor";
     private static final String GROUP_CLASSNAME = "org.eclipse.kapua.app.console.module.authorization.client.group.GroupViewDescriptor";
     private static final String DATA_CLASSNAME = "org.eclipse.kapua.app.console.module.data.client.DataViewDescriptor";

    @Override
    public List<ViewDescriptor> getCustomEntityViews() {
        List<ViewDescriptor> views = new ArrayList<ViewDescriptor>();
        try {
            UserViewDescriptor userView = (UserViewDescriptor)Class.forName(USER_CLASSNAME).newInstance();
            views.add(userView);
            AboutViewDescriptor aboutView = (AboutViewDescriptor)Class.forName(ABOUT_CLASSNAME).newInstance();
            views.add(aboutView);
            RoleViewDescriptor roleView = (RoleViewDescriptor)Class.forName(ROLES_CLASSNAME).newInstance();
            views.add(roleView);
            WelcomeViewDescriptor welcomeView = (WelcomeViewDescriptor)Class.forName(WELCOME_CLASSNAME).newInstance();
            views.add(welcomeView);
            TagViewDescriptor tagView = (TagViewDescriptor)Class.forName(TAG_CLASSNAME).newInstance();
            views.add(tagView);
            DevicesViewDescriptor devicesView = (DevicesViewDescriptor)Class.forName(DEVICE_CLASSNAME).newInstance();
            views.add(devicesView);
            ConnectionViewDescriptor connectionView = (ConnectionViewDescriptor)Class.forName(CONNECTION_CLASSNAME).newInstance();
            views.add(connectionView);
            GroupViewDescriptor groupView = (GroupViewDescriptor)Class.forName(GROUP_CLASSNAME).newInstance();
            views.add(groupView);
            DataViewDescriptor dataView = (DataViewDescriptor)Class.forName(DATA_CLASSNAME).newInstance();
            views.add(dataView);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Collections.sort(views);
        return views;
    }
}
