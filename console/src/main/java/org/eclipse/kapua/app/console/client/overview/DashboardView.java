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
package org.eclipse.kapua.app.console.client.overview;

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

    private GwtSession m_currentSession;
    private ContentPanel centerPanel;
    private ContentPanel southPanel;

    private CenterOverviewView m_centerOverviewView;
    private BottomOverviewView m_bottomOverviewView;

    public DashboardView(GwtSession currentSession) {
        m_currentSession = currentSession;
    }

    /**
     * Add panel to different area of overview.
     */
    public void AddOverviewsPanel() {
        // Last message summary and last data received chart
        m_centerOverviewView = new CenterOverviewView(m_currentSession);
        centerPanel.add(m_centerOverviewView);

        // Usage chart and currently connected/disconnected device
        m_bottomOverviewView = new BottomOverviewView(m_currentSession);
        southPanel.add(m_bottomOverviewView);
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setBorders(false);
        setLayout(new FitLayout());

        LayoutContainer mainLayoutContainer = new LayoutContainer();
        mainLayoutContainer.setLayout(new BorderLayout());

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

        mainLayoutContainer.add(centerPanel, centerData);

        //
        // SOUTH PANEL
        //
        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, .28F);
        southData.setMargins(new Margins(5, 0, 0, 0));
        southData.setSplit(false);

        southPanel = new ContentPanel();
        southPanel.setBodyBorder(false);
        southPanel.setBorders(false);
        southPanel.setLayout(new FitLayout());
        southPanel.setHeaderVisible(false);

        mainLayoutContainer.add(southPanel, southData);

        //
        // Add all panels to root panel
        add(mainLayoutContainer);

        //
        // Add specific view in panels
        AddOverviewsPanel();
    }

    public void refresh() {
        if (rendered) {
            m_centerOverviewView.refresh();
        }
    }

}
