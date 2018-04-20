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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;

public class RoleViewDescriptor extends AbstractEntityViewDescriptor<GwtRole> {

    @Override
    public String getViewId() {
        return "role";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.STREET_VIEW;
    }

    @Override
    public Integer getOrder() {
        return 800;
    }

    @Override
    public String getName() {
        return RoleView.getName();
    }

    @Override
    public EntityView<GwtRole> getViewInstance(GwtSession currentSession) {
        return new RoleView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(RoleSessionPermission.read());
    }
}
