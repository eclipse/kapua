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
package org.eclipse.kapua.app.console.module.user.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserViewDescriptor extends AbstractEntityViewDescriptor<GwtUser> {

    @Override
    public EntityView<GwtUser> getViewInstance(GwtSession currentSession) {
        return new UserView(currentSession);
    }

    @Override
    public String getViewId() {
        return "user";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.USERS;
    }

    @Override
    public Integer getOrder() {
        return 700;
    }

    @Override
    public String getName() {
        return UserView.getName();
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(UserSessionPermission.read());
    }
}
