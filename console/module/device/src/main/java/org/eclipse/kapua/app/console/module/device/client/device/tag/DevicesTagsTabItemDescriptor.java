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
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.device.shared.model.permission.DeviceSessionPermission;
import org.eclipse.kapua.app.console.module.tag.client.TagView;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;

public class DevicesTagsTabItemDescriptor extends AbstractEntityTabDescriptor<GwtTag, DevicesTagTabItem, TagView> {

    @Override
    public DevicesTagTabItem getTabViewInstance(TagView view, GwtSession currentSession) {
        return new DevicesTagTabItem(currentSession);
    }

    @Override
    public String getViewId() {
        return "tag.devices";
    }

    @Override
    public Integer getOrder() {
        return 200;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(DeviceSessionPermission.read()) && currentSession.hasPermission(AccessInfoSessionPermission.read());
    }
}

