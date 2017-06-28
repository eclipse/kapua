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
package org.eclipse.kapua.app.console.module.device.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.views.EntityView;
import org.eclipse.kapua.app.console.commons.client.views.EntityViewDescriptor;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;

public class DeviceViewDescriptor implements EntityViewDescriptor<GwtDevice>, IsSerializable {

    @Override
    public String getId() {
        return "device_new";
    }

    @Override
    public String getName() {
        return "Device New!";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.ANCHOR;
    }

    @Override
    public EntityView<GwtDevice> getViewInstance(GwtSession currentSession) {
        return new DeviceView(currentSession);
    }

}
