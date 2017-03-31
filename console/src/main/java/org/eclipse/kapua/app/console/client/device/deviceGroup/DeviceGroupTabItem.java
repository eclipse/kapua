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
package org.eclipse.kapua.app.console.client.device.deviceGroup;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceGroupTabItem extends KapuaTabItem<GwtDevice> {

    private DeviceGroupGrid grid;
    DeviceGroupTabItemToolbar tool;
    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    public DeviceGroupTabItem(GwtSession gwtSession) {
        super(MSGS.gridUserTabPermissionsLabel(), new KapuaIcon(IconSet.CHECK_CIRCLE));

        grid = new DeviceGroupGrid(null, gwtSession);

    }

    public DeviceGroupGrid getGrid() {
        return grid;
    }

    @Override
    public void setEntity(GwtDevice t) {
        super.setEntity(t);
        if (t != null) {
            grid.setDeviceId(t.getId());
            ((DeviceGroupTabItemToolbar) grid.getToolbar()).setSelectedDevice(t);
        } else {
            grid.setDeviceId(null);
        }
    }

    @Override
    protected void doRefresh() {
        grid.refresh();
        grid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null);
        grid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(grid);
        
    }

    public void setSelectedDevice(GwtDevice selectedDevice) {
        grid.getToolbar().setSelectedDevice(selectedDevice);
        if (selectedDevice != null) {
            grid.setDeviceId(selectedDevice.getId());
        }else{
            grid.setDeviceId(null);
        }
        
        grid.refresh();
    }

}
