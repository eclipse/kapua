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
package org.eclipse.kapua.app.console.module.endpoint.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class EndpointSessionPermission extends GwtSessionPermission {

    protected EndpointSessionPermission() {
        super();
    }

    private EndpointSessionPermission(GwtSessionPermissionAction action) {
        super("endpoint_info", action, GwtSessionPermissionScope.SELF);
    }

    public static EndpointSessionPermission read() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.read);
    }

    public static EndpointSessionPermission write() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.write);
    }

    public static EndpointSessionPermission delete() {
        return new EndpointSessionPermission(GwtSessionPermissionAction.delete);
    }
}
