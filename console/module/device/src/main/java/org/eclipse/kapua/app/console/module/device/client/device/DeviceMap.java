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
package org.eclipse.kapua.app.console.module.device.client.device;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;

public class DeviceMap extends LayoutContainer {

    public DeviceMap(DeviceView devicesView,
            GwtSession currentSession) {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
    }

    public void refresh(GwtDeviceQueryPredicates predicates) {
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------

    @Override
    public void onUnload() {
        // clean-up the Marker list
        // for (Marker marker : m_markers) {
        // Event.clearInstanceListeners(marker);
        // }
        super.onUnload();
    }

    @Override
    public void onResize(int width, int height) {
        // m_mapWidget.setSize(String.valueOf(width), String.valueOf(height));
        super.onResize(width, height);
    }
}
