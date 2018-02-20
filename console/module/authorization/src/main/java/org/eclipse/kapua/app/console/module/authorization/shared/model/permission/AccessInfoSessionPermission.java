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
package org.eclipse.kapua.app.console.module.authorization.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class AccessInfoSessionPermission extends GwtSessionPermission {

    protected AccessInfoSessionPermission() {
        super();
    }

    private AccessInfoSessionPermission(GwtSessionPermissionAction action) {
        super("access_info", action, GwtSessionPermissionScope.SELF);
    }

    public static AccessInfoSessionPermission read() {
        return new AccessInfoSessionPermission(GwtSessionPermissionAction.read);
    }

    public static AccessInfoSessionPermission write() {
        return new AccessInfoSessionPermission(GwtSessionPermissionAction.write);
    }

    public static AccessInfoSessionPermission delete() {
        return new AccessInfoSessionPermission(GwtSessionPermissionAction.delete);
    }
}
