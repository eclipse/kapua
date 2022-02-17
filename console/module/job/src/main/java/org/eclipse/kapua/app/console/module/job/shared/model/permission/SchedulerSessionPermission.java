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

public class SchedulerSessionPermission extends GwtSessionPermission {

    protected SchedulerSessionPermission() {
        super();
    }

    private SchedulerSessionPermission(GwtSessionPermissionAction action) {
        super("scheduler", action, GwtSessionPermissionScope.SELF);
    }

    public static SchedulerSessionPermission read() {
        return new SchedulerSessionPermission(GwtSessionPermissionAction.read);
    }

    public static SchedulerSessionPermission write() {
        return new SchedulerSessionPermission(GwtSessionPermissionAction.write);
    }

    public static SchedulerSessionPermission delete() {
        return new SchedulerSessionPermission(GwtSessionPermissionAction.delete);
    }
}
