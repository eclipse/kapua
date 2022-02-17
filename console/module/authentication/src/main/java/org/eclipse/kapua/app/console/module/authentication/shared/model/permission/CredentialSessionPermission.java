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
package org.eclipse.kapua.app.console.module.authentication.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class CredentialSessionPermission extends GwtSessionPermission {

    protected CredentialSessionPermission() {
        super();
    }

    private CredentialSessionPermission(GwtSessionPermissionAction action) {
        super("credential", action, GwtSessionPermissionScope.SELF);
    }

    public static CredentialSessionPermission read() {
        return new CredentialSessionPermission(GwtSessionPermissionAction.read);
    }

    public static CredentialSessionPermission write() {
        return new CredentialSessionPermission(GwtSessionPermissionAction.write);
    }

    public static CredentialSessionPermission delete() {
        return new CredentialSessionPermission(GwtSessionPermissionAction.delete);
    }
}
