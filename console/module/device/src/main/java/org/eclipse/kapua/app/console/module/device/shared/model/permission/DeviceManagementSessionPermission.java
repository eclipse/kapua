/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class DeviceManagementSessionPermission extends GwtSessionPermission {

    protected DeviceManagementSessionPermission() {
        super();
    }

    private DeviceManagementSessionPermission(GwtSessionPermissionAction action) {
        super("device_management", action, GwtSessionPermissionScope.SELF);
    }

    public static DeviceManagementSessionPermission read() {
        return new DeviceManagementSessionPermission(GwtSessionPermissionAction.read);
    }

    public static DeviceManagementSessionPermission write() {
        return new DeviceManagementSessionPermission(GwtSessionPermissionAction.write);
    }

    public static DeviceManagementSessionPermission delete() {
        return new DeviceManagementSessionPermission(GwtSessionPermissionAction.delete);
    }

    public static DeviceManagementSessionPermission execute() {
        return new DeviceManagementSessionPermission(GwtSessionPermissionAction.execute);
    }
}
