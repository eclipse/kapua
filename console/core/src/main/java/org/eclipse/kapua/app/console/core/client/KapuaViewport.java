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
package org.eclipse.kapua.app.console.core.client;

import com.extjs.gxt.ui.client.widget.Viewport;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

/**
 * Extension of basic Viewport that monitors browser window resize and reacts
 * with displaying message when window is too small. It also disables user
 * interaction with UI if too small for usage.
 */
public class KapuaViewport extends Viewport {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    /**
     * Minimum width of browser window.
     */
    public static final int MIN_CLIENT_WIDTH = 900;

    /**
     * Minimum height of browser window.
     */
    public static final int MIN_CLIENT_HEIGHT = 530;

    private InfoPopup infoPopup;

    public KapuaViewport() {
        super();

        setMonitorWindowResize(true);
        infoPopup = new InfoPopup(MSGS.browserWindowTooSmall());
        int clientHeight = Window.getClientHeight();
        int clientWidth = Window.getClientWidth();
        // React at browser size at login, before any resize is made
        if ((clientHeight < MIN_CLIENT_HEIGHT) || (clientWidth < MIN_CLIENT_WIDTH)) {
            infoPopup.center();
            infoPopup.show();
        }
    }

    @Override
    protected void onWindowResize(final int width, final int height) {
        if ((width < MIN_CLIENT_WIDTH) || (height < MIN_CLIENT_HEIGHT)) {
            infoPopup.center();
            infoPopup.show();
        } else {
            if (infoPopup != null) {
                infoPopup.hide();
            }
        }

        super.onWindowResize(width, height);
    }

}
