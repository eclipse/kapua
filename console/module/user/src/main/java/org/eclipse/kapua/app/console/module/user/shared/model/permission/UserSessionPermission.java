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
package org.eclipse.kapua.app.console.module.user.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class UserSessionPermission extends GwtSessionPermission {

    protected UserSessionPermission() {
        super();
    }

    private UserSessionPermission(GwtSessionPermissionAction action) {
        super("user", action, GwtSessionPermissionScope.SELF);
    }

    public static UserSessionPermission read() {
        return new UserSessionPermission(GwtSessionPermissionAction.read);
    }

    public static UserSessionPermission write() {
        return new UserSessionPermission(GwtSessionPermissionAction.write);
    }

    public static UserSessionPermission delete() {
        return new UserSessionPermission(GwtSessionPermissionAction.delete);
    }
}
