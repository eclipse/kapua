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
package org.eclipse.kapua.app.console.module.device.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.commons.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.commons.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.commons.client.ui.view.AbstractGwtEntityView;
import org.eclipse.kapua.app.console.commons.client.views.EntityView;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;

import java.util.List;

public class DeviceView extends AbstractGwtEntityView<GwtDevice> implements EntityView<GwtDevice>, IsSerializable {

    @Override
    public String getId() {
        return "device_new";
    }

    @Override
    public String getName() {
        return "Device New!";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.ANCHOR;
    }

    @Override
    public List<KapuaTabItem<GwtDevice>> getTabs(AbstractGwtEntityView<GwtDevice> entityView, GwtSession currentSession) {
        return null;
    }

    @Override
    public EntityGrid<GwtDevice> getEntityGrid(AbstractGwtEntityView<GwtDevice> entityView, GwtSession currentSession) {
        return null;
    }

    @Override
    public EntityFilterPanel<GwtDevice> getEntityFilterPanel(AbstractGwtEntityView<GwtDevice> entityView, GwtSession currentSession2) {
        return null;
    }
}
