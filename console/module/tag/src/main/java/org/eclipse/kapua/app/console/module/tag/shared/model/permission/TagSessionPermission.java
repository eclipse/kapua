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
