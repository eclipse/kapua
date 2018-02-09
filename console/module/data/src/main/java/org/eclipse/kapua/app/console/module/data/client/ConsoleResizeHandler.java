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
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

public class ConsoleResizeHandler {
    public ConsoleResizeHandler() {}

    public void addResizeHandler(final BorderLayoutData messageLayout, final int widthLimit) {
        Window.addResizeHandler(new ResizeHandler() {
        @Override
        public void onResize(ResizeEvent arg0) {
            float width = arg0.getWidth();
            if(width <= 300 && width > 150) {
                messageLayout.setMargins(new Margins(5, 5, 300, 5));
            } else if(width <= 400 && width > 300) {
                messageLayout.setMargins(new Margins(5, 5, 150, 5));
            } else if(width <= 450 && width > 400) {
                messageLayout.setMargins(new Margins(5, 5, 90, 5));
            } else if(width <= 500 && width > 450) {
                messageLayout.setMargins(new Margins(5, 5, 70, 5));
            } else if(width <= 650 && width > 500) {
                messageLayout.setMargins(new Margins(5, 5, 50, 5));
            } else if(width <= 900 && width > 650) {
                messageLayout.setMargins(new Margins(5, 5, 30, 5));
            } else if(width < widthLimit && width >900) {
                messageLayout.setMargins(new Margins(5, 5, 20, 5));
            } else if(width >= widthLimit) {
                messageLayout.setMargins(new Margins(5, 5, 5, 5));
            }
        }
    });
    }
}
