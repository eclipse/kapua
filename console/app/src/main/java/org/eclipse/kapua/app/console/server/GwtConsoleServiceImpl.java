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

import org.eclipse.kapua.app.console.commons.client.views.EntityView;
import org.eclipse.kapua.app.console.commons.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.service.GwtConsoleService;

import java.util.ArrayList;
import java.util.List;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

    private static final String CLASSNAME = "org.eclipse.kapua.app.console.module.device.client.DeviceView";

    @Override
    public List<EntityView<? extends GwtEntityModel>> getCustomEntityViews() {
        List<EntityView<? extends GwtEntityModel>> views = new ArrayList<EntityView<? extends GwtEntityModel>>();
        try {
            EntityView<GwtEntityModel> entityView = (EntityView<GwtEntityModel>)Class.forName(CLASSNAME).newInstance();
            views.add(entityView);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return views;
    }
}
