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
package org.eclipse.kapua.app.console.module.endpoint.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.permission.EndpointSessionPermission;

public class EndpointViewDescriptor extends AbstractEntityViewDescriptor<GwtEndpoint> {

    @Override
    public String getViewId() {
        return "endpoints";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.GLOBE;
    }

    @Override
    public Integer getOrder() {
        return 1050;
    }

    @Override
    public String getName() {
        return EndpointView.getName();
    }

    @Override
    public EntityView<GwtEndpoint> getViewInstance(GwtSession currentSession) {
        return new EndpointView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(EndpointSessionPermission.readAll());
    }
}
