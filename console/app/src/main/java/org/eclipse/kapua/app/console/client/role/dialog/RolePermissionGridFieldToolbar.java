/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.role.dialog;

import java.util.Date;

import org.eclipse.kapua.app.console.client.ui.widget.EntityGridFieldToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;

public class RolePermissionGridFieldToolbar extends EntityGridFieldToolbar<GwtRolePermission> {

    public RolePermissionGridFieldToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public GwtRolePermission getNewModel() {
        GwtRolePermission gwtRolePermission = new GwtRolePermission();
        gwtRolePermission.setCreatedOn(new Date());
        gwtRolePermission.setCreatedBy(currentSession.getUser().getId());
        gwtRolePermission.setTargetScopeId(currentSession.getSelectedAccount().getId());
        return gwtRolePermission;
    }

}
