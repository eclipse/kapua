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
package org.eclipse.kapua.app.console.module.job.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class JobSessionPermission extends GwtSessionPermission {

    protected JobSessionPermission() {
        super();
    }

    private JobSessionPermission(GwtSessionPermissionAction action) {
        this(action, GwtSessionPermissionScope.SELF);
    }

    private JobSessionPermission(GwtSessionPermissionAction action, GwtSessionPermissionScope scope) {
        super("job", action, scope);
    }

    public static JobSessionPermission read() {
        return new JobSessionPermission(GwtSessionPermissionAction.read);
    }

    public static JobSessionPermission write() {
        return new JobSessionPermission(GwtSessionPermissionAction.write);
    }

    public static JobSessionPermission delete() {
        return new JobSessionPermission(GwtSessionPermissionAction.delete);
    }

    public static JobSessionPermission deleteAll() {
        return new JobSessionPermission(GwtSessionPermissionAction.delete, GwtSessionPermissionScope.ALL);
    }

    public static JobSessionPermission execute() {
        return new JobSessionPermission(GwtSessionPermissionAction.execute);
    }
}
