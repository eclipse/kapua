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

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceServiceAsync;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class BottomOverviewView extends LayoutContainer
{

    private final GwtDeviceServiceAsync  gwtDeviceService             = GWT.create(GwtDeviceService.class);

    private static final ConsoleMessages MSGS                         = GWT.create(ConsoleMessages.class);
    private GwtSession                   m_currentSession;

    private String                       chartColorMissingDevice      = "#CC5151";
    private String                       chartColorDisconnectedDevice = "#F9AC63";                         // Orange
    private String                       chartColorConnectedDevice    = "#51CC51";                         // Green
    private String                       chartColorNoDeviceFound      = "#8E8E8E";                         // Gray

    public BottomOverviewView(GwtSession currentSession)
    {
        m_currentSession = currentSession;
    }

    protected void onRender(final Element parent, int index)
    {
        super.onRender(parent, index);

        setLayout(new FitLayout());

        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setBorders(false);
        mainPanel.setBodyBorder(false);
        mainPanel.setHeaderVisible(false);
        mainPanel.setLayout(new FitLayout());

        add(mainPanel);

        LayoutContainer layoutContainer = new LayoutContainer();
        layoutContainer.setLayout(new BorderLayout());

        mainPanel.add(layoutContainer);

        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 0, 0, 0));
        centerData.setSplit(false);

        ContentPanel centerPanel = new ContentPanel();
        centerPanel.setBodyBorder(false);
        centerPanel.setBorders(false);
        centerPanel.setHeaderVisible(false);
        centerPanel.setLayout(new FitLayout());

        layoutContainer.add(centerPanel, centerData);

    }

}
