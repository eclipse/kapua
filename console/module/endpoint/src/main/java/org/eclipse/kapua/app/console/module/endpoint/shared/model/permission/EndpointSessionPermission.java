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
package org.eclipse.kapua.app.console.module.endpoint.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class EndpointSessionPermission extends GwtSessionPermission {

    protected EndpointSessionPermission() {
        super();
    }

    private EndpointSessionPermission(GwtSessionPermissionAction action) {
        this(action, GwtSessionPermissionScope.SELF);
    }

    private EndpointSessionPermission(GwtSessionPermissionAction action, GwtSessionPermissionScope permissionScope) {
        super("endpoint_info", action, permissionScope);
    }

    public static EndpointSessionPermission read() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.read);
    }

    public static EndpointSessionPermission readAll() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.read, GwtSessionPermissionScope.ALL);
    }

    public static EndpointSessionPermission write() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.write);
    }

    public static EndpointSessionPermission writeAll() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.write, GwtSessionPermissionScope.ALL);
    }

    public static EndpointSessionPermission delete() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.delete);
    }

    public static EndpointSessionPermission deleteAll() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.delete, GwtSessionPermissionScope.ALL);
    }
}
