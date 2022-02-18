/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.client.role.dialog;

import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityGridFieldToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;

import java.util.Date;

public class RolePermissionGridFieldToolbar extends EntityGridFieldToolbar<GwtRolePermission> {

    public RolePermissionGridFieldToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public GwtRolePermission getNewModel() {
        GwtRolePermission gwtRolePermission = new GwtRolePermission();
        gwtRolePermission.setCreatedOn(new Date());
        gwtRolePermission.setCreatedBy(currentSession.getUserId());
        gwtRolePermission.setTargetScopeId(currentSession.getSelectedAccountId());
        return gwtRolePermission;
    }

}
