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

public class DeviceSessionPermission extends GwtSessionPermission {

    protected DeviceSessionPermission() {
        super();
    }

    private DeviceSessionPermission(GwtSessionPermissionAction action) {
        super("device", action, GwtSessionPermissionScope.SELF);
    }

    public static DeviceSessionPermission read() {
        return new DeviceSessionPermission(GwtSessionPermissionAction.read);
    }

    public static DeviceSessionPermission write() {
        return new DeviceSessionPermission(GwtSessionPermissionAction.write);
    }

    public static DeviceSessionPermission delete() {
        return new DeviceSessionPermission(GwtSessionPermissionAction.delete);
    }
}
