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
package org.eclipse.kapua.app.console.module.device.client.connection;

import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.views.AbstractViewDescriptor;
import org.eclipse.kapua.app.console.commons.client.views.EntityView;
import org.eclipse.kapua.app.console.commons.client.views.EntityViewDescriptor;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;

public class ConnectionViewDescriptor extends AbstractViewDescriptor implements EntityViewDescriptor<GwtDeviceConnection> {

    @Override
    public String getViewId() {
        return "connection";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.PLUG;
    }

    @Override
    public Integer getOrder() {
        return 300;
    }

    @Override
    public String getName() {
        return ConnectionView.getName();
    }

    @Override
    public EntityView<GwtDeviceConnection> getViewInstance(GwtSession currentSession) {
        return new ConnectionView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasConnectionReadPermission();
    }
}
