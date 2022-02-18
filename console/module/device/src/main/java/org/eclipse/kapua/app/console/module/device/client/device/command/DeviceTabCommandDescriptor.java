/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.command;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.device.DeviceView;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceManagementSessionPermission;

public class DeviceTabCommandDescriptor extends AbstractEntityTabDescriptor<GwtDevice, DeviceTabCommand, DeviceView> {

    @Override
    public DeviceTabCommand getTabViewInstance(DeviceView view, GwtSession currentSession) {
        return new DeviceTabCommand(currentSession);
    }

    @Override
    public String getViewId() {
        return "device.command";
    }

    @Override
    public Integer getOrder() {
        return 800;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(DeviceManagementSessionPermission.execute());
    }
}
