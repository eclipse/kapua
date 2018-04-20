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
package org.eclipse.kapua.app.console.module.device.client.device;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;

public class DeviceView extends AbstractEntityView<GwtDevice> {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private DeviceGrid deviceGrid;
    private DeviceFilterPanel deviceFilterPanel;

    public DeviceView(GwtSession currentSession) {
        super(currentSession);
    }

    public static String getName() {
        return MSGS.mainMenuDevices();
    }

    @Override
    public EntityGrid<GwtDevice> getEntityGrid(AbstractEntityView<GwtDevice> entityView, GwtSession currentSession) {
        if (deviceGrid == null) {
            deviceGrid = new DeviceGrid(entityView, currentSession);
        }
        return deviceGrid;
    }

    @Override
    public EntityFilterPanel<GwtDevice> getEntityFilterPanel(AbstractEntityView<GwtDevice> entityView, GwtSession currentSession) {
        if (deviceFilterPanel == null) {
            deviceFilterPanel = new DeviceFilterPanel(entityView, currentSession);
        }
        return deviceFilterPanel;
    }

}
