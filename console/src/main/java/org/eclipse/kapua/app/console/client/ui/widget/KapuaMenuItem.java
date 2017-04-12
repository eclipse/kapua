/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.ui.widget;

import org.eclipse.kapua.app.console.client.resources.icons.IconSet;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class KapuaMenuItem extends MenuItem {

    private IconSet iconSetIcon;

    public void setIcon(IconSet icon) {
        this.iconSetIcon = icon;
        setIcon((AbstractImagePrototype) null);
    }

    @Override
    public void setIcon(AbstractImagePrototype icon) {
        super.setIcon(icon);
        if (this.iconSetIcon != null && rendered) {
            Element e = DOM.createElement("i");
            El.fly(e).addStyleName("x-menu-item-icon", "fa", "fa-fw", iconSetIcon.getStyleName());
            el().insertChild(e, 0);
        }
    }
}
