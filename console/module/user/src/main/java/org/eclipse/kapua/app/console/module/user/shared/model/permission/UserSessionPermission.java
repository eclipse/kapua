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
