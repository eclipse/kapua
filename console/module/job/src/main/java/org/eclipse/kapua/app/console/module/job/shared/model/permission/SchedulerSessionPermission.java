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
