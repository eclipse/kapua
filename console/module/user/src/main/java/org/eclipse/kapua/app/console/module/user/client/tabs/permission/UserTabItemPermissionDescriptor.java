/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.user.client.tabs.permission;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.DomainSessionPermission;
import org.eclipse.kapua.app.console.module.user.client.UserView;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserTabItemPermissionDescriptor extends AbstractEntityTabDescriptor<GwtUser, UserTabItemPermission, UserView> {

    @Override
    public UserTabItemPermission getTabViewInstance(UserView view, GwtSession currentSession) {
        return new UserTabItemPermission(currentSession);
    }

    @Override
    public String getViewId() {
        return "user.permissions";
    }

    @Override
    public Integer getOrder() {
        return 400;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(AccessInfoSessionPermission.read())
            && currentSession.hasPermission(DomainSessionPermission.read());
    }
}
