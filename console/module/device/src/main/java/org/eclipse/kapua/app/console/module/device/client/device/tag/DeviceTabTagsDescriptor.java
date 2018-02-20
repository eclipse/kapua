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
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.device.DeviceView;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.tag.shared.model.permission.TagSessionPermission;

public class DeviceTabTagsDescriptor extends AbstractEntityTabDescriptor<GwtDevice, DeviceTabTags, DeviceView> {

    @Override
    public DeviceTabTags getTabViewInstance(DeviceView view, GwtSession currentSession) {
        return new DeviceTabTags(currentSession);
    }

    @Override
    public String getViewId() {
        return "device.tags";
    }

    @Override
    public Integer getOrder() {
        return 150;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(TagSessionPermission.read());
    }
}
