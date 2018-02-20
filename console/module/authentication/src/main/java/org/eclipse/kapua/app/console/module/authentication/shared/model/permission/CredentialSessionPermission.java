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
