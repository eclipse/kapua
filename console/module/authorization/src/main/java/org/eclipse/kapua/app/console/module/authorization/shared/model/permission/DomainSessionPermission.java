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

public class DomainSessionPermission extends GwtSessionPermission {

    protected DomainSessionPermission() {
        super();
    }

    private DomainSessionPermission(GwtSessionPermissionAction action) {
        super("domain", action, GwtSessionPermissionScope.SELF);
    }

    public static DomainSessionPermission read() {
        return new DomainSessionPermission(GwtSessionPermissionAction.read);
    }

    public static DomainSessionPermission write() {
        return new DomainSessionPermission(GwtSessionPermissionAction.write);
    }

    public static DomainSessionPermission delete() {
        return new DomainSessionPermission(GwtSessionPermissionAction.delete);
    }
}
