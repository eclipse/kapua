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
package org.eclipse.kapua.app.console.module.api.client.ui.panel;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.widget.Layout;

public class ContentPanel extends com.extjs.gxt.ui.client.widget.ContentPanel {

    private String originalHeading;
    private KapuaIcon icon;

    public ContentPanel() {
        super();
    }

    public ContentPanel(Layout layout) {
        super(layout);
    }

    @Override
    public String getHeading() {
        return originalHeading;
    }

    @Override
    public void setHeading(String heading) {
        super.setHeading((icon != null ? icon.getInlineHTML() + "&nbsp;&nbsp;" : "") + heading);
        this.originalHeading = heading;
    }

    public void setIcon(KapuaIcon icon) {
        super.setHeading(icon.getInlineHTML() + "&nbsp;&nbsp;" + originalHeading);
        this.icon = icon;
    }

}
