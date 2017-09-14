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
package org.eclipse.kapua.app.console.module.device.client.device.configuration;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.device.DeviceView;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;

public class DeviceTabConfigurationDescriptor extends AbstractEntityTabDescriptor<GwtDevice, DeviceTabConfiguration, DeviceView> {

    @Override
    public DeviceTabConfiguration getTabViewInstance(DeviceView view, GwtSession currentSession) {
        return new DeviceTabConfiguration(currentSession);
    }

    @Override
    public String getViewId() {
        return "device.configuration";
    }

    @Override
    public Integer getOrder() {
        return 500;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
