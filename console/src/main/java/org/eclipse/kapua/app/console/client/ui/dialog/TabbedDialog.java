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

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.user.client.Element;

public abstract class TabbedDialog extends ActionDialog
{
    protected TabPanel m_tabsPanel;

    public TabbedDialog()
    {
        super();
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        super.onRender(parent, pos);

        //
        // Tabs setup
        m_tabsPanel = new TabPanel();
        m_tabsPanel.setPlain(true);
        m_tabsPanel.setBorders(false);
        m_tabsPanel.setHeight(1000);
        m_tabsPanel.setBodyBorder(false);

        createTabItems();

        // Color tab items background
        for (TabItem t : m_tabsPanel.getItems()) {
            t.setStyleAttribute("background-color", "#E8E8E8");
        }

        m_formPanel.add(m_tabsPanel);
    }

    public abstract void createTabItems();
}
