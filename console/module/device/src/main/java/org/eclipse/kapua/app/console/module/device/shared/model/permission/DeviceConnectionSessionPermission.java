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

public class DeviceConnectionSessionPermission extends GwtSessionPermission {

    protected DeviceConnectionSessionPermission() {
        super();
    }

    private DeviceConnectionSessionPermission(GwtSessionPermissionAction action) {
        super("device_connection", action, GwtSessionPermissionScope.SELF);
    }

    public static DeviceConnectionSessionPermission read() {
        return new DeviceConnectionSessionPermission(GwtSessionPermissionAction.read);
    }

    public static DeviceConnectionSessionPermission write() {
        return new DeviceConnectionSessionPermission(GwtSessionPermissionAction.write);
    }

    public static DeviceConnectionSessionPermission delete() {
        return new DeviceConnectionSessionPermission(GwtSessionPermissionAction.delete);
    }
}
