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
package org.eclipse.kapua.app.console.module.account.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class AccountSessionPermission extends GwtSessionPermission {

    protected AccountSessionPermission() {
        super();
    }

    private AccountSessionPermission(GwtSessionPermissionAction action) {
        super("account", action, GwtSessionPermissionScope.SELF);
    }

    public static AccountSessionPermission read() {
        return new AccountSessionPermission(GwtSessionPermissionAction.read);
    }

    public static AccountSessionPermission write() {
        return new AccountSessionPermission(GwtSessionPermissionAction.write);
    }

    public static AccountSessionPermission delete() {
        return new AccountSessionPermission(GwtSessionPermissionAction.delete);
    }
}
