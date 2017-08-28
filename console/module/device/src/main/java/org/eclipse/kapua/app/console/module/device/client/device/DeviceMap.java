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
package org.eclipse.kapua.app.console.module.device.client.device;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;

public class DeviceMap extends LayoutContainer {

    public DeviceMap(DeviceView devicesView,
            GwtSession currentSession) {
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);
    }

    public void refresh(GwtDeviceQueryPredicates predicates) {
    }


    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------

    public void onUnload() {
        // clean-up the Marker list
        // for (Marker marker : m_markers) {
        // Event.clearInstanceListeners(marker);
        // }
        super.onUnload();
    }

    public void onResize(int width, int height) {
        // m_mapWidget.setSize(String.valueOf(width), String.valueOf(height));
        super.onResize(width, height);
    }
}
