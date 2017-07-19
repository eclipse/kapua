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

import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.views.AbstractViewDescriptor;
import org.eclipse.kapua.app.console.commons.client.views.View;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;

public class DevicesViewDescriptor extends AbstractViewDescriptor {

    @Override
    public String getViewId() {
        return "devices";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.HDD_O;
    }

    @Override
    public Integer getOrder() {
        return 200;
    }

    @Override
    public String getName() {
        return DevicesView.getName();
    }

    @Override
    public View getViewInstance(GwtSession currentSession) {
        return new DevicesView(currentSession);
    }
}
