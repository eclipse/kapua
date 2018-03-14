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
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;

public class DeviceTabTags extends KapuaTabItem<GwtDevice> {

    private static final ConsoleDeviceMessages DEVICES_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private DeviceTagGrid deviceTagGrid;

    public DeviceTabTags(GwtSession currentSession) {
        super(currentSession, DEVICES_MSGS.tabTagsTitle(), new KapuaIcon(IconSet.TAGS));
        deviceTagGrid = new DeviceTagGrid(null, currentSession, selectedEntity);
        setEnabled(false);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(deviceTagGrid);
        setBorders(false);
    }

    @Override
    public void setEntity(GwtDevice selectedDevice) {
        super.setEntity(selectedDevice);
        deviceTagGrid.setSelectedDevice(selectedDevice);
        setEnabled(selectedDevice != null);
    }

    @Override
    protected void doRefresh() {
        deviceTagGrid.refresh();
    }
}
