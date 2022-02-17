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

public class DeviceEventSessionPermission extends GwtSessionPermission {

    protected DeviceEventSessionPermission() {
        super();
    }

    private DeviceEventSessionPermission(GwtSessionPermissionAction action) {
        super("device_event", action, GwtSessionPermissionScope.SELF);
    }

    public static DeviceEventSessionPermission read() {
        return new DeviceEventSessionPermission(GwtSessionPermissionAction.read);
    }

    public static DeviceEventSessionPermission write() {
        return new DeviceEventSessionPermission(GwtSessionPermissionAction.write);
    }

    public static DeviceEventSessionPermission delete() {
        return new DeviceEventSessionPermission(GwtSessionPermissionAction.delete);
    }
}
