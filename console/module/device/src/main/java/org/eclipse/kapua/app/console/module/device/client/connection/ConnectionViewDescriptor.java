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
package org.eclipse.kapua.app.console.module.device.client.connection;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceConnectionSessionPermission;

public class ConnectionViewDescriptor extends AbstractEntityViewDescriptor<GwtDeviceConnection> {

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
        return 200;
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
        return currentSession.hasPermission(DeviceConnectionSessionPermission.read());
    }
}
