/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.button;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;

public class SplitButton extends com.extjs.gxt.ui.client.widget.button.SplitButton {

    private String originalText;
    private KapuaIcon kapuaIcon;

    public SplitButton(String text, KapuaIcon kapuaIcon) {
        super();
        setText(text);
        setKapuaIcon(kapuaIcon);
    }

    @Override
    public String getText() {
        return originalText;
    }

    @Override
    public void setText(String text) {
        super.setText((kapuaIcon != null ? kapuaIcon.getInlineHTML() + " " : "") + text);
        this.originalText = text;
    }

    public void setKapuaIcon(KapuaIcon kapuaIcon) {
        super.setText(kapuaIcon.getInlineHTML() + " " + originalText);
        this.kapuaIcon = kapuaIcon;
    }

}
