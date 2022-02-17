/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
