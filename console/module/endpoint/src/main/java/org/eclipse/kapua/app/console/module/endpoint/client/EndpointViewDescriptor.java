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
        return currentSession.hasPermission(EndpointSessionPermission.readAll()) &&
                (
                        currentSession.isSelectedAccountRootLevel()
                                ||
                                currentSession.isSelectedAccountFirstLevel()
                );
    }
}
