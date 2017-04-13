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
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.dashboard;

import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

public class DashboardView extends LayoutContainer {

    @SuppressWarnings("unused")
    private GwtSession m_currentSession;
    private ContentPanel centerPanel;

    public DashboardView(GwtSession currentSession) {
        m_currentSession = currentSession;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setBorders(false);
        setLayout(new FitLayout());

        //
        // CENTER DATA
        //
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 0, 0, 0));
        centerData.setSplit(false);

        centerPanel = new ContentPanel();
        centerPanel.setBodyBorder(false);
        centerPanel.setBorders(false);
        centerPanel.setLayout(new FitLayout());
        centerPanel.setHeaderVisible(false);


        LayoutContainer mainLayoutContainer = new LayoutContainer();
        mainLayoutContainer.setLayout(new BorderLayout());
        mainLayoutContainer.add(centerPanel, centerData);

        add(mainLayoutContainer);
    }

    public void refresh() {
        if (rendered) {
        }
    }

}
