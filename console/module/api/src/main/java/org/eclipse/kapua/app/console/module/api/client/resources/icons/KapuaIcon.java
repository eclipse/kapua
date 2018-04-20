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
package org.eclipse.kapua.app.console.module.api.client.resources.icons;

import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;

public class KapuaIcon extends Text {

    private IconSet icon;
    private Color color;
    private Integer emSize;
    private String title;
    private boolean spin;

    private static Color defaultColor = Color.BLUE_KAPUA;

    public KapuaIcon(IconSet icon) {
        super();

        this.icon = icon;
        this.color = defaultColor;
    }

    public static Color getDefaultColor() {
        return defaultColor;
    }

    public static void setDefaultColor(Color defaultColor) {
        KapuaIcon.defaultColor = defaultColor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEmSize(Integer emSize) {
        this.emSize = emSize;
    }

    public void setSpin(boolean spin) {
        this.spin = spin;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        setText(getInlineHTML());
    }

    public String getInlineHTML() {
        StringBuilder sb = new StringBuilder("<i class='fa ");
        sb.append(icon.getStyleName());

        //
        // Spin
        if (spin) {
            sb.append(" fa-spin");
        }

        //
        // Size
        if (emSize != null) {
            switch (emSize) {
            case 2:
                sb.append(" fa-2x");
                break;
            case 3:
                sb.append(" fa-3x");
                break;
            case 4:
                sb.append(" fa-4x");
                break;
            case 5:
                sb.append(" fa-5x");
                break;
            default:
                sb.append(" fa-lg");
                break;
            }
        } else {
            sb.append(" fa-lg");
        }

        sb.append("' ");

        //
        // Color
        if (color != null) {
            sb.append("style='color:rgb(") //
                    .append(color.getR()) //
                    .append(",") //
                    .append(color.getG()) //
                    .append(",") //
                    .append(color.getB()) //
                    .append(")' ");
        }

        //
        // Tooltip
        if (title != null) {
            sb.append("title='")
                    .append(title)
                    .append("' ");
        }
        sb.append("></i>");

        return sb.toString();
    }
}
