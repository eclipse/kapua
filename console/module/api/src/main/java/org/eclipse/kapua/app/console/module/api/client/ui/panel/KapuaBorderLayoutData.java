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

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class KapuaBorderLayoutData extends BorderLayoutData {

    public KapuaBorderLayoutData(LayoutRegion region) {
        super(region);

        setSplit(true);
        setCollapsible(false);
        setHideCollapseTool(true);

        setMargins(new Margins(0, 0, 0, 0));
    }

    public KapuaBorderLayoutData(LayoutRegion region, float size) {
        this(region);
        setSize(size);
    }

    public void setMarginTop(int top) {
        if (top >= 0) {
            getMargins().top = top;
        }
    }

    public void setMarginRight(int right) {
        if (right >= 0) {
            getMargins().right = right;
        }
    }

    public void setMarginBottom(int bottom) {
        if (bottom >= 0) {
            getMargins().bottom = bottom;
        }
    }

    public void setMarginLeft(int left) {
        if (left >= 0) {
            getMargins().left = left;
        }
    }
}
