/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client.tabs.roles;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.role.RoleView;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;

public class UsersRoleTabItemDescriptor extends AbstractEntityTabDescriptor<GwtRole, UsersRoleTabItem, RoleView> {

    @Override
    public String getViewId() {
        return "role.users";
    }

    @Override
    public Integer getOrder() {
        return 300;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }

    @Override
    public UsersRoleTabItem getTabViewInstance(RoleView view, GwtSession currentSession) {
        return new UsersRoleTabItem(currentSession);
    }
}
