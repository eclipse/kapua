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
