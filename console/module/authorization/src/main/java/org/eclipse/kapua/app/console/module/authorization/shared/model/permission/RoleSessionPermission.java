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
package org.eclipse.kapua.app.console.module.authorization.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class RoleSessionPermission extends GwtSessionPermission {

    protected RoleSessionPermission() {
        super();
    }

    private RoleSessionPermission(GwtSessionPermissionAction action) {
        super("role", action, GwtSessionPermissionScope.SELF);
    }

    public static RoleSessionPermission read() {
        return new RoleSessionPermission(GwtSessionPermissionAction.read);
    }

    public static RoleSessionPermission write() {
        return new RoleSessionPermission(GwtSessionPermissionAction.write);
    }

    public static RoleSessionPermission delete() {
        return new RoleSessionPermission(GwtSessionPermissionAction.delete);
    }
}
