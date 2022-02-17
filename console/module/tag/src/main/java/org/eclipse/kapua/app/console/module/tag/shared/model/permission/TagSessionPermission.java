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
package org.eclipse.kapua.app.console.module.tag.shared.model.permission;

import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;

public class TagSessionPermission extends GwtSessionPermission {

    protected TagSessionPermission() {
        super();
    }

    private TagSessionPermission(GwtSessionPermissionAction action) {
        super("tag", action, GwtSessionPermissionScope.SELF);
    }

    public static TagSessionPermission read() {
        return new TagSessionPermission(GwtSessionPermissionAction.read);
    }

    public static TagSessionPermission write() {
        return new TagSessionPermission(GwtSessionPermissionAction.write);
    }

    public static TagSessionPermission delete() {
        return new TagSessionPermission(GwtSessionPermissionAction.delete);
    }
}
