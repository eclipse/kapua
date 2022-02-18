/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.dialog;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.user.client.Element;

public abstract class TabbedDialog extends ActionDialog {

    protected TabPanel tabsPanel;

    public TabbedDialog() {
        super();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        //
        // Tabs setup
        tabsPanel = new TabPanel();
        tabsPanel.setPlain(true);
        tabsPanel.setBorders(false);
        tabsPanel.setHeight(1000);
        tabsPanel.setBodyBorder(false);

        createTabItems();

        // Color tab items background
        for (TabItem t : tabsPanel.getItems()) {
            t.setStyleAttribute("background-color", "#E8E8E8");
        }

        formPanel.add(tabsPanel);
    }

    public abstract void createTabItems();
}
