/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

public class DeviceManagementRegistrySessionPermission extends GwtSessionPermission {

    private static final long serialVersionUID = 1L;

    public DeviceManagementRegistrySessionPermission() {
        super();
    }

    private DeviceManagementRegistrySessionPermission(GwtSessionPermissionAction action) {
        super("device_management_registry", action, GwtSessionPermissionScope.SELF);
    }

    public static DeviceManagementRegistrySessionPermission read() {
        return new DeviceManagementRegistrySessionPermission(GwtSessionPermissionAction.read);
    }

    public static DeviceManagementRegistrySessionPermission write() {
        return new DeviceManagementRegistrySessionPermission(GwtSessionPermissionAction.write);
    }

    public static DeviceManagementRegistrySessionPermission delete() {
        return new DeviceManagementRegistrySessionPermission(GwtSessionPermissionAction.delete);
    }
}
