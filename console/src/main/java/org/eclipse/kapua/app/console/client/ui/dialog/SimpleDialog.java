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
package org.eclipse.kapua.app.console.client.ui.dialog;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.Element;

public abstract class SimpleDialog extends ActionDialog
{
    protected ContentPanel m_bodyPanel;

    public SimpleDialog()
    {
        super();
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        super.onRender(parent, pos);

        //
        // Body setup
        m_bodyPanel = new ContentPanel();
        m_bodyPanel.setBorders(false);
        m_bodyPanel.setHeight(1000);
        m_bodyPanel.setBodyBorder(false);
        m_bodyPanel.setHeaderVisible(false);
        m_bodyPanel.setStyleAttribute("background-color", "#F0F0F0");
        m_bodyPanel.setBodyStyle("background-color:transparent");

        createBody();

        m_formPanel.add(m_bodyPanel);
    }

    public abstract void createBody();
}
