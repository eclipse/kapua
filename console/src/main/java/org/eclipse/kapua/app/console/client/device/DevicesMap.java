/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DevicesMap extends LayoutContainer {

    private final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final GwtDeviceServiceAsync gwtDeviceService = GWT.create(GwtDeviceService.class);

    public DevicesMap(DevicesView devicesView,
            GwtSession currentSession) {
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);
    }

    public void refresh(GwtDeviceQueryPredicates predicates)
    {
    }

    private void initMapOptions()
    {
    }

    private void placeMarkers(List<GwtDevice> devices) {
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
