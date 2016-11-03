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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.resources.icons;

import org.eclipse.kapua.app.console.client.ui.misc.color.Color;

import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;

public class KapuaIcon extends Text {

    IconSet icon;
    Color color;
    Integer emSize;

    public KapuaIcon(IconSet icon) {
        super();

        this.icon = icon;
        this.color = Color.BLUE_KAPUA;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEmSize(Integer emSize) {
        this.emSize = emSize;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        setText(getInlineHTML());
    }

    public String getInlineHTML() {
        StringBuilder sb = new StringBuilder("<i class='fa ");
        sb.append(icon.getStyleName());

        if (emSize != null) {
            switch (emSize) {
            case 2:
                sb.append(" fa-2x' ");
                break;
            default:
                sb.append(" fa-lg' ");
                break;
            }
        } else {
            sb.append(" fa-lg' ");
        }

        if (color != null) {
            sb.append("style='color:rgb(") //
                    .append(color.getR()) //
                    .append(",") //
                    .append(color.getG()) //
                    .append(",") //
                    .append(color.getB()) //
                    .append(")' ");
        }

        sb.append("></i>");

        return sb.toString();
    }
}
