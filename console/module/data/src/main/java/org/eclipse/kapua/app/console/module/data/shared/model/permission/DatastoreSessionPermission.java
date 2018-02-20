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
package org.eclipse.kapua.app.console.module.data.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class DatastoreSessionPermission extends GwtSessionPermission {

    protected DatastoreSessionPermission() {
        super();
    }

    private DatastoreSessionPermission(GwtSessionPermissionAction action) {
        super("datastore", action, GwtSessionPermissionScope.SELF);
    }

    public static DatastoreSessionPermission read() {
        return new DatastoreSessionPermission(GwtSessionPermissionAction.read);
    }

    public static DatastoreSessionPermission write() {
        return new DatastoreSessionPermission(GwtSessionPermissionAction.write);
    }

    public static DatastoreSessionPermission delete() {
        return new DatastoreSessionPermission(GwtSessionPermissionAction.delete);
    }
}
