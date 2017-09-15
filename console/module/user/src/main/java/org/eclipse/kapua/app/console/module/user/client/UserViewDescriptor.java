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
package org.eclipse.kapua.app.console.module.user.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

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
        return 800;
    }

    @Override
    public String getName() {
        return UserView.getName();
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasUserReadPermission();
    }
}
